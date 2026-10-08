package se.lexicon.subscriptionapi.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class PlanMapperTest {

    private final PlanMapper planMapper = Mappers.getMapper(PlanMapper.class);

    @Test
    @DisplayName("Should map Plan entity to PlanResponse DTO")
    void shouldMapPlanEntityToPlanResponse() {
        // Arrange
        Operator testOperator = new Operator();
        testOperator.setId(1L);
        testOperator.setName("Breadband SE");

        Plan testPlan = new Plan();
        testPlan = new Plan();
        testPlan.setId(10L);
        testPlan.setName("Fiber 100");
        testPlan.setPrice(new BigDecimal("150.00"));
        testPlan.setServiceType(ServiceType.INTERNET);
        testPlan.setDataLimit(100);
        testPlan.setActive(true);
        testPlan.setOperator(testOperator);

        // Act
        PlanResponse response = planMapper.toResponse(testPlan);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.name()).isEqualTo("Fiber 100");
        assertThat(response.price()).isEqualTo(new BigDecimal("150.00"));
        assertThat(response.serviceType()).isEqualTo(ServiceType.INTERNET);
        assertThat(response.dataLimit()).isEqualTo(100);
        assertThat(response.isActive()).isTrue();
        assertThat(response.operatorId()).isEqualTo(1L);
        assertThat(response.operatorName()).isEqualTo("Breadband SE");
    }

    @Test
    @DisplayName("Should map PlanRequest DTO to Plan entity")
    void shouldMapPlanRequestToPlanEntity() {
        // Arrange
        PlanRequest request = new PlanRequest(
                "Fiber 100",
                new BigDecimal("100.00"),
                ServiceType.MOBILE,
                100,
                true,
                1L
        );

        // Act
        Plan plan = planMapper.toEntity(request);

        // Assert
        assertThat(plan).isNotNull();
        assertThat(plan.getName()).isEqualTo("Fiber 100");
        assertThat(plan.getPrice()).isEqualTo(new BigDecimal("100.00"));
        assertThat(plan.getServiceType()).isEqualTo(ServiceType.MOBILE);
        assertThat(plan.getDataLimit()).isEqualTo(100);
        assertThat(plan.isActive()).isTrue();
        assertThat(plan.getOperator()).isNull(); // Operator should be null, because we set it in Service layer
    }
}
