package se.lexicon.subscriptionapi.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import se.lexicon.subscriptionapi.domain.constant.SubscriptionStatus;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"customer", "plan"})
@Entity
@Table(name = "subscriptions")
public class Subscription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subscription_status")
    @Enumerated(EnumType.STRING)
    private SubscriptionStatus subscriptionStatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, updatable = true)
    private LocalDateTime updatedAt;

    @Column(nullable = true, updatable = true)
    private LocalDateTime cancellationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        subscriptionStatus = SubscriptionStatus.ACTIVE;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}
