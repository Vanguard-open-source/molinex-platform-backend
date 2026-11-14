package com.molinex.platform.shared.application.result;

import java.util.Locale;

public record ApplicationError(String code, String message, ErrorType type) {
    public static ApplicationError validationError(String field, String reason) {
        return new ApplicationError("VALIDATION_ERROR", field + ": " + reason, ErrorType.VALIDATION);
    }

    public static ApplicationError notFound(String resource, String id) {
        return new ApplicationError(resource.toUpperCase(Locale.ROOT) + "_NOT_FOUND",
                resource + " not found: " + id, ErrorType.NOT_FOUND);
    }

    public static ApplicationError conflict(String resource, String reason) {
        return new ApplicationError(resource.toUpperCase(Locale.ROOT) + "_CONFLICT", reason, ErrorType.CONFLICT);
    }

    public static ApplicationError unexpected(String context, String reason) {
        return new ApplicationError("UNEXPECTED_ERROR", context + ": " + reason, ErrorType.INTERNAL);
    }
}
