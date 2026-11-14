package com.molinex.platform.production.interfaces.rest;

import com.molinex.platform.shared.application.result.ApplicationError;
import com.molinex.platform.shared.application.result.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

final class ProductionProblems {

    private ProductionProblems() {
    }

    static ResponseEntity<ProblemDetail> from(ApplicationError error) {
        var status = switch (error.type()) {
            case VALIDATION -> HttpStatus.BAD_REQUEST;
            case NOT_FOUND -> HttpStatus.NOT_FOUND;
            case CONFLICT -> HttpStatus.CONFLICT;
            case UNAUTHORIZED -> HttpStatus.UNAUTHORIZED;
            case FORBIDDEN -> HttpStatus.FORBIDDEN;
            case INTERNAL -> HttpStatus.INTERNAL_SERVER_ERROR;
        };
        var problem = ProblemDetail.forStatusAndDetail(status, error.message());
        problem.setProperty("code", error.code());
        return ResponseEntity.of(problem).build();
    }

    static ResponseEntity<ProblemDetail> invalid(String code, String message) {
        return from(new ApplicationError(code, message, ErrorType.VALIDATION));
    }

    static ResponseEntity<ProblemDetail> notFound(String code, String message) {
        return from(new ApplicationError(code, message, ErrorType.NOT_FOUND));
    }
}
