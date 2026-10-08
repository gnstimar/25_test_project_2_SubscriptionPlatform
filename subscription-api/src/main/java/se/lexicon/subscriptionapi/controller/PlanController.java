package se.lexicon.subscriptionapi.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;
import se.lexicon.subscriptionapi.service.PlanService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/plans")
@RequiredArgsConstructor
@Validated
public class PlanController {

    private final PlanService planService;

    @PostMapping
    public ResponseEntity<PlanResponse> createPlan(@RequestBody @Valid PlanRequest request) {
        PlanResponse planResponse = planService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(planResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable @Positive Long id, @RequestBody @Valid PlanRequest request) {
        PlanResponse planResponse = planService.updatePlan(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(planResponse);
    }

    @DeleteMapping("/{id}")
    public void deletePlan(@PathVariable @Positive Long id) {
        planService.deletePlan(id);
    }

    @GetMapping
    public ResponseEntity<List<PlanResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllPlans());
    }

    @GetMapping("/actives")
    public ResponseEntity<List<PlanResponse>> findAllActive() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllActivePlans());
    }

    @GetMapping("/actives/{serviceType}")
    public ResponseEntity<List<PlanResponse>> findAllActiveByServiceType(@PathVariable ServiceType serviceType) {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getActivePlansByServiceType(serviceType));
    }

    @GetMapping("/{operatorId}")
    public ResponseEntity<List<PlanResponse>> findByOperatorId(@PathVariable Long operatorId) {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlansByOperator(operatorId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long id) {
        PlanResponse planResponse = planService.getPlanById(id);
        return ResponseEntity.status(HttpStatus.OK).body(planResponse);
    }
}
