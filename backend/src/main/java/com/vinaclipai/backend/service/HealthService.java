package com.vinaclipai.backend.service;

import com.vinaclipai.backend.dto.response.HealthResponse;

public interface HealthService {
    HealthResponse check();
}
