package se.lexicon.subscriptionapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.entity.Operator;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.PlanMapper;
import se.lexicon.subscriptionapi.repository.OperatorRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.service.PlanService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlanServiceImpl implements PlanService {

    private final PlanRepository planRepository;
    private final PlanMapper planMapper;
    private final OperatorRepository operatorRepository;

    @Override
    @Transactional
    public PlanResponse createPlan(PlanRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Plan request cannot be null.");
        }

        Operator operator = operatorRepository.findById(request.operatorId()).orElseThrow(() -> new ResourceNotFoundException("Operator not found with ID: " + request.operatorId()));

        Plan plan = planMapper.toEntity(request);
        plan.setOperator(operator);

        Plan savedPlan = planRepository.save(plan);
        return planMapper.toResponse(savedPlan);
    }

    @Override
    @Transactional
    public PlanResponse updatePlan(Long id, PlanRequest request) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }
        if (request == null) {
            throw new IllegalArgumentException("Plan Request cannot be null for update.");
        }

        Plan plan = planRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Plan is not found with ID: " + id));
        plan.setName(request.name());
        plan.setPrice(request.price());
        plan.setServiceType(request.serviceType());
        plan.setDataLimit(request.dataLimit());
        plan.setActive(request.active());
        Operator operator = operatorRepository.findById(request.operatorId()).orElseThrow(() -> new ResourceNotFoundException("operator not found with ID: " + request.operatorId()));
        plan.setOperator(operator);

        Plan updatedPlan = planRepository.save(plan);
        return planMapper.toResponse(updatedPlan);
    }

    @Override
    @Transactional
    public void deletePlan(Long id) {
        if (!planRepository.existsById(id)) {
            throw new ResourceNotFoundException("Plan not found with ID: " + id);
        }
        planRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> getAllPlans() {
        return planRepository.findAll()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> getAllActivePlans() {
        return planRepository.findByIsActiveTrue()
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> getActivePlansByServiceType(ServiceType serviceType) {
        if (serviceType == null) {
            throw new IllegalArgumentException("Service Type cannot be empty.");
        }
        return planRepository.findByIsActiveTrueAndServiceType(serviceType)
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<PlanResponse> getPlansByOperator(Long operatorId) {
        if (operatorId == null) {
            throw new IllegalArgumentException("Operator ID cannot be empty.");
        }

        return planRepository.findByOperator_Id(operatorId)
                .stream()
                .map(planMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PlanResponse getPlanById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be empty.");
        }

        return planRepository.findById(id).map(planMapper::toResponse).orElseThrow(() -> new ResourceNotFoundException("Plan not found with ID: " + id));
    }
}
