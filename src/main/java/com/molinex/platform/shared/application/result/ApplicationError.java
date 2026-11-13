package com.molinex.platform.shared.application.result;

public record ApplicationError(String code, String message, ErrorType type) {
}
