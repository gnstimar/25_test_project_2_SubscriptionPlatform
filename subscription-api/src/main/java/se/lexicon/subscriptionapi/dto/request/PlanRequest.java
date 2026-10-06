package se.lexicon.subscriptionapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import se.lexicon.subscriptionapi.domain.constant.ServiceType;

import java.math.BigDecimal;

public record PlanRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
        String name,

        @NotNull(message = "Price is required.")
        @Positive(message = "Price must be positive.")
        BigDecimal price,

        @NotNull(message = "Service type is required")
        ServiceType serviceType,

        Integer dataLimit,

        boolean active,

        @NotNull(message = "Operator ID is required.")
        Long operatorId
) {
}
