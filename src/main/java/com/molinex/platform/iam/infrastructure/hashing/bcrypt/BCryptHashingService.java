package com.molinex.platform.iam.infrastructure.hashing.bcrypt;

import com.molinex.platform.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class BCryptHashingService implements HashingService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(String password) {
        return encoder.encode(password);
    }

    public boolean matches(String password, String encoded) {
        return password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72 && encoder.matches(password, encoded);
    }
}
