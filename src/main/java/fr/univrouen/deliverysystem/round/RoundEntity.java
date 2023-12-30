package fr.univrouen.deliverysystem.round;

import fr.univrouen.deliverysystem.delivery.DeliveryEntity;
import fr.univrouen.deliverysystem.driver.DriverEntity;
import io.micrometer.common.lang.Nullable;
import io.micrometer.common.lang.Nullable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.AssertFalse;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "round")
public class RoundEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(RoundEntity.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, updatable = true)
    @UpdateTimestamp
    private Instant updatedAt;

    @Column(name = "public_id", nullable = false, unique = true)
    private String publicId;

    @Column(name = "name")
    @NotBlank(message = "Le champ name est requis.")
    @Size(min = 2, max = 50, message = "Le nom de la tournée doit contenir entre {min} et {max} caractères.")
    private String name;

    @Column(name = "start_date")
    @NotNull(message = "Le champ startDate est requis.")
    private Instant startDate;

    @Column(name = "end_date")
    @NotNull(message = "Le champ endDate est requis.")
    private Instant endDate;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER, optional = true)
    @JoinColumn(name = "fk_driver_id", referencedColumnName = "id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private DriverEntity driver;

    @OneToMany(mappedBy = "round", cascade = CascadeType.ALL)
    private List<DeliveryEntity> deliveries;

    public RoundEntity() {
        this.id = 0L;
        this.createdAt = this.updatedAt = Instant.now();
        this.publicId = "";
        this.name = "";
        this.startDate = this.endDate = Instant.now();
        this.driver = null;
        this.deliveries = new ArrayList<DeliveryEntity>();
    }

    public RoundEntity(
            Long id,
            Instant createdAt,
            Instant updatedAt,
            String publicId,
            String name,
            Instant startDate,
            Instant endDate,
            DriverEntity driver,
            List<DeliveryEntity> deliveries) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.publicId = publicId;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.driver = driver;
        this.deliveries = deliveries;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public DriverEntity getDriver() {
        return this.driver;
    }

    public void setDriver(DriverEntity driver) {
        this.driver = driver;
    }

    public List<DeliveryEntity> getDeliveries() {
        return deliveries;
    }

    public void setDeliveries(List<DeliveryEntity> deliveries) {
        this.deliveries = deliveries;
    }

    @Override
    public String toString() {
        final String createdAtFormatted = createdAt != null
                ? ZonedDateTime
                        .ofInstant(createdAt, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";
        final String updatedAtFormatted = updatedAt != null
                ? ZonedDateTime
                        .ofInstant(updatedAt, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";
        final String startDateFormatted = startDate != null
                ? ZonedDateTime
                        .ofInstant(startDate, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";
        final String endDateFormatted = endDate != null
                ? ZonedDateTime
                        .ofInstant(endDate, ZoneId.systemDefault())
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
                : "N/A";
        return ("Round[" +
                "id=" +
                id +
                ", createdAt=" +
                createdAtFormatted +
                ", updatedAt=" +
                updatedAtFormatted +
                ", updatedAt=" +
                publicId +
                ", name='" +
                name +
                '\'' +
                ", updatedAt=" +
                updatedAtFormatted +
                ", startDate=" +
                startDateFormatted +
                ", endDate=" +
                endDateFormatted +
                "]");
    }

    @PrePersist
    public void onPrePersist() {
        log.debug("Tentative de création d'une nouvelle tournée : " + getName());
    }

    @PostPersist
    public void onPostPersist() {
        log.debug("Tournée '" + getName() + "' créé avec l'ID : " + getId());
    }

    @PreRemove
    public void onPreRemove() {
        log.debug("Tentative de suppression d'une tournée : " + getName());
    }

    @PostRemove
    public void onPostRemove() {
        log.debug("Tournée '" + getName() + "' supprimé");
    }

    @PreUpdate
    public void onPreUpdate() {
        log.debug("Tentative de mise à jour d'une tournée : " + getName());
    }

    @PostUpdate
    public void onPostUpdate() {
        log.debug("Tournée '" + getName() + "' mis à jour");
    }

    @PostLoad
    public void onPostLoad() {
        log.debug("Tournée '" + getName() + "' chargé");
    }

    public static RoundEntityBuilder builder() {
        return new RoundEntityBuilder();
    }

    public static class RoundEntityBuilder {

        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String publicId;
        private String name;
        private Instant startDate;
        private Instant endDate;
        private DriverEntity driver;
        private List<DeliveryEntity> deliveries;

        public RoundEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public RoundEntityBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public RoundEntityBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public RoundEntityBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public RoundEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public RoundEntityBuilder startDate(Instant startDate) {
            this.startDate = startDate;
            return this;
        }

        public RoundEntityBuilder endDate(Instant endDate) {
            this.endDate = endDate;
            return this;
        }

        public RoundEntityBuilder driver(DriverEntity driver) {
            this.driver = driver;
            return this;
        }

        public RoundEntityBuilder deliveries(List<DeliveryEntity> deliveries) {
            this.deliveries = deliveries;
            return this;
        }

        public RoundEntity build() {
            final RoundEntity roundEntity = new RoundEntity();
            roundEntity.setId(this.id);
            roundEntity.setCreatedAt(this.createdAt);
            roundEntity.setUpdatedAt(this.updatedAt);
            roundEntity.setPublicId(this.publicId);
            roundEntity.setName(this.name);
            roundEntity.setStartDate(this.startDate);
            roundEntity.setEndDate(this.endDate);
            roundEntity.setDriver(this.driver);
            roundEntity.setDeliveries(this.deliveries);
            return roundEntity;
        }
    }
}
