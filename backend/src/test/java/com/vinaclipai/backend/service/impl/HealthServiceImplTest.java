package com.vinaclipai.backend.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.vinaclipai.backend.dto.response.HealthResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.jdbc.core.JdbcTemplate;

class HealthServiceImplTest {

    @Test
    void checkReturnsUpWhenDatabaseResponds() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(1);
        HealthServiceImpl service = new HealthServiceImpl(jdbcTemplate);

        HealthResponse response = service.check();

        assertThat(response.status()).isEqualTo("UP");
        assertThat(response.service()).isEqualTo("core-backend");
        assertThat(response.database()).isEqualTo("UP");
    }

    @Test
    void checkReturnsDownWhenDatabaseResponseIsUnexpected() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class)).thenReturn(0);
        HealthServiceImpl service = new HealthServiceImpl(jdbcTemplate);

        HealthResponse response = service.check();

        assertThat(response.status()).isEqualTo("DOWN");
        assertThat(response.database()).isEqualTo("DOWN");
    }

    @Test
    void checkReturnsDownWhenDatabaseIsUnavailable() {
        JdbcTemplate jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        when(jdbcTemplate.queryForObject("SELECT 1", Integer.class))
                .thenThrow(new DataAccessResourceFailureException("database unavailable"));
        HealthServiceImpl service = new HealthServiceImpl(jdbcTemplate);

        HealthResponse response = service.check();

        assertThat(response.status()).isEqualTo("DOWN");
        assertThat(response.database()).isEqualTo("DOWN");
    }
}
