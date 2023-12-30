package fr.univrouen.deliverysystem.delivery;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import fr.univrouen.deliverysystem.round.RoundRequest;

public class DeliveryRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String publicId;

    @NotBlank(message = "Le champ pickupAddress est requis.")
    @Size(min = 2, max = 95, message = "L'adresse de ramassage doit contenir entre {min} et {max} caractères.")
    private String pickupAddress;

    @NotBlank(message = "Le champ dropoffAddress est requis.")
    @Size(min = 2, max = 95, message = "L'adresse de dépôt doit contenir entre {min} et {max} caractères.")
    private String dropoffAddress;
    
    private RoundRequest round;

    public DeliveryRequest() {
        this.publicId = "";
        this.pickupAddress = "";
        this.dropoffAddress = "";
        this.round = null;
    }

    public DeliveryRequest(String publicId, String pickupAddress, String dropoffAddress, RoundRequest round) {
        this.publicId = publicId;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
        this.round = round;
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

    public RoundRequest getRound() {
        return round;
    }

    public void setRound(RoundRequest round) {
        this.round = round;
    }

    @Override
    public String toString() {
        return "DeliveryRequest[" +
                "publicId='" + publicId + '\'' +
                ", pickupAddress='" + pickupAddress + '\'' +
                ", dropoffAddress='" + dropoffAddress + '\'' +
                "]";
    }

    public static DeliveryRequestBuilder builder() {
        return new DeliveryRequestBuilder();
    }

    public static class DeliveryRequestBuilder {

        private String publicId;
        private String pickupAddress;
        private String dropoffAddress;
        private RoundRequest round;

        public DeliveryRequestBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public DeliveryRequestBuilder pickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
            return this;
        }

        public DeliveryRequestBuilder dropoffAddress(String dropoffAddress) {
            this.dropoffAddress = dropoffAddress;
            return this;
        }

        public DeliveryRequestBuilder round(RoundRequest round) {
            this.round = round;
            return this;
        }

        public DeliveryRequest build() {
            final DeliveryRequest deliveryRequest = new DeliveryRequest();
            deliveryRequest.setPublicId(this.publicId);
            deliveryRequest.setPickupAddress(this.pickupAddress);
            deliveryRequest.setDropoffAddress(this.dropoffAddress);
            deliveryRequest.setRound(this.round);
            return deliveryRequest;
        }
    }
}
