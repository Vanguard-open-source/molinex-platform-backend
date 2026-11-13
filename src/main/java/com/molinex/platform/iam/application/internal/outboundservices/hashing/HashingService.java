package com.molinex.platform.iam.application.internal.outboundservices.hashing;

public interface HashingService {
    String encode(String password);

    boolean matches(String password, String encoded);
}
