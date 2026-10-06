package se.lexicon.subscriptionapi.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
public class PlanRepositoryTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private Operator testOperator;

    @BeforeEach
    void setUp() {
        testOperator = new Operator();
        testOperator.setName("Bredband SE");
        testOperator = testEntityManager.persistAndFlush(testOperator);

        // Active Internet
        Plan activeInternetPlan = new Plan();
        activeInternetPlan.setName("Fiber 100");
        activeInternetPlan.setPrice(new BigDecimal("110.00"));
        activeInternetPlan.setServiceType(ServiceType.INTERNET);
        activeInternetPlan.setDataLimit(100);
        activeInternetPlan.setActive(true);
        activeInternetPlan.setOperator(testOperator);
        testEntityManager.persist(activeInternetPlan);

        // Inactive Internet
        Plan inactiveInternetPlan = new Plan();
        inactiveInternetPlan.setName("Fiber 100 Inactive");
        inactiveInternetPlan.setPrice(new BigDecimal("50"));
        inactiveInternetPlan.setServiceType(ServiceType.INTERNET);
        inactiveInternetPlan.setDataLimit(null);
        inactiveInternetPlan.setActive(false);
        inactiveInternetPlan.setOperator(testOperator);
        testEntityManager.persist(inactiveInternetPlan);

        // Active Mobile
        Plan activeMobilePlan = new Plan();
        activeMobilePlan.setName("Mobile Basic");
        activeMobilePlan.setPrice(new BigDecimal("90.00"));
        activeMobilePlan.setServiceType(ServiceType.MOBILE);
        activeMobilePlan.setDataLimit(5);
        activeMobilePlan.setActive(true);
        activeMobilePlan.setOperator(testOperator);
        testEntityManager.persist(activeMobilePlan);

        testEntityManager.flush();
    }

    @Test
    @DisplayName("Should find isActive = true Plans")
    public void shouldFindActivePlans() {
        List<Plan> activePlans = planRepository.findByIsActiveTrue();

        assertThat(activePlans).hasSize(2);
        assertThat(activePlans).extracting(Plan::getName).containsExactlyInAnyOrder(
                "Fiber 100",
                "Mobile Basic"
        );
    }

    @Test
    @DisplayName("Should find active plans by ServiceType")
    public void shouldFindByActiveAndServiceType() {
        List<Plan> internetPlans = planRepository.findByIsActiveTrueAndServiceType(ServiceType.INTERNET);

        assertThat(internetPlans).hasSize(1);
        assertThat(internetPlans.get(0).getName()).isEqualTo("Fiber 100");
    }

    @Test
    @DisplayName("Should find plans by Operator Id")
    public void shouldFindByOperatorId() {
        List<Plan> plansByOperatorId = planRepository.findByOperator_Id(testOperator.getId());

        assertThat(plansByOperatorId).hasSize(3);
    }

    @Test
    @DisplayName("Should find active plans by Operator Id")
    public void shouldFindByActiveAndOperatorId() {
        List<Plan> activeAndOperatorIdPlans = planRepository.findByOperator_IdAndIsActiveTrue(testOperator.getId());

        assertThat(activeAndOperatorIdPlans).hasSize(2);
        assertThat(activeAndOperatorIdPlans).extracting(Plan::getName).containsExactlyInAnyOrder(
                "Fiber 100",
                "Mobile Basic"
        );
    }


}
