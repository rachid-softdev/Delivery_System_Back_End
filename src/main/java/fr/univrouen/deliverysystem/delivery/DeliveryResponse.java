package fr.univrouen.deliverysystem.delivery;

import java.io.Serializable;
import java.time.Instant;

import fr.univrouen.deliverysystem.round.RoundResponse;
import fr.univrouen.deliverysystem.round.RoundResponse.RoundResponseBuilder;

public class DeliveryResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Instant createdAt;
    private Instant updatedAt;
    private String publicId;
    private String pickupAddress;
    private String dropoffAddress;
    private RoundResponse round;

    public DeliveryResponse() {
        this.createdAt = this.updatedAt = Instant.now();
        this.publicId = "";
        this.pickupAddress = "";
        this.dropoffAddress = "";
        this.round = null;
    }

    public DeliveryResponse(Instant createdAt, Instant updatedAt, String publicId, String pickupAddress,
            String dropoffAddress, RoundResponse round) {
        this.publicId = publicId;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.round = round;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPublicId() {
        return publicId;
    }

    public void setPublicId(String publicId) {
        this.publicId = publicId;
    }

    public String getPickupAddress() {
        return pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public RoundResponse getRound() {
        return round;
    }

    public void setRound(RoundResponse round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "DeliveryResponse[" +
                "createdAt='" + createdAt + '\'' +
                "updatedAt='" + updatedAt + '\'' +
                "publicId='" + publicId + '\'' +
                ", pickupAddress='" + pickupAddress + '\'' +
                ", dropoffAddress='" + dropoffAddress + '\'' +
                "]";
    }

    public static DeliveryResponseBuilder builder() {
        return new DeliveryResponseBuilder();
    }

    public static class DeliveryResponseBuilder {

        private Instant createdAt;
        private Instant updatedAt;
        private String publicId;
        private String pickupAddress;
        private String dropoffAddress;

        public DeliveryResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DeliveryResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DeliveryResponseBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public DeliveryResponseBuilder pickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
            return this;
        }

        public DeliveryResponseBuilder dropoffAddress(String dropoffAddress) {
            this.dropoffAddress = dropoffAddress;
            return this;
        }

        public DeliveryResponse build() {
            final DeliveryResponse deliveryResponse = new DeliveryResponse();
            deliveryResponse.setCreatedAt(this.createdAt);
            deliveryResponse.setUpdatedAt(this.updatedAt);
            deliveryResponse.setPublicId(this.publicId);
            deliveryResponse.setPickupAddress(this.pickupAddress);
            deliveryResponse.setDropoffAddress(this.dropoffAddress);
            return deliveryResponse;
        }
    }
}
