package fr.univrouen.deliverysystem.delivery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.univrouen.deliverysystem.driver.DriverEntity;
import fr.univrouen.deliverysystem.driver.DriverRequest;
import fr.univrouen.deliverysystem.driver.DriverResponse;
import fr.univrouen.deliverysystem.round.RoundEntity;
import fr.univrouen.deliverysystem.round.RoundMapper;
import fr.univrouen.deliverysystem.round.RoundMapperImpl;
import fr.univrouen.deliverysystem.round.RoundRequest;
import fr.univrouen.deliverysystem.round.RoundResponse;

@Component
public class DeliveryMapperImpl implements DeliveryMapper, Serializable {

    private static final long serialVersionUID = 1L;
 
    @Override
    public DeliveryEntity toDeliveryEntity(DeliveryRequest deliveryRequest) {
        if (deliveryRequest == null) {
            deliveryRequest = new DeliveryRequest();
        }
        final DeliveryEntity deliveryEntity = new DeliveryEntity();
        deliveryEntity.setPublicId(deliveryRequest.getPublicId());
        deliveryEntity.setPickupAddress(deliveryRequest.getPickupAddress());
        deliveryEntity.setDropoffAddress(deliveryRequest.getDropoffAddress());
        final DeliveryMapperHelper deliveryMapperHelper = new DeliveryMapperHelper();
        deliveryEntity.setRound(deliveryMapperHelper.toRoundEntity(deliveryRequest.getRound()));
        return deliveryEntity;
    }

    @Override
    public DeliveryRequest toDeliveryRequest(DeliveryEntity deliveryEntity) {
        if (deliveryEntity == null) {
            deliveryEntity = new DeliveryEntity();
        }
        final DeliveryRequest deliveryRequest = new DeliveryRequest();
        deliveryRequest.setPublicId(deliveryEntity.getPublicId());
        deliveryRequest.setPickupAddress(deliveryEntity.getPickupAddress());
        deliveryRequest.setDropoffAddress(deliveryEntity.getDropoffAddress());
        DeliveryMapperHelper deliveryMapperHelper = new DeliveryMapperHelper();
        deliveryRequest.setRound(deliveryMapperHelper.toRoundRequest(deliveryEntity.getRound()));
        return deliveryRequest;
    }

    @Override
    public DeliveryResponse toDeliveryResponse(DeliveryEntity deliveryEntity) {
        if (deliveryEntity == null) {
            deliveryEntity = new DeliveryEntity();
        }
        final DeliveryResponse deliveryResponse = new DeliveryResponse();
        deliveryResponse.setPublicId(deliveryEntity.getPublicId());
        deliveryResponse.setPickupAddress(deliveryEntity.getPickupAddress());
        deliveryResponse.setDropoffAddress(deliveryEntity.getDropoffAddress());
        final DeliveryMapperHelper deliveryMapperHelper = new DeliveryMapperHelper();
        deliveryResponse.setRound(deliveryMapperHelper.toRoundResponse(deliveryEntity.getRound()));
        return deliveryResponse;
    }

    @Override
    public List<DeliveryResponse> toDeliveryResponseList(List<DeliveryEntity> deliveryEntities) {
        if (deliveryEntities == null) {
            deliveryEntities = new ArrayList<DeliveryEntity>();
        }
        final List<DeliveryResponse> deliveryResponses = new ArrayList<>();
        for (DeliveryEntity deliveryEntity : deliveryEntities) {
            deliveryResponses.add(toDeliveryResponse(deliveryEntity));
        }
        return deliveryResponses;
    }

    /**
     * Classe interne pour mapper les relations(1-n/n-n) afin d'éviter les cycles
     * Une meilleur option est de créer une classe Base et donc ces filles contenant les attributs de relation hérite de Base
     */
    private class DeliveryMapperHelper {

        public RoundEntity toRoundEntity(RoundRequest roundRequest) {
            if (roundRequest == null) {
                roundRequest = new RoundRequest();
            }
            final RoundEntity roundEntity = new RoundEntity();
            roundEntity.setPublicId(roundRequest.getPublicId());
            roundEntity.setName(roundRequest.getName());
            roundEntity.setStartDate(roundRequest.getStartDate());
            roundEntity.setEndDate(roundRequest.getEndDate());
            return roundEntity;
        }

        public RoundRequest toRoundRequest(RoundEntity roundEntity) {
            if (roundEntity == null) {
                roundEntity = new RoundEntity();
            }
            final RoundRequest roundRequest = new RoundRequest();
            roundRequest.setPublicId(roundEntity.getPublicId());
            roundRequest.setName(roundEntity.getName());
            roundRequest.setStartDate(roundEntity.getStartDate());
            roundRequest.setEndDate(roundEntity.getEndDate());
            return roundRequest;
        }

        public RoundResponse toRoundResponse(RoundEntity roundEntity) {
            if (roundEntity == null) {
                roundEntity = new RoundEntity();
            }
            final RoundResponse roundResponse = new RoundResponse();
            roundResponse.setPublicId(roundEntity.getPublicId());
            roundResponse.setName(roundEntity.getName());
            roundResponse.setStartDate(roundEntity.getStartDate());
            roundResponse.setEndDate(roundEntity.getEndDate());
            return roundResponse;
        }

    }
}
