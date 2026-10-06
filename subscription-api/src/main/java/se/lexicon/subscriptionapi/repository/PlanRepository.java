package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;

import java.util.List;

@Repository
public interface PlanRepository extends JpaRepository<Plan, Long> {
    // List active plans
    List<Plan> findByIsActiveTrue();

    // Find active plans based on their service type
    List<Plan> findByIsActiveTrueAndServiceType(ServiceType serviceType);

    // Find plans based on their operator id
    List<Plan> findByOperator_Id(Long operatorId);

    // Find active plans based on operator id
    List<Plan> findByOperator_IdAndIsActiveTrue(Long operatorId);
}
