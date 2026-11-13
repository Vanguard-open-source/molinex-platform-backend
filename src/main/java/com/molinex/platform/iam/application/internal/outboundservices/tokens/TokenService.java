package com.molinex.platform.iam.application.internal.outboundservices.tokens;

import java.util.Optional;

import com.molinex.platform.iam.interfaces.acl.AuthenticatedPrincipal;

public interface TokenService {
    String issue(AuthenticatedPrincipal principal);

    Optional<AuthenticatedPrincipal> validate(String token);
}
