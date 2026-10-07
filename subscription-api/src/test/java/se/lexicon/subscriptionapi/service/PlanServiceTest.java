package se.lexicon.subscriptionapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.PlanMapper;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.service.impl.PlanServiceImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class PlanServiceTest {

    @Mock
    private PlanRepository planRepository;

    @Mock
    private OperatorRepository operatorRepository;

    @Mock
    private PlanMapper planMapper;

    @InjectMocks
    private PlanServiceImpl planService;

    private Operator testOperator;
    private Plan testPlan;
    private PlanRequest planRequest;
    private PlanResponse planResponse;

    @BeforeEach
    void setUp() {
        testOperator = new Operator();
        testOperator.setId(1L);
        testOperator.setName("Breadband SE");

        testPlan = new Plan();
        testPlan.setId(10L);
        testPlan.setName("Fiber 100");
        testPlan.setPrice(new BigDecimal("150.00"));
        testPlan.setServiceType(ServiceType.INTERNET);
        testPlan.setDataLimit(100);
        testPlan.setActive(true);
        testPlan.setOperator(testOperator);

        planRequest = new PlanRequest(
                "Fiber 100",
                new BigDecimal("150.00"),
                ServiceType.INTERNET,
                100,
                true,
                1L
        );

        planResponse = new PlanResponse(
                10L,
                "Fiber 100",
                new BigDecimal("150.00"),
                ServiceType.INTERNET,
                100,
                true,
                1L,
                "Breadband SE"
        );
    }

    @Test
    @DisplayName("Should create a plan successfully")
    void shouldCreatePlan() {
        // Arrange
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(testOperator));
        when(planMapper.toEntity(planRequest)).thenReturn(testPlan);
        when(planRepository.save(any(Plan.class))).thenReturn(testPlan);
        when(planMapper.toResponse(testPlan)).thenReturn(planResponse);

        // Act
        PlanResponse result = planService.createPlan(planRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(10L);
        assertThat(result.name()).isEqualTo("Fiber 100");
        assertThat(result.operatorId()).isEqualTo(1L);

        verify(operatorRepository, times(1)).findById(1L);
        verify(planRepository, times(1)).save(testPlan);
    }

    @Test
    @DisplayName("Should throw exception when Operator ID is not found when creating")
    void shouldThrowExceptionWhenOperatorIdNotFoundWhenCreating() {
        // Arrange
        when(operatorRepository.findById(199L)).thenReturn(Optional.empty());
        PlanRequest invalidRequest = new PlanRequest(
                "Fiber 100",
                new BigDecimal("100.00"),
                ServiceType.INTERNET,
                100,
                true,
                199L
        );

        // Act

        // Assert
        assertThatThrownBy(()->planService.createPlan(invalidRequest))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Operator not found with ID: 199");

        verify(planRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should update Plan successfully")
    void shouldUpdatePlan() {
        // Arrange
        when(planRepository.findById(10L)).thenReturn(Optional.of(testPlan));
        when(operatorRepository.findById(1L)).thenReturn(Optional.of(testOperator));
        when(planRepository.save(any(Plan.class))).thenReturn(testPlan);
        when(planMapper.toResponse(testPlan)).thenReturn(planResponse);

        // Act
        PlanResponse result = planService.updatePlan(10L, planRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(10L);

        verify(planRepository, times(1)).save(testPlan);
    }

    @Test
    @DisplayName("Should return all active plans")
    void shouldReturnAllActivePlans() {
        // Arrange
        when(planRepository.findByIsActiveTrue()).thenReturn(List.of(testPlan));
        when(planMapper.toResponse(testPlan)).thenReturn(planResponse);

        // Act
        List<PlanResponse> activePlans = planService.getAllActivePlans();

        // Assert
        assertThat(activePlans).hasSize(1);
        assertThat(activePlans.get(0).name()).isEqualTo("Fiber 100");

        verify(planRepository, times(1)).findByIsActiveTrue();
    }
}
