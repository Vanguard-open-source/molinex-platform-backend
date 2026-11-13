package com.molinex.platform.iam;

import com.molinex.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.molinex.platform.iam.domain.model.aggregates.User;
import com.molinex.platform.iam.domain.model.valueobjects.AccountStatus;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.iam.domain.model.valueobjects.PasswordHash;
import com.molinex.platform.iam.domain.model.valueobjects.Role;
import com.molinex.platform.iam.domain.model.valueobjects.UserProfile;
import com.molinex.platform.iam.domain.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.json.JsonMapper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:iam-tests;MODE=MySQL;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "JWT_SECRET=" + AuthenticationIntegrationTests.TEST_SECRET
})
class AuthenticationIntegrationTests {
    static final String TEST_SECRET = "test-signing-key-only-used-by-authentication-integration-tests";
    private static final String PASSWORD = "correct-password";

    @Autowired
    UserRepository users;

    @Autowired
    HashingService hashing;

    private MockMvc mvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    void authenticatesActiveAccountAndSignsToken() throws Exception {
        var user = account(AccountStatus.ACTIVE);
        var response = login(user.getEmail().value(), PASSWORD)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("operator"))
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andReturn().getResponse();
        var token = JsonMapper.builder().build().readTree(response.getContentAsString()).get("token").asText();
        var key = Keys.hmacShaKeyFor(TEST_SECRET.getBytes(StandardCharsets.UTF_8));
        var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
        assertEquals(user.getId().value().toString(), claims.getSubject());
        assertEquals("operator", claims.get("role"));
    }

    @Test
    void rejectsIncorrectPasswordUnknownEmailAndUnavailableAccounts() throws Exception {
        assertRejected(login(account(AccountStatus.ACTIVE).getEmail().value(), "wrong"));
        assertRejected(login("missing@example.com", PASSWORD));
        assertRejected(login(account(AccountStatus.INACTIVE).getEmail().value(), PASSWORD));
        assertRejected(login(account(AccountStatus.LOCKED).getEmail().value(), PASSWORD));
    }

    @Test
    void rejectsMissingPassword() throws Exception {
        login("ana@example.com", "").andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.token").doesNotExist());
    }

    private User account(AccountStatus status) {
        return users.save(new User(null, new EmailAddress(UUID.randomUUID() + "@example.com"),
                new PasswordHash(hashing.encode(PASSWORD)), new UserProfile("Ana"), Role.OPERATOR, status));
    }

    private ResultActions login(String email, String password) throws Exception {
        return mvc.perform(post("/api/v1/tokens").contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"email":"%s","password":"%s"}
                        """.formatted(email, password)));
    }

    private void assertRejected(ResultActions result) throws Exception {
        result.andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.detail").value("Invalid credentials"))
                .andExpect(jsonPath("$.token").doesNotExist());
    }
}
