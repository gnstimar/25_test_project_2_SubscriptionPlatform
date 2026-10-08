package se.lexicon.subscriptionapi.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.security.JwtAuthenticationFilter;
import se.lexicon.subscriptionapi.security.JwtTokenProvider;
import se.lexicon.subscriptionapi.service.SubscriptionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.time.LocalDateTime;

@WebMvcTest(SubscriptionController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SubscriptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private SubscriptionService subscriptionService;

    @MockitoBean
    private JwtTokenProvider jwtTokenProvider;

    @MockitoBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    private SubscriptionRequest subscriptionRequest;
    private SubscriptionResponse subscriptionResponse;

    @BeforeEach
    void setUp() {
        subscriptionRequest = new SubscriptionRequest(100L, 1L);

        subscriptionResponse = new SubscriptionResponse(
                1000L,
                SubscriptionStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                1L,
                "Fiber 100",
                100L,
                "test@test.com"
        );
    }

    @Test
    @DisplayName("POST /api/v1/subscriptions - Should subscribe successfully and return 201 CREATED")
    void shouldSubscribe() throws Exception {
        when(subscriptionService.subscribe(any(SubscriptionRequest.class))).thenReturn(subscriptionResponse);

        mockMvc.perform(post("/api/v1/subscriptions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subscriptionRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1000L))
                .andExpect(jsonPath("$.customerEmail").value("test@test.com"))
                .andExpect(jsonPath("$.planName").value("Fiber 100"));

        verify(subscriptionService, times(1)).subscribe(any(SubscriptionRequest.class));
    }
}
