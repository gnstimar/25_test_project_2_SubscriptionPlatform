package se.lexicon.subscriptionapi.service;

import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;

import java.util.List;

public interface PlanService {
    // ADMIN
    PlanResponse createPlan(PlanRequest request);
    PlanResponse updatePlan(Long id, PlanRequest request);
    void deletePlan(Long id);
    List<PlanResponse> getAllPlans();

    // CUSTOMER
    List<PlanResponse> getAllActivePlans();
    List<PlanResponse> getActivePlansByServiceType(ServiceType serviceType);
    List<PlanResponse> getPlansByOperator(Long operatorId);
    PlanResponse getPlanById(Long id);
}
