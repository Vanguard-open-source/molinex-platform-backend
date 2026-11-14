package com.molinex.platform.commercial;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "spring.datasource.url=${MOLINEX_COMMERCIAL_TEST_URL:jdbc:h2:mem:molinex-commercial;MODE=MySQL;DB_CLOSE_DELAY=-1}",
        "spring.datasource.username=${MOLINEX_TEST_DB_USER:sa}",
        "spring.datasource.password=${MOLINEX_TEST_DB_PASSWORD:}",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.open-in-view=false"
})
class CommercialInquiryIntegrationTests {

    private static final String INQUIRIES = "/api/v1/commercial-inquiries";
    private static final String INQUIRY = """
            {"fullName":"Ana Perez","email":"ana.perez@molinosur.pe","phoneNumber":"+51987654321",
             "companyName":"Molino Sur","message":"We would like a quote for our mill."}
            """;
    private static final String INVALID_INQUIRY = """
            {"fullName":"Ana Perez","email":"not-an-email","companyName":"","message":"Hello"}
            """;

    private MockMvc mvc;

    @BeforeEach
    void setUp(@Autowired WebApplicationContext context) {
        mvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @DisplayName("US-36 E1: A valid inquiry from the Landing Page is persisted and confirmed with 201")
    void persistsInquiryAndConfirms() throws Exception {
        mvc.perform(post(INQUIRIES).contentType(MediaType.APPLICATION_JSON).content(INQUIRY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.fullName").value("Ana Perez"))
                .andExpect(jsonPath("$.companyName").value("Molino Sur"))
                .andExpect(jsonPath("$.submittedAt").isNotEmpty());
    }

    @Test
    @DisplayName("US-36 E2 and E3: Empty required fields or an invalid email are rejected with 400")
    void rejectsInvalidInquiry() throws Exception {
        mvc.perform(post(INQUIRIES).contentType(MediaType.APPLICATION_JSON).content(INVALID_INQUIRY))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.id").doesNotExist());
    }
}
