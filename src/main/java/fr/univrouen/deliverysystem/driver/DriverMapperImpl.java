package fr.univrouen.deliverysystem.driver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.univrouen.deliverysystem.delivery.DeliveryEntity;
import fr.univrouen.deliverysystem.delivery.DeliveryResponse;
import fr.univrouen.deliverysystem.round.RoundEntity;
import fr.univrouen.deliverysystem.round.RoundMapper;
import fr.univrouen.deliverysystem.round.RoundMapperImpl;
import fr.univrouen.deliverysystem.round.RoundRequest;
import fr.univrouen.deliverysystem.round.RoundResponse;

@Component
public class DriverMapperImpl implements DriverMapper, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public DriverEntity toDriverEntity(DriverRequest driverRequest) {
        if (driverRequest == null) {
            driverRequest = new DriverRequest();
        }
        final DriverEntity driverEntity = new DriverEntity();
        driverEntity.setPublicId(driverRequest.getPublicId());
        driverEntity.setName(driverRequest.getName());
        driverEntity.setAvailable(driverRequest.isAvailable());
        driverEntity.setUpdatedAt(driverRequest.getUpdatedAt());
        final DriverMapperHelper driverMapperHelper = new DriverMapperHelper();
        final List<RoundEntity> rounds = new ArrayList<RoundEntity>();
        for (RoundRequest round : driverRequest.getRounds()) {
            rounds.add(driverMapperHelper.toRoundEntity(round));
        }
        driverEntity.setRounds(rounds);
        return driverEntity;
    }

    @Override
    public DriverRequest toDriverRequest(DriverEntity driverEntity) {
        if (driverEntity == null) {
            driverEntity = new DriverEntity();
        }
        final DriverRequest driverRequest = new DriverRequest();
        driverRequest.setPublicId(driverEntity.getPublicId());
        driverRequest.setName(driverEntity.getName());
        driverRequest.setAvailable(driverEntity.isAvailable());
        driverRequest.setCreatedAt(driverEntity.getCreatedAt());
        driverRequest.setUpdatedAt(driverEntity.getUpdatedAt());
        final List<RoundRequest> rounds = new ArrayList<RoundRequest>();
        final DriverMapperHelper driverMapperHelper = new DriverMapperHelper();
        for (RoundEntity round : driverEntity.getRounds()) {
            rounds.add(driverMapperHelper.toRoundRequest(round));
        }
        driverRequest.setRounds(rounds);
        return driverRequest;
    }

    @Override
    public DriverResponse toDriverResponse(DriverEntity driverEntity) {
        if (driverEntity == null) {
            driverEntity = new DriverEntity();
        }
        final DriverResponse driverResponse = new DriverResponse();
        driverResponse.setPublicId(driverEntity.getPublicId());
        driverResponse.setName(driverEntity.getName());
        driverResponse.setAvailable(driverEntity.isAvailable());
        driverResponse.setCreatedAt(driverEntity.getCreatedAt());
        driverResponse.setUpdatedAt(driverEntity.getUpdatedAt());
        final List<RoundResponse> rounds = new ArrayList<RoundResponse>();
        final DriverMapperHelper driverMapperHelper = new DriverMapperHelper();
        for (RoundEntity round : driverEntity.getRounds()) {
            rounds.add(driverMapperHelper.toRoundResponse(round));
        }
        driverResponse.setRounds(rounds);
        return driverResponse;
    }

    @Override
    public List<DriverResponse> toDriverResponseList(List<DriverEntity> driverEntities) {
        if (driverEntities == null) {
            driverEntities = new ArrayList<DriverEntity>();
        }
        final List<DriverResponse> driverResponses = new ArrayList<>();
        for (DriverEntity driverEntity : driverEntities) {
            driverResponses.add(toDriverResponse(driverEntity));
        }
        return driverResponses;
    }

    /**
     * Classe interne pour mapper les relations(1-n/n-n) afin d'éviter les cycles
     * Une meilleur option est de créer une classe Base et donc ces filles contenant
     * les attributs de relation hérite de Base
     */
    private class DriverMapperHelper {

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
            List<DeliveryResponse> deliveriesResponses = new ArrayList<DeliveryResponse>();
            for (DeliveryEntity deliveryEntity : roundEntity.getDeliveries()) {
                deliveriesResponses.add(this.toDeliveryResponse(deliveryEntity));
            }
            roundResponse.setDeliveries(deliveriesResponses);
            return roundResponse;
        }

        public DeliveryResponse toDeliveryResponse(DeliveryEntity deliveryEntity) {
            if (deliveryEntity == null) {
                deliveryEntity = new DeliveryEntity();
            }
            final DeliveryResponse deliveryResponse = new DeliveryResponse();
            deliveryResponse.setPublicId(deliveryEntity.getPublicId());
            deliveryResponse.setPickupAddress(deliveryEntity.getPickupAddress());
            deliveryResponse.setDropoffAddress(deliveryEntity.getDropoffAddress());
            return deliveryResponse;
        }

    }
}
