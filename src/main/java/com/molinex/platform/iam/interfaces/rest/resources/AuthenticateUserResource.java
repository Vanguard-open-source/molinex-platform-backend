package com.molinex.platform.iam.interfaces.rest.resources;

public record AuthenticateUserResource(
        String email,
        String username,
        String password
) {
}
