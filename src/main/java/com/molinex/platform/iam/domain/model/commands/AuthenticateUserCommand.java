package com.molinex.platform.iam.domain.model.commands;

public record AuthenticateUserCommand(
        String email,
        String password
) {
}
