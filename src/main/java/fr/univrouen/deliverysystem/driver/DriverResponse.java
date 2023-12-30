package fr.univrouen.deliverysystem.driver;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fr.univrouen.deliverysystem.round.RoundResponse;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DriverResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String publicId;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private boolean isAvailable;
    private List<RoundResponse> rounds;

    public DriverResponse() {
        this.name = "";
        this.isAvailable = false;
        this.createdAt = this.updatedAt = Instant.now();
        this.rounds = new ArrayList<RoundResponse>();
    }

    public DriverResponse(String publicId, String name, boolean isAvailable, Instant createdAt, Instant updatedAt,
            List<RoundResponse> rounds) {
        this.publicId = publicId;
        this.name = name;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rounds = rounds;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
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

    public List<RoundResponse> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundResponse> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        final String createdAtFormatted = createdAt != null ? ZonedDateTime.ofInstant(createdAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        final String updatedAtFormatted = updatedAt != null ? ZonedDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        return "DriverResponse[" +
                "publicId='" + publicId + '\'' +
                ", name=" + name +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                "]";
    }

    public static class DriverResponseBuilder {

        private String publicId;
        private String name;
        private boolean isAvailable;
        private Instant createdAt;
        private Instant updatedAt;
        private List<RoundResponse> rounds;

        public DriverResponseBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public DriverResponseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DriverResponseBuilder isAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public DriverResponseBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DriverResponseBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DriverResponseBuilder rounds(List<RoundResponse> rounds) {
            this.rounds = rounds;
            return this;
        }

        public DriverResponse build() {
            DriverResponse driverResponse = new DriverResponse();
            driverResponse.setPublicId(this.publicId);
            driverResponse.setName(this.name);
            driverResponse.setAvailable(this.isAvailable);
            driverResponse.setCreatedAt(this.createdAt);
            driverResponse.setUpdatedAt(this.updatedAt);
            driverResponse.setRounds(this.rounds);
            return driverResponse;
        }
    }
}
