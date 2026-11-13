package com.molinex.platform.iam.interfaces.rest.transform;

import com.molinex.platform.iam.domain.model.commands.AuthenticateUserCommand;

import com.molinex.platform.iam.interfaces.rest.resources.AuthenticateUserResource;

public final class AuthenticateUserCommandFromResourceAssembler {
    private AuthenticateUserCommandFromResourceAssembler() {
    }

    public static AuthenticateUserCommand toCommandFromResource(AuthenticateUserResource r) {
        return new AuthenticateUserCommand(r.email() == null ? r.username() : r.email(), r.password());
    }
}
