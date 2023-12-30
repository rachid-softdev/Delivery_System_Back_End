package fr.univrouen.deliverysystem.round;

import java.util.List;

public interface RoundMapper {

	RoundEntity toRoundEntity(RoundRequest roundRequest);

	RoundRequest toRoundRequest(RoundEntity roundEntity);

	RoundResponse toRoundResponse(RoundEntity roundEntity);

	List<RoundResponse> toRoundResponseList(List<RoundEntity> roundEntities);

}
