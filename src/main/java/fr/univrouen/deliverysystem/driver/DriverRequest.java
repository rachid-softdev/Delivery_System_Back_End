package fr.univrouen.deliverysystem.driver;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fr.univrouen.deliverysystem.round.RoundRequest;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class DriverRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private Instant createdAt;
    private Instant updatedAt;
    private String publicId;

    @NotBlank(message = "Le champ name est requis.")
    @Size(min = 2, max = 50, message = "Le nom du conducteur doit contenir entre 2 et 50 caractères.")
    private String name;

    @NotNull(message = "Le champ isAvailable est requis.")
    // @AssertTrue(message = "Le champ isAvailable doit être true ou false.")
    private boolean isAvailable;

    private List<RoundRequest> rounds;

    public DriverRequest() {
        this.name = "";
        this.isAvailable = false;
        this.createdAt = this.updatedAt = Instant.now();
        this.rounds = new ArrayList<RoundRequest>();
    }

    public DriverRequest(String publicId, String name, boolean isAvailable, Instant createdAt, Instant updatedAt,
            List<RoundRequest> rounds) {
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

    public List<RoundRequest> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundRequest> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        final String createdAtFormatted = createdAt != null ? ZonedDateTime.ofInstant(createdAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        final String updatedAtFormatted = updatedAt != null ? ZonedDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        return "DriverRequest[" +
                "publicId='" + publicId + '\'' +
                ", name=" + name +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                "]";
    }

    public static DriverRequestBuilder builder() {
        return new DriverRequestBuilder();
    }

    public static class DriverRequestBuilder {

        private String publicId;
        private String name;
        private boolean isAvailable;
        private Instant createdAt;
        private Instant updatedAt;
        private List<RoundRequest> rounds;

        public DriverRequestBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public DriverRequestBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DriverRequestBuilder isAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public DriverRequestBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DriverRequestBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DriverRequestBuilder rounds(List<RoundRequest> rounds) {
            this.rounds = rounds;
            return this;
        }

        public DriverRequest build() {
            DriverRequest driverRequest = new DriverRequest();
            driverRequest.setPublicId(this.publicId);
            driverRequest.setName(this.name);
            driverRequest.setAvailable(this.isAvailable);
            driverRequest.setCreatedAt(this.createdAt);
            driverRequest.setUpdatedAt(this.updatedAt);
            driverRequest.setRounds(this.rounds);
            return driverRequest;
        }
    }
}
