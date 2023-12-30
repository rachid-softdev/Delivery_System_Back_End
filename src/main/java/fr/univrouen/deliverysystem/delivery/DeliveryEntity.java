package fr.univrouen.deliverysystem.delivery;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PostLoad;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

import fr.univrouen.deliverysystem.round.RoundEntity;

@Entity
@Table(name = "delivery")
public class DeliveryEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    private static Log log = LogFactory.getLog(DeliveryEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = true)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @Column(name = "pickup_address")
    @NotBlank(message = "Le champ pickupAddress est requis.")
    @Size(min = 2, max = 95, message = "L'adresse de ramassage doit contenir entre {min} et {max} caractères.")
    private String pickupAddress;

    @Column(name = "dropoff_address")
    @NotBlank(message = "Le champ dropoffAddress est requis.")
    @Size(min = 2, max = 95, message = "L'adresse de dépôt doit contenir entre {min} et {max} caractères.")
    private String dropoffAddress;

    @ManyToOne(cascade = CascadeType.ALL, optional = true)
    @JoinColumn(name = "fk_round_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.NO_ACTION)
    private RoundEntity round;

    public DeliveryEntity() {
        this.id = 0L;
        this.createdAt = this.updatedAt = Instant.now();
        this.publicId = "";
        this.pickupAddress = "";
        this.dropoffAddress = "";
        this.round = null;
    }

    public DeliveryEntity(Long id, Instant createdAt, Instant updatedAt, String publicId, String pickupAddress,
            String dropoffAddress, RoundEntity round) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publicId = publicId;
        this.pickupAddress = pickupAddress;
        this.dropoffAddress = dropoffAddress;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
        return this.pickupAddress;
    }

    public void setPickupAddress(String pickupAddress) {
        this.pickupAddress = pickupAddress;
    }

    public String getDropoffAddress() {
        return this.dropoffAddress;
    }

    public void setDropoffAddress(String dropoffAddress) {
        this.dropoffAddress = dropoffAddress;
    }

    public RoundEntity getRound() {
        return round;
    }

    public void setRound(RoundEntity round) {
        this.round = round;
    }

    @Override
    public String toString() {
        final String createdAtFormatted = createdAt != null ? ZonedDateTime.ofInstant(createdAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        final String updatedAtFormatted = updatedAt != null ? ZonedDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        return "DeliveryEntity[" +
                "id=" + id +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                ", publicId=" + publicId +
                ", pickupAddress='" + pickupAddress + '\'' +
                ", dropoffAddress='" + dropoffAddress + '\'' +
                "]";
    }

    @PrePersist
    public void onPrePersist() {
        log.debug("Tentative de création d'une nouvelle livraison : " + getPublicId());
    }

    @PostPersist
    public void onPostPersist() {
        log.debug("Livraison '" + getPublicId() + "' créée avec l'ID : " + getId());
    }

    @PreRemove
    public void onPreRemove() {
        log.debug("Tentative de suppression d'une livraison : " + getPublicId());
    }

    @PostRemove
    public void onPostRemove() {
        log.debug("Livraison '" + getPublicId() + "' supprimée");
    }

    @PreUpdate
    public void onPreUpdate() {
        log.debug("Tentative de mise à jour d'une livraison : " + getPublicId());
    }

    @PostUpdate
    public void onPostUpdate() {
        log.debug("Livraison '" + getPublicId() + "' mise à jour");
    }

    @PostLoad
    public void onPostLoad() {
        log.debug("Livraison '" + getPublicId() + "' chargée");
    }

    public static DeliveryEntityBuilder builder() {
        return new DeliveryEntityBuilder();
    }

    public static class DeliveryEntityBuilder {

        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String publicId;
        private String pickupAddress;
        private String dropoffAddress;

        public DeliveryEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DeliveryEntityBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DeliveryEntityBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DeliveryEntityBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public DeliveryEntityBuilder pickupAddress(String pickupAddress) {
            this.pickupAddress = pickupAddress;
            return this;
        }

        public DeliveryEntityBuilder dropoffAddress(String dropoffAddress) {
            this.dropoffAddress = dropoffAddress;
            return this;
        }

        public DeliveryEntity build() {
            final DeliveryEntity deliveryEntity = new DeliveryEntity();
            deliveryEntity.setId(this.id);
            deliveryEntity.setCreatedAt(this.createdAt);
            deliveryEntity.setUpdatedAt(this.updatedAt);
            deliveryEntity.setPublicId(this.publicId);
            deliveryEntity.setPickupAddress(this.pickupAddress);
            deliveryEntity.setDropoffAddress(this.dropoffAddress);
            return deliveryEntity;
        }
    }
}
