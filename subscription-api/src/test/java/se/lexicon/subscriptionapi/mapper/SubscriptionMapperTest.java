package se.lexicon.subscriptionapi.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import se.lexicon.subscriptionapi.domain.constant.Role;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;

import java.math.BigDecimal;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class SubscriptionMapperTest {

    private final SubscriptionMapper subscriptionMapper = Mappers.getMapper(SubscriptionMapper.class);

    @Test
    @DisplayName("Should map Subscription entity to SubscriptionResponse DTO")
    void shouldMapSubscriptionToSubscriptionResponse() {
        // Arrange
        Customer customer = new Customer();
        customer.setId(5L);
        customer.setEmail("test@test.com");
        customer.setFirstName("Maria");
        customer.setLastName("Larsson");
        customer.setPassword("password123");
        customer.setRoles(Set.of(Role.ROLE_USER));

        Operator testOperator = new Operator();
        testOperator.setName("Bredband SE");

        Plan internetPlan = new Plan();
        internetPlan.setId(20L);
        internetPlan.setName("Fiber 300");
        internetPlan.setPrice(new BigDecimal("110.00"));
        internetPlan.setServiceType(ServiceType.INTERNET);
        internetPlan.setDataLimit(100);
        internetPlan.setActive(true);
        internetPlan.setOperator(testOperator);

        Subscription subscription = new Subscription();
        subscription.setId(100L);
        subscription.setCustomer(customer);
        subscription.setPlan(internetPlan);
        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);

        // Act
        SubscriptionResponse response = subscriptionMapper.toResponse(subscription);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(100L);
        assertThat(response.subscriptionStatus()).isEqualTo(SubscriptionStatus.ACTIVE);
        assertThat(response.customerId()).isEqualTo(5L);
        assertThat(response.customerEmail()).isEqualTo("test@test.com");
        assertThat(response.planId()).isEqualTo(20L);
        assertThat(response.planName()).isEqualTo("Fiber 300");
    }
}
