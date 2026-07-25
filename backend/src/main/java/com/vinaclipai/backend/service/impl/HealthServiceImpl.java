package com.vinaclipai.backend.service.impl;

import com.vinaclipai.backend.dto.response.HealthResponse;
import com.vinaclipai.backend.service.HealthService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HealthServiceImpl implements HealthService {

    private final JdbcTemplate jdbcTemplate;

    public HealthServiceImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public HealthResponse check() {
        try {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            if (result != null && result == 1) {
                return HealthResponse.up();
            }
            return HealthResponse.down();
        } catch (RuntimeException exception) {
            return HealthResponse.down();
        }
    }
}
