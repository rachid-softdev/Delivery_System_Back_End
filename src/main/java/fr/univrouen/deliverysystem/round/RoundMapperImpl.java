package fr.univrouen.deliverysystem.round;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.univrouen.deliverysystem.delivery.DeliveryEntity;
import fr.univrouen.deliverysystem.delivery.DeliveryMapper;
import fr.univrouen.deliverysystem.delivery.DeliveryMapperImpl;
import fr.univrouen.deliverysystem.delivery.DeliveryRequest;
import fr.univrouen.deliverysystem.delivery.DeliveryResponse;
import fr.univrouen.deliverysystem.driver.DriverEntity;
import fr.univrouen.deliverysystem.driver.DriverMapper;
import fr.univrouen.deliverysystem.driver.DriverMapperImpl;
import fr.univrouen.deliverysystem.driver.DriverRequest;
import fr.univrouen.deliverysystem.driver.DriverResponse;

@Component
public class RoundMapperImpl implements RoundMapper, Serializable {

    private static final long serialVersionUID = 1L;

    @Override
    public RoundEntity toRoundEntity(RoundRequest roundRequest) {
        if (roundRequest == null) {
            roundRequest = new RoundRequest();
        }
        final RoundEntity roundEntity = new RoundEntity();
        roundEntity.setPublicId(roundRequest.getPublicId());
        roundEntity.setName(roundRequest.getName());
        roundEntity.setStartDate(roundRequest.getStartDate());
        roundEntity.setEndDate(roundRequest.getEndDate());
        final RoundMapperHelper roundMapperHelper = new RoundMapperHelper();
        roundEntity.setDriver(roundMapperHelper.toDriverEntity(roundRequest.getDriver()));
        final List<DeliveryEntity> deliveries = new ArrayList<DeliveryEntity>();
        for (DeliveryResponse delivery : roundRequest.getDeliveries()) {
            deliveries.add(roundMapperHelper.toDeliveryEntity(delivery));
        }
        roundEntity.setDeliveries(deliveries);
        return roundEntity;
    }

    @Override
    public RoundRequest toRoundRequest(RoundEntity roundEntity) {
        if (roundEntity == null) {
            roundEntity = new RoundEntity();
        }
        final RoundRequest roundRequest = new RoundRequest();
        roundRequest.setPublicId(roundEntity.getPublicId());
        roundRequest.setName(roundEntity.getName());
        roundRequest.setStartDate(roundEntity.getStartDate());
        roundRequest.setEndDate(roundEntity.getEndDate());
        final RoundMapperHelper roundMapperHelper = new RoundMapperHelper();
        roundRequest.setDriver(roundMapperHelper.toDriverResponse(roundEntity.getDriver()));
        final List<DeliveryResponse> deliveries = new ArrayList<DeliveryResponse>();
        for (DeliveryEntity delivery : roundEntity.getDeliveries()) {
            deliveries.add(roundMapperHelper.toDeliveryResponse(delivery));
        }
        roundRequest.setDeliveries(deliveries);
        return roundRequest;
    }

    @Override
    public RoundResponse toRoundResponse(RoundEntity roundEntity) {
        if (roundEntity == null) {
            roundEntity = new RoundEntity();
        }
        final RoundResponse roundResponse = new RoundResponse();
        roundResponse.setPublicId(roundEntity.getPublicId());
        roundResponse.setName(roundEntity.getName());
        roundResponse.setStartDate(roundEntity.getStartDate());
        roundResponse.setEndDate(roundEntity.getEndDate());
        final RoundMapperHelper roundMapperHelper = new RoundMapperHelper();
        roundResponse.setDriver(roundMapperHelper.toDriverResponse(roundEntity.getDriver()));
        final List<DeliveryResponse> deliveries = new ArrayList<DeliveryResponse>();
        for (DeliveryEntity delivery : roundEntity.getDeliveries()) {
            deliveries.add(roundMapperHelper.toDeliveryResponse(delivery));
        }
        roundResponse.setDeliveries(deliveries);
        return roundResponse;
    }

    @Override
    public List<RoundResponse> toRoundResponseList(List<RoundEntity> roundEntities) {
        if (roundEntities == null) {
            roundEntities = new ArrayList<RoundEntity>();
        }
        final List<RoundResponse> roundResponses = new ArrayList<>();
        for (RoundEntity roundEntity : roundEntities) {
            roundResponses.add(toRoundResponse(roundEntity));
        }
        return roundResponses;
    }

    /**
     * Classe interne pour mapper les relations(1-n/n-n) afin d'éviter les cycles
     * Une meilleur option est de créer une classe Base et donc ces filles contenant
     * les attributs de relation hérite de Base
     */
    private class RoundMapperHelper {

        public DriverEntity toDriverEntity(DriverRequest driverRequest) {
            if (driverRequest == null) {
                return null;
            }
            final DriverEntity driverEntity = new DriverEntity();
            driverEntity.setPublicId(driverRequest.getPublicId());
            driverEntity.setName(driverRequest.getName());
            driverEntity.setAvailable(driverRequest.isAvailable());
            driverEntity.setUpdatedAt(driverRequest.getUpdatedAt());
            return driverEntity;
        }

        public DriverEntity toDriverEntity(DriverResponse driverResponse) {
            if (driverResponse == null) {
                return null;
            }
            final DriverEntity driverEntity = new DriverEntity();
            driverEntity.setPublicId(driverResponse.getPublicId());
            driverEntity.setName(driverResponse.getName());
            driverEntity.setAvailable(driverResponse.isAvailable());
            driverEntity.setUpdatedAt(driverResponse.getUpdatedAt());
            return driverEntity;
        }

        public DriverRequest toDriverRequest(DriverEntity driverEntity) {
            if (driverEntity == null) {
                return null;
            }
            final DriverRequest driverRequest = new DriverRequest();
            driverRequest.setPublicId(driverEntity.getPublicId());
            driverRequest.setName(driverEntity.getName());
            driverRequest.setAvailable(driverEntity.isAvailable());
            driverRequest.setCreatedAt(driverEntity.getCreatedAt());
            driverRequest.setUpdatedAt(driverEntity.getUpdatedAt());
            return driverRequest;
        }

        public DriverResponse toDriverResponse(DriverEntity driverEntity) {
            if (driverEntity == null) {
                return null;
            }
            final DriverResponse driverResponse = new DriverResponse();
            driverResponse.setPublicId(driverEntity.getPublicId());
            driverResponse.setName(driverEntity.getName());
            driverResponse.setAvailable(driverEntity.isAvailable());
            driverResponse.setCreatedAt(driverEntity.getCreatedAt());
            driverResponse.setUpdatedAt(driverEntity.getUpdatedAt());
            return driverResponse;
        }

        public DeliveryEntity toDeliveryEntity(DeliveryRequest deliveryRequest) {
            if (deliveryRequest == null) {
                return null;
            }
            final DeliveryEntity deliveryEntity = new DeliveryEntity();
            deliveryEntity.setPublicId(deliveryRequest.getPublicId());
            deliveryEntity.setPickupAddress(deliveryRequest.getPickupAddress());
            deliveryEntity.setDropoffAddress(deliveryRequest.getDropoffAddress());
            return deliveryEntity;
        }

        public DeliveryEntity toDeliveryEntity(DeliveryResponse deliveryResponse) {
            if (deliveryResponse == null) {
                return null;
            }
            final DeliveryEntity deliveryEntity = new DeliveryEntity();
            deliveryEntity.setPublicId(deliveryResponse.getPublicId());
            deliveryEntity.setPickupAddress(deliveryResponse.getPickupAddress());
            deliveryEntity.setDropoffAddress(deliveryResponse.getDropoffAddress());
            return deliveryEntity;
        }

        public DeliveryRequest toDeliveryRequest(DeliveryEntity deliveryEntity) {
            if (deliveryEntity == null) {
                return null;
            }
            final DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setPublicId(deliveryEntity.getPublicId());
            deliveryRequest.setPickupAddress(deliveryEntity.getPickupAddress());
            deliveryRequest.setDropoffAddress(deliveryEntity.getDropoffAddress());
            return deliveryRequest;
        }

        public DeliveryResponse toDeliveryResponse(DeliveryEntity deliveryEntity) {
            if (deliveryEntity == null) {
                return null;
            }
            final DeliveryResponse deliveryResponse = new DeliveryResponse();
            deliveryResponse.setPublicId(deliveryEntity.getPublicId());
            deliveryResponse.setPickupAddress(deliveryEntity.getPickupAddress());
            deliveryResponse.setDropoffAddress(deliveryEntity.getDropoffAddress());
            return deliveryResponse;
        }

        public Iterable<DeliveryEntity> toDeliveriesEntities(RoundRequest roundRequest) {
            if (roundRequest == null) {
                return null;
            }
            final List<DeliveryEntity> deliveries = new ArrayList<DeliveryEntity>();
            for (DeliveryResponse delivery : roundRequest.getDeliveries()) {
                deliveries.add(RoundMapperHelper.this.toDeliveryEntity(delivery));
            }
            return deliveries;
        }

        public Iterable<DeliveryRequest> toDeliveriesRequests(RoundEntity roundEntity) {
            if (roundEntity == null) {
                return null;
            }
            final List<DeliveryRequest> deliveries = new ArrayList<DeliveryRequest>();
            for (DeliveryEntity delivery : roundEntity.getDeliveries()) {
                deliveries.add(RoundMapperHelper.this.toDeliveryRequest(delivery));
            }
            return deliveries;
        }

        public Iterable<DeliveryResponse> toDeliveriesResponses(RoundEntity roundEntity) {
            if (roundEntity == null) {
                return null;
            }
            final List<DeliveryResponse> deliveries = new ArrayList<DeliveryResponse>();
            for (DeliveryEntity delivery : roundEntity.getDeliveries()) {
                deliveries.add(RoundMapperHelper.this.toDeliveryResponse(delivery));
            }
            return deliveries;
        }

    }
}
