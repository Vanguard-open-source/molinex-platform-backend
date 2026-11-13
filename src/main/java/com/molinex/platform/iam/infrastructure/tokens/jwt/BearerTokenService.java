package com.molinex.platform.iam.infrastructure.tokens.jwt;

import java.time.temporal.ChronoUnit;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

import com.molinex.platform.iam.interfaces.acl.AuthenticatedPrincipal;
import com.molinex.platform.iam.domain.model.valueobjects.Role;
import com.molinex.platform.iam.application.internal.outboundservices.tokens.TokenService;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

@Service
public class BearerTokenService implements TokenService {
    private final SecretKey key;
    private final long expirationDays;

    public BearerTokenService(
            @Value("${JWT_SECRET:}")
            String secret,
            @Value("${JWT_EXPIRATION_DAYS:1}")
            long expirationDays) {
        this.key = secret.isBlank() ? null : Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        if (expirationDays <= 0) throw new IllegalArgumentException("JWT expiration must be positive");
        this.expirationDays = expirationDays;
    }

    public String issue(AuthenticatedPrincipal principal) {
        if (key == null) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, "JWT signing is unavailable");
        }
        var now = Instant.now();
        return Jwts.builder().subject(principal.subject().toString()).claim("role", principal.role())
                .issuedAt(Date.from(now)).expiration(Date.from(now.plus(expirationDays, ChronoUnit.DAYS))).signWith(key)
                .compact();
    }

    public Optional<AuthenticatedPrincipal> validate(String token) {
        if (key == null) return Optional.empty();
        try {
            var claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(token).getPayload();
            if (claims.getExpiration() == null || claims.getIssuedAt() == null || claims.getSubject() == null ||
                    claims.get("role") == null) return Optional.empty();
            var role = Role.fromValue(claims.get("role", String.class));
            var subject = Integer.valueOf(claims.getSubject());
            if (subject <= 0) return Optional.empty();
            return Optional.of(new AuthenticatedPrincipal(subject, role.value()));
        } catch (JwtException | IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
