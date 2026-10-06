package se.lexicon.subscriptionapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import se.lexicon.subscriptionapi.domain.constant.Role;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.entity.Subscription;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
public class SubscriptionRepositoryTest {

    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Customer customer;
    private Plan internetPlan;

    @BeforeEach
    public void setUp() {
        customer = new Customer();
        customer.setEmail("test@test.com");
        customer.setFirstName("Maria");
        customer.setLastName("Larsson");
        customer.setPassword("password123");
        customer.setRoles(Set.of(Role.ROLE_USER));
        customer = testEntityManager.persistAndFlush(customer);

        Operator testOperator = new Operator();
        testOperator.setName("Bredband SE");
        testEntityManager.persist(testOperator);

        internetPlan = new Plan();
        internetPlan.setName("Fiber 100");
        internetPlan.setPrice(new BigDecimal("110.00"));
        internetPlan.setServiceType(ServiceType.INTERNET);
        internetPlan.setDataLimit(100);
        internetPlan.setActive(true);
        internetPlan.setOperator(testOperator);
        testEntityManager.persist(internetPlan);

        Subscription subscription = new Subscription();
        subscription.setCustomer(customer);
        subscription.setPlan(internetPlan);
        subscription.setSubscriptionStatus(SubscriptionStatus.ACTIVE);
        testEntityManager.persist(subscription);

        testEntityManager.flush();
    }

    @Test
    @DisplayName("Should find subscriptions by Customer Id")
    public void shouldFindByCustomerId() {
        List<Subscription> subscriptionsByCustomerId = subscriptionRepository.findAllByCustomer_Id(customer.getId());

        assertThat(subscriptionsByCustomerId).hasSize(1);
        assertThat(subscriptionsByCustomerId.get(0).getPlan().getName()).isEqualTo("Fiber 100");
    }

    @Test
    @DisplayName("Should find subscriptions by Customer Id and Service type and Subscription Status")
    public void shouldFindByCustomerIdAndPlanServiceTypeAndSubscriptionStatus() {
        Optional<Subscription> subscriptionsByCustomerIdAndServiceTypeAndStatus = subscriptionRepository.findByCustomer_IdAndPlanServiceTypeAndSubscriptionStatus(
                customer.getId(),
                ServiceType.INTERNET,
                SubscriptionStatus.ACTIVE
        );

        assertThat(subscriptionsByCustomerIdAndServiceTypeAndStatus).isPresent();
        assertThat(subscriptionsByCustomerIdAndServiceTypeAndStatus.get().getPlan().getName()).isEqualTo("Fiber 100");
    }

    @Test
    @DisplayName("Should return true when active subscription exist for customer and Service Type")
    public void shouldReturnTrueWhenActiveSubscriptionExists() {
        boolean exists = subscriptionRepository.existsByCustomer_IdAndPlanServiceTypeAndSubscriptionStatus(
                customer.getId(),
                ServiceType.INTERNET,
                SubscriptionStatus.ACTIVE
        );

        assertThat(exists).isTrue();
    }

}
