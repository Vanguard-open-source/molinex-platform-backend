package com.molinex.platform.shared.application.result;

import java.util.Objects;

public final class Result<T, E> {

    private final T value;
    private final E error;

    private Result(T value, E error) {
        this.value = value;
        this.error = error;
    }

    public static <T, E> Result<T, E> success(T value) {
        return new Result<>(Objects.requireNonNull(value), null);
    }

    public static <T, E> Result<T, E> failure(E error) {
        return new Result<>(null, Objects.requireNonNull(error));
    }

    public boolean isSuccess() {
        return error == null;
    }

    public T value() {
        return value;
    }

    public E error() {
        return error;
    }
}
