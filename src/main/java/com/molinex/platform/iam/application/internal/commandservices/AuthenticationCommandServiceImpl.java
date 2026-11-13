package com.molinex.platform.iam.application.internal.commandservices;

import com.molinex.platform.iam.domain.model.valueobjects.AccountStatus;
import com.molinex.platform.iam.domain.model.valueobjects.EmailAddress;
import com.molinex.platform.iam.domain.model.commands.AuthenticateUserCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.ErrorType;
import com.molinex.platform.shared.application.result.Result;
import org.springframework.stereotype.Service;

import com.molinex.platform.iam.application.commandservices.AuthenticationCommandService;
import com.molinex.platform.iam.application.commandservices.AuthenticationResult;
import com.molinex.platform.iam.application.internal.outboundservices.hashing.HashingService;
import com.molinex.platform.iam.application.internal.outboundservices.tokens.TokenService;
import com.molinex.platform.iam.domain.repositories.UserRepository;
import com.molinex.platform.iam.interfaces.acl.AuthenticatedPrincipal;
import com.molinex.platform.shared.application.events.DomainEventPublisher;

import java.util.UUID;

@Service
public class AuthenticationCommandServiceImpl implements AuthenticationCommandService {
    private final UserRepository repository;
    private final HashingService hashing;
    private final TokenService tokens;
    private final DomainEventPublisher events;
    private final String dummyHash;

    public AuthenticationCommandServiceImpl(UserRepository repository, HashingService hashing,
                                            TokenService tokens, DomainEventPublisher events) {
        this.repository = repository;
        this.hashing = hashing;
        this.tokens = tokens;
        this.events = events;
        this.dummyHash = hashing.encode(UUID.randomUUID().toString());
    }


    public Result<AuthenticationResult, ApplicationError> handle(AuthenticateUserCommand command) {
        if (command.email() == null || command.email().isBlank() || command.password() == null ||
                command.password().isBlank())
            return Result.failure(ApplicationError.validationError("credentials", "email and password are required"));
        final EmailAddress email;
        try {
            email = new EmailAddress(command.email());
        } catch (IllegalArgumentException ex) {
            return invalid();
        }
        var user = repository.findByEmail(email);
        var hash = user.map(account -> account.getCredential().value()).orElse(dummyHash);
        if (!hashing.matches(command.password(), hash) || user.isEmpty()
                || user.get().getStatus() != AccountStatus.ACTIVE) {
            return invalid();
        }
        var account = user.get();
        var token = tokens.issue(new AuthenticatedPrincipal(account.getId().value(), account.getRole().value()));
        account.onAuthenticated();
        events.publishAndClearEvents(account);
        return Result.success(
                new AuthenticationResult(account.getId().value(), account.getEmail().value(), account.getRole().value(), token));
    }

    private Result<AuthenticationResult, ApplicationError> invalid() {
        return Result.failure(new ApplicationError("AUTHENTICATION_ERROR", "Invalid credentials", ErrorType.UNAUTHORIZED));
    }
}
