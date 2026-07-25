package com.vinaclipai.backend.dto.response;

public record HealthResponse(
    String status,
    String service,
    String database
) {
    public static HealthResponse up() {
        return new HealthResponse("UP", "core-backend", "UP");
    }

    public static HealthResponse down() {
        return new HealthResponse("DOWN", "core-backend", "DOWN");
    }
}
