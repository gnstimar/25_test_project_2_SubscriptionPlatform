package se.lexicon.subscriptionapi.dto.request;

import jakarta.validation.constraints.NotNull;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;

public record SubscriptionRequest(
        @NotNull(message = "Plan ID is required.")
        Long planId,

        @NotNull(message = "Customer ID is required.")
        Long customerId
) {
}
