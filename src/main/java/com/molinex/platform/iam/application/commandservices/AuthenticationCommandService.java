package com.molinex.platform.iam.application.commandservices;

import com.molinex.platform.iam.domain.model.commands.AuthenticateUserCommand;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.Result;

public interface AuthenticationCommandService {
    Result<AuthenticationResult, ApplicationError> handle(AuthenticateUserCommand command);
}
