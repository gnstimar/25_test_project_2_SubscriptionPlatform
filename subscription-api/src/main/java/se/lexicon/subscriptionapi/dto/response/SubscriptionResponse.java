package se.lexicon.subscriptionapi.dto.response;

import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;

import java.time.LocalDateTime;

public record SubscriptionResponse(
        Long id,
        SubscriptionStatus subscriptionStatus,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime cancellationDate,
        Long planId,
        String planName,
        Long customerId,
        String customerEmail
) {
}
