package se.lexicon.subscriptionapi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import se.lexicon.subscriptionapi.domain.constant.Role;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.mapper.SubscriptionMapper;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.repository.SubscriptionRepository;
import se.lexicon.subscriptionapi.service.impl.SubscriptionServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SubscriptionServiceTest {

    @Mock
    private SubscriptionRepository subscriptionRepository;

    @Mock
    private SubscriptionMapper subscriptionMapper;

    @Mock
    private PlanRepository planRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private SubscriptionServiceImpl subscriptionService;

    private Operator testOperator;
    private Plan testInternetPlan;
    private Customer testCustomer;
    private SubscriptionRequest subscriptionRequest;
    private SubscriptionResponse subscriptionResponse;
    private Subscription testSubscription;

    @BeforeEach
    void setUp() {
        testOperator = new Operator();
        testOperator.setId(1L);
        testOperator.setName("Breadband SE");

        testInternetPlan = new Plan();
        testInternetPlan.setId(10L);
        testInternetPlan.setName("Fiber 100");
        testInternetPlan.setPrice(new BigDecimal("150.00"));
        testInternetPlan.setServiceType(ServiceType.INTERNET);
        testInternetPlan.setDataLimit(100);
        testInternetPlan.setActive(true);
        testInternetPlan.setOperator(testOperator);

        testCustomer = new Customer();
        testCustomer.setId(2L);
        testCustomer.setEmail("test@test.com");
        testCustomer.setFirstName("Maria");
        testCustomer.setLastName("Larsson");
        testCustomer.setPassword("password123");
        testCustomer.setRoles(Set.of(Role.ROLE_USER));

        subscriptionRequest = new SubscriptionRequest(
                10L,
                2L
        );

        subscriptionResponse = new SubscriptionResponse(
                3L,
                SubscriptionStatus.ACTIVE,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null,
                10L,
                "Fiber 100",
                2L,
                "test@test.com"
        );

        testSubscription = new Subscription();
        testSubscription.setId(4L);
        testSubscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        testSubscription.setCreatedAt(LocalDateTime.now());
        testSubscription.setUpdatedAt(LocalDateTime.now());
        testSubscription.setPlan(testInternetPlan);
        testSubscription.setCustomer(testCustomer);

    }

    @Test
    @DisplayName("Should successfully subscribe")
    void shouldSubscribe() {
        // Arrange
        when(planRepository.findById(10L)).thenReturn(Optional.of(testInternetPlan));
        when(customerRepository.findById(2L)).thenReturn(Optional.of(testCustomer));
        when(subscriptionRepository.existsByCustomer_IdAndPlanServiceTypeAndSubscriptionStatus(
                2L,
                ServiceType.INTERNET,
                SubscriptionStatus.ACTIVE
        )).thenReturn(false);
        when(subscriptionRepository.save(any(Subscription.class))).thenReturn(testSubscription);
        when(subscriptionMapper.toResponse(testSubscription)).thenReturn(subscriptionResponse);

        // Act
        SubscriptionResponse result = subscriptionService.subscribe(subscriptionRequest);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.id()).isEqualTo(3L);
        assertThat(result.planId()).isEqualTo(10L);
        assertThat(result.customerEmail()).isEqualTo("test@test.com");

        verify(planRepository, times(1)).findById(10L);
        verify(subscriptionRepository, times(1)).save(any(Subscription.class));
    }

    // Throw an exception if the Plan is inactive

    // Throw an exception if customer tries to subscribe to the same Service Type

    // ChangePlan

    // Throw an exception if Operator or Service Type is not matching

    // CancelSubscription
}
