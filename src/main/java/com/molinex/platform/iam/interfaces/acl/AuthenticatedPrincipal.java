package com.molinex.platform.iam.interfaces.acl;

public record AuthenticatedPrincipal(
        Integer subject,
        String role
) {
}
