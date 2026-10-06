package se.lexicon.subscriptionapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;
import se.lexicon.subscriptionapi.domain.entity.Subscription;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    List<Subscription> findAllByCustomer_Id(Long customerId);

    Optional<Subscription> findByCustomer_IdAndPlanServiceTypeAndSubscriptionStatus(
            Long customerId,
            ServiceType serviceType,
            SubscriptionStatus subscriptionStatus
    );

    boolean existsByCustomer_IdAndPlanServiceTypeAndSubscriptionStatus(
            Long customerId,
            ServiceType serviceType,
            SubscriptionStatus subscriptionStatus
    );

}
