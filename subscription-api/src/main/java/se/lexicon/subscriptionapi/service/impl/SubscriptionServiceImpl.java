package se.lexicon.subscriptionapi.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.domain.entity.Customer;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.domain.entity.Subscription;
import se.lexicon.subscriptionapi.dto.request.SubscriptionRequest;
import se.lexicon.subscriptionapi.dto.response.SubscriptionResponse;
import se.lexicon.subscriptionapi.exception.BusinessRuleException;
import se.lexicon.subscriptionapi.exception.ResourceNotFoundException;
import se.lexicon.subscriptionapi.mapper.SubscriptionMapper;
import se.lexicon.subscriptionapi.repository.CustomerRepository;
import se.lexicon.subscriptionapi.repository.PlanRepository;
import se.lexicon.subscriptionapi.repository.SubscriptionRepository;
import se.lexicon.subscriptionapi.service.SubscriptionService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final PlanRepository planRepository;
    private final CustomerRepository customerRepository;

    @Override
    @Transactional
    public SubscriptionResponse subscribe(SubscriptionRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Subscription request cannot be null.");
        }

        Subscription subscription = new Subscription();
        Plan plan = planRepository.findById(request.planId()).orElseThrow(() -> new ResourceNotFoundException("Plan is not found with ID: " + request.planId()));

        if (!plan.isActive()) {
            throw new BusinessRuleException("Cannot subscribe for inactive Plan.");
        }

        Customer customer = customerRepository.findById(request.customerId()).orElseThrow(() -> new ResourceNotFoundException("Customer is not found with ID: " + request.customerId()));

        if (subscriptionRepository.existsByCustomer_IdAndPlanServiceTypeAndSubscriptionStatus(
                request.customerId(),
                plan.getServiceType(),
                SubscriptionStatus.ACTIVE
        )) {
            throw new BusinessRuleException("A customer may have at most one active subscription per service type.");
        }

        subscription.setPlan(plan);
        subscription.setCustomer(customer);

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(savedSubscription);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionResponse> getSubscriptionsByCustomer(Long customerId) {
        if (customerId == null) {
            throw new IllegalArgumentException("Customer ID cannot be null.");
        }
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with ID: " + customerId);
        }
        return subscriptionRepository.findAllByCustomer_Id(customerId)
                .stream()
                .map(subscriptionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public SubscriptionResponse changePlan(Long subscriptionId, Long newPlanId) {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null.");
        }
        if (newPlanId == null) {
            throw new IllegalArgumentException("New Plan ID cannot be null.");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription is not found with ID: " + subscriptionId));

        if (subscription.getSubscriptionStatus() != SubscriptionStatus.ACTIVE) {
            throw new BusinessRuleException("Plan change is only allowed for ACTIVE subscriptions.");
        }

        Plan newPlan = planRepository.findById(newPlanId).orElseThrow(() -> new ResourceNotFoundException("New Plan is not found with ID: " + newPlanId));
        if (!newPlan.isActive()) {
            throw new BusinessRuleException("Cannot change to an inactive Plan.");
        }

        if (!subscription.getPlan().getOperator().getId().equals(newPlan.getOperator().getId())) {
            throw new BusinessRuleException("Cannot have different operator when want to change Plan.");
        }
        if (!subscription.getPlan().getServiceType().equals(newPlan.getServiceType())) {
            throw new BusinessRuleException("Plan change is only allowed within the same service type.");
        }

        subscription.setPlan(newPlan);
        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(savedSubscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse cancelSubscription(Long subscriptionId) {
        if (subscriptionId == null) {
            throw new IllegalArgumentException("Subscription ID cannot be null.");
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new ResourceNotFoundException("Subscription is not found with ID: " + subscriptionId));

        if (subscription.getSubscriptionStatus() == SubscriptionStatus.CANCELLED) {
            throw new BusinessRuleException("Subscription is already cancelled.");
        }

        subscription.setSubscriptionStatus(SubscriptionStatus.CANCELLED);
        subscription.setCancellationDate(LocalDateTime.now());

        Subscription savedSubscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toResponse(savedSubscription);
    }
}
