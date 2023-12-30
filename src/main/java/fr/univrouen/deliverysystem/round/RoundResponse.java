package fr.univrouen.deliverysystem.round;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import fr.univrouen.deliverysystem.delivery.DeliveryEntity;
import fr.univrouen.deliverysystem.delivery.DeliveryResponse;
import fr.univrouen.deliverysystem.driver.DriverResponse;
import fr.univrouen.deliverysystem.round.RoundResponse.RoundResponseBuilder;

public class RoundResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Instant createdAt;
    private Instant updatedAt;
    private String publicId;
    private String name;
    private Instant startDate;
    private Instant endDate;
    private DriverResponse driver;
    private List<DeliveryResponse> deliveries;

    public RoundResponse() {
        this.createdAt = this.updatedAt = Instant.now();
        this.publicId = "";
        this.name = "";
        this.startDate = this.endDate = Instant.now();
        this.driver = null;
        this.deliveries = new ArrayList<DeliveryResponse>();
    }

    public RoundResponse(Instant createdAt, Instant updatedAt, String publicId, String name, Instant startDate,
            Instant endDate, DriverResponse driver,
            List<DeliveryResponse> deliveries) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publicId = publicId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.driver = driver;
        this.deliveries = deliveries;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public DriverResponse getDriver() {
        return this.driver;
    }

    public void setDriver(DriverResponse driver) {
        this.driver = driver;
    }

    public List<DeliveryResponse> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryResponse> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public String toString() {
        return "RoundResponse[" +
                "createdAt='" + createdAt + '\'' +
                "updatedAt='" + updatedAt + '\'' +
                "publicId='" + publicId + '\'' +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                "]";
    }

    public static RoundResponseBuilder builder() {
        return new RoundResponseBuilder();
    }

    public static class RoundResponseBuilder {

        private Instant createdAt;
        private Instant updatedAt;
        private String publicId;
        private String name;
        private Instant startDate;
        private Instant endDate;
        private DriverResponse driver;
        private List<DeliveryResponse> deliveries;

        public RoundResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoundResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoundResponseBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public RoundResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoundResponseBuilder startDate(Instant startDate) {
            this.startDate = startDate;
            return this;
        }

        public RoundResponseBuilder endDate(Instant endDate) {
            this.endDate = endDate;
            return this;
        }

        public RoundResponseBuilder driver(DriverResponse driver) {
            this.driver = driver;
            return this;
        }

        public RoundResponseBuilder deliveries(List<DeliveryResponse> deliveries) {
            this.deliveries = deliveries;
            return this;
        }

        public RoundResponse build() {
            RoundResponse roundResponse = new RoundResponse();
            roundResponse.setCreatedAt(this.createdAt);
            roundResponse.setUpdatedAt(this.updatedAt);
            roundResponse.setPublicId(this.publicId);
            roundResponse.setName(this.name);
            roundResponse.setStartDate(this.startDate);
            roundResponse.setEndDate(this.endDate);
            roundResponse.setDriver(this.driver);
            roundResponse.setDeliveries(this.deliveries);
            return roundResponse;
        }
    }
}
