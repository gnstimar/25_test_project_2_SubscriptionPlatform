package se.lexicon.subscriptionapi.controller;

import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.util.List;

@RestController
@RequestMapping("/api/vi/subscriptions")
@RequiredArgsConstructor
@Validated
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> subscribe(@RequestBody SubscriptionRequest request) {
        SubscriptionResponse subscriptionResponse = subscriptionService.subscribe(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(subscriptionResponse);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SubscriptionResponse>> getSubscriptionsByCustomer(@PathVariable @Positive Long customerId) {
        List<SubscriptionResponse> responses = subscriptionService.getSubscriptionsByCustomer(customerId);
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @PutMapping("/{subscriptionId}/change-plan/{newPlanId}")
    public ResponseEntity<SubscriptionResponse> changePlan(@PathVariable @Positive Long subscriptionId, @PathVariable @Positive Long newPlanId) {
        SubscriptionResponse subscriptionResponse = subscriptionService.changePlan(subscriptionId, newPlanId);
        return ResponseEntity.status(HttpStatus.OK).body(subscriptionResponse);
    }

    @PutMapping("/{subscriptionId}/cancel")
    public ResponseEntity<SubscriptionResponse> cancelSubscription(@PathVariable Long subscriptionId) {
        SubscriptionResponse subscriptionResponse = subscriptionService.cancelSubscription(subscriptionId);
        return ResponseEntity.status(HttpStatus.OK).body(subscriptionResponse);
    }
}
