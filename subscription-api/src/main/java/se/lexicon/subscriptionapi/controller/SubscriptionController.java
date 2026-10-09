package se.lexicon.subscriptionapi.controller;

import io.swagger.v3.oas.annotations.Operation;
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
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
@Validated
@Tag(name = "Subscriptions", description = "Subscriptions endpoints")
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    @Operation(
            summary = "Create a new subscription",
            description = "Requires JWT.\n\nRoles: USER, ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriptionResponse> subscribe(@RequestBody @Valid SubscriptionRequest request) {
        SubscriptionResponse subscriptionResponse = subscriptionService.subscribe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionResponse);
    }

    @GetMapping("/customer/{customerId}")
    @Operation(
            summary = "Get subscription by customer ID",
            description = "Requires JWT.\n\nRoles: ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByCustomer(@PathVariable @Positive Long customerId) {
        List<SubscriptionResponse> responses = subscriptionService.getSubscriptionsByCustomer(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/{subscriptionId}/change-plan/{newPlanId}")
    @Operation(
            summary = "Change a plan",
            description = "Requires JWT.\n\nRoles: USER, ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriptionResponse> changePlan(@PathVariable @Positive Long subscriptionId, @PathVariable @Positive Long newPlanId) {
        SubscriptionResponse subscriptionResponse = subscriptionService.changePlan(subscriptionId, newPlanId);
        return ResponseEntity.status(HttpStatus.OK).body(subscriptionResponse);
    }

    @PutMapping("/{subscriptionId}/cancel")
    @Operation(
            summary = "Cancel a subscription",
            description = "Requires JWT.\n\nRoles: USER, ADMIN",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable Long subscriptionId) {
        SubscriptionResponse subscriptionResponse = subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body(subscriptionResponse);
    }
}
