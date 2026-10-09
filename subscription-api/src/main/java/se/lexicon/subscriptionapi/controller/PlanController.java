package se.lexicon.subscriptionapi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@Tag(name = "Plans", description = "Plans endpoints")
public class PlanController {

    private final PlanService planService;

    @PostMapping
    @Operation(
            summary = "Create a new plan",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PlanResponse> createPlan(@RequestBody @Valid PlanRequest request) {
        PlanResponse planResponse = planService.createPlan(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(planResponse);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a plan",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PlanResponse> updatePlan(@PathVariable @Positive Long id, @RequestBody @Valid PlanRequest request) {
        PlanResponse planResponse = planService.updatePlan(id, request);
        return ResponseEntity.status(HttpStatus.OK).body(planResponse);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a plan",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Plan deleted successfully (No Content)"),
            @ApiResponse(responseCode = "404", description = "Plan not found")
    })
    public ResponseEntity<Void> deletePlan(@PathVariable @Positive Long id) {
        planService.deletePlan(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    @Operation(
            summary = "Get all plans",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<PlanResponse>> findAll() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllPlans());
    }

    @GetMapping("/actives")
    @Operation(
            summary = "Get all active plans",
            description = "Requires JWT.\n\nRoles: USER, ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PlanResponse>> findAllActive() {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getAllActivePlans());
    }

    @GetMapping("/actives/{serviceType}")
    @Operation(
            summary = "Get active plans based on service type",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<List<PlanResponse>> findAllActiveByServiceType(@PathVariable ServiceType serviceType) {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getActivePlansByServiceType(serviceType));
    }

    @GetMapping("/operator/{operatorId}")
    @Operation(
            summary = "Get plan by operator ID",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<PlanResponse>> findByOperatorId(@PathVariable Long operatorId) {
        return ResponseEntity.status(HttpStatus.OK).body(planService.getPlansByOperator(operatorId));
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get plan by ID",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PlanResponse> getPlanById(@PathVariable Long id) {
        PlanResponse planResponse = planService.getPlanById(id);
        return ResponseEntity.status(HttpStatus.OK).body(planResponse);
    }
}
