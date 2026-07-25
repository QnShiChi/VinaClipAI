package com.vinaclipai.backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.vinaclipai.backend.dto.response.HealthResponse;
import com.vinaclipai.backend.service.HealthService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(HealthController.class)
class HealthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HealthService healthService;

    @Test
    void getHealthReturnsOkWhenServiceIsUp() throws Exception {
        when(healthService.check()).thenReturn(HealthResponse.up());

        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("UP"))
            .andExpect(jsonPath("$.service").value("core-backend"))
            .andExpect(jsonPath("$.database").value("UP"));
    }

    @Test
    void getHealthReturnsServiceUnavailableWhenServiceIsDown() throws Exception {
        when(healthService.check()).thenReturn(HealthResponse.down());

        mockMvc.perform(get("/api/health"))
            .andExpect(status().isServiceUnavailable())
            .andExpect(jsonPath("$.status").value("DOWN"))
            .andExpect(jsonPath("$.database").value("DOWN"));
    }
}
