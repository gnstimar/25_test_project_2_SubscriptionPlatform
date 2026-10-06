package se.lexicon.subscriptionapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import se.lexicon.subscriptionapi.domain.entity.Plan;
import se.lexicon.subscriptionapi.dto.request.PlanRequest;
import se.lexicon.subscriptionapi.dto.response.PlanResponse;

@Mapper(componentModel = "spring")
public interface PlanMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subscriptions", ignore = true)
    @Mapping(target = "operator", ignore = true)
    Plan toEntity(PlanRequest request);

    @Mapping(source = "operator.id", target = "operatorId")
    @Mapping(source = "operator.name", target = "operatorName")
    PlanResponse toResponse(Plan plan);
}
