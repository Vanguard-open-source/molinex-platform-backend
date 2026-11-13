package com.molinex.platform.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        Integer id,
        String email,
        String role,
        String token
) {
}
