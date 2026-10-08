package se.lexicon.subscriptionapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.security.JwtAuthenticationFilter;
import se.lexicon.subscriptionapi.security.JwtTokenProvider;
import se.lexicon.subscriptionapi.service.PlanService;
import tools.jackson.databind.ObjectMapper;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

@WebMvcTest(PlanController.class)
@AutoConfigureMockMvc(addFilters = false) // turns off Security
public class PlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PlanService planService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private PlanRequest planRequest;
    private PlanResponse planResponse;

    @BeforeEach
    void setUp() {
        planRequest = new PlanRequest(
                "Fiber 100",
                new BigDecimal("199.00"),
                ServiceType.INTERNET,
                100,
                true,
                1L
        );

        planResponse = new PlanResponse(
                10L,
                "Fiber 100",
                new BigDecimal("199.00"),
                ServiceType.INTERNET,
                100,
                true,
                1L,
                "Breadband SE"
        );
    }

    @Test
    @DisplayName("POST api/v1/plans - Should create plan and return 201 CREATED")
    void shouldCreatePlan() throws Exception{
        when(planService.createPlan(any(PlanRequest.class))).thenReturn(planResponse);

        mockMvc.perform(post("/api/v1/plans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(planRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10L))
                .andExpect(jsonPath("$.name").value("Fiber 100"))
                .andExpect(jsonPath("$.operatorName").value("Breadband SE"));

        verify(planService, times(1)).createPlan(any(PlanRequest.class));
    }


}
