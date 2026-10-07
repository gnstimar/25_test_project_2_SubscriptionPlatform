package se.lexicon.subscriptionapi.service;

import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;

import java.util.List;

public interface SubscriptionService {
    SubscriptionResponse subscribe(SubscriptionRequest request);

    List<SubscriptionResponse> getSubscriptionsByCustomer(Long customerId);

    SubscriptionResponse changePlan(Long subscriptionId, Long newPlanId);

    SubscriptionResponse cancelSubscription(Long subscriptionId);
}
