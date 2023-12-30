package fr.univrouen.deliverysystem.driver;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;

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
import org.hibernate.annotations.UpdateTimestamp;

import fr.univrouen.deliverysystem.round.RoundEntity;

@Entity
@Table(name = "driver")
public class DriverEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(DriverEntity.class);

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

    @Column(name = "name")
    @NotBlank(message = "Le champ name est requis.")
    @Size(min = 2, max = 50, message = "Le nom du conducteur doit contenir entre {min} et {max} caractères.")
    private String name;

    @Column(name = "isAvailable")
    @NotNull(message = "Le champ isAvailable est requis.")
    // @AssertTrue(message = "Le champ isAvailable doit être true ou false.")
    // @AssertFalse(message = "Le champ isAvailable doit être true ou false.")
    private boolean isAvailable;

    @OneToMany(mappedBy = "driver", cascade = CascadeType.ALL)
    private List<RoundEntity> rounds;

    public DriverEntity() {
        this.id = 0L;
        this.publicId = "";
        this.name = "";
        this.isAvailable = false;
        this.createdAt = this.updatedAt = Instant.now();
        this.rounds = new ArrayList<RoundEntity>();
    }

    public DriverEntity(Long id, String publicId, String name, boolean isAvailable, Instant createdAt,
            Instant updatedAt, List<RoundEntity> rounds) {
        this.id = id;
        this.publicId = publicId;
        this.name = name;
        this.isAvailable = isAvailable;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.rounds = rounds;
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

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public List<RoundEntity> getRounds() {
        return rounds;
    }

    public void setRounds(List<RoundEntity> rounds) {
        this.rounds = rounds;
    }

    @Override
    public String toString() {
        final String createdAtFormatted = createdAt != null ? ZonedDateTime.ofInstant(createdAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        final String updatedAtFormatted = updatedAt != null ? ZonedDateTime.ofInstant(updatedAt, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "N/A";
        return "Driver[" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isAvailable=" + isAvailable +
                ", createdAt=" + createdAtFormatted +
                ", updatedAt=" + updatedAtFormatted +
                "]";
    }

    @PrePersist
    public void onPrePersist() {
        log.debug("Tentative de création d'un nouveau livreur : " + getName());
    }

    @PostPersist
    public void onPostPersist() {
        log.debug("Livreur '" + getName() + "' créé avec l'ID : " + getId());
    }

    @PreRemove
    public void onPreRemove() {
        log.debug("Tentative de suppression du Livreur : " + getName());
    }

    @PostRemove
    public void onPostRemove() {
        log.debug("Livreur '" + getName() + "' supprimé");
    }

    @PreUpdate
    public void onPreUpdate() {
        log.debug("Tentative de mise à jour du Livreur : " + getName());
    }

    @PostUpdate
    public void onPostUpdate() {
        log.debug("Livreur '" + getName() + "' mis à jour");
    }

    @PostLoad
    public void onPostLoad() {
        log.debug("Livreur '" + getName() + "' chargé");
    }

    public static DriverEntityBuilder builder() {
        return new DriverEntityBuilder();
    }

    public static class DriverEntityBuilder {

        private Long id;
        private Instant createdAt;
        private Instant updatedAt;
        private String publicId;
        private String name;
        private boolean isAvailable;
        private List<RoundEntity> rounds;

        public DriverEntityBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DriverEntityBuilder createdAt(Instant createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public DriverEntityBuilder updatedAt(Instant updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public DriverEntityBuilder publicId(String publicId) {
            this.publicId = publicId;
            return this;
        }

        public DriverEntityBuilder name(String name) {
            this.name = name;
            return this;
        }

        public DriverEntityBuilder isAvailable(boolean isAvailable) {
            this.isAvailable = isAvailable;
            return this;
        }

        public DriverEntityBuilder rounds(List<RoundEntity> rounds) {
            this.rounds = rounds;
            return this;
        }

        public DriverEntity build() {
            final DriverEntity driverEntity = new DriverEntity();
            driverEntity.setId(this.id);
            driverEntity.setCreatedAt(this.createdAt);
            driverEntity.setUpdatedAt(this.updatedAt);
            driverEntity.setPublicId(this.publicId);
            driverEntity.setName(this.name);
            driverEntity.setAvailable(this.isAvailable);
            driverEntity.setRounds(this.rounds);
            return driverEntity;
        }
    }

}
