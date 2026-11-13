package com.molinex.platform.iam.application.commandservices;

public record AuthenticationResult(
        Integer id,
        String email,
        String role,
        String token
) {
}
