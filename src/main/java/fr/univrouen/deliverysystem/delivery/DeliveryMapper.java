package fr.univrouen.deliverysystem.delivery;

import java.util.List;

public interface DeliveryMapper {

	DeliveryEntity toDeliveryEntity(DeliveryRequest deliveryRequest);

	DeliveryRequest toDeliveryRequest(DeliveryEntity deliveryEntity);

	DeliveryResponse toDeliveryResponse(DeliveryEntity deliveryEntity);

	List<DeliveryResponse> toDeliveryResponseList(List<DeliveryEntity> deliveryEntities);

}
