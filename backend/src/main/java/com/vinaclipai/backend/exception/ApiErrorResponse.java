package com.vinaclipai.backend.exception;

import java.time.Instant;

public record ApiErrorResponse(
    String code,
    String message,
    Instant timestamp
) {
    public static ApiErrorResponse internalServerError(String message) {
        return new ApiErrorResponse("INTERNAL_SERVER_ERROR", message, Instant.now());
    }
}
