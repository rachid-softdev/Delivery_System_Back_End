// The line `package fr.univrouen.deliverysystem.application.exception;` is declaring the package name
// for the class `ApplicationException`. In Java, packages are used to organize classes and prevent
// naming conflicts. The package name is a way to uniquely identify the location of the class within
// the project's directory structure. In this case, the class `ApplicationException` is part of the
// `fr.univrouen.deliverysystem.application.exception` package.
package fr.univrouen.deliverysystem.round;

import java.io.Serializable;
import java.sql.Driver;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import fr.univrouen.deliverysystem.delivery.DeliveryEntity;
import fr.univrouen.deliverysystem.delivery.DeliveryRepository;
import fr.univrouen.deliverysystem.driver.DriverEntity;
import fr.univrouen.deliverysystem.driver.DriverFilter;
import fr.univrouen.deliverysystem.driver.DriverFilterSpecification;
import fr.univrouen.deliverysystem.driver.DriverRepository;
import fr.univrouen.deliverysystem.utils.RandomIDGenerator;
import fr.univrouen.deliverysystem.application.exception.ApplicationException;

@Service
public class RoundService implements Serializable {

  private static final long serialVersionUID = 1L;

  private final RoundRepository roundRepository;
  private final DriverRepository driverRepository;
  private final DeliveryRepository deliveryRepository;

  private final RandomIDGenerator randomIDGenerator;

  public RoundService(
      @Autowired RoundRepository roundRepository,
      @Autowired DriverRepository driverRepository,
      @Autowired DeliveryRepository deliveryRepository,
      @Autowired RandomIDGenerator randomIDGenerator) {
    this.roundRepository = roundRepository;
    this.driverRepository = driverRepository;
    this.deliveryRepository = deliveryRepository;
    this.randomIDGenerator = randomIDGenerator;
  }

  public List<RoundEntity> getAllRounds() {
    return roundRepository.findAll();
  }

  public Page<RoundEntity> getAllRounds(RoundFilter filter, Pageable pageable) {
    Specification<RoundEntity> roundSpecification = RoundFilterSpecification.filterBy(filter);
    return roundRepository.findAll(roundSpecification, pageable);
  }

  /*
   * public Page<RoundEntity> searchRounds(RoundFilter filter, Pageable pageable)
   * {
   * Specification<RoundEntity> roundSpecification =
   * RoundFilterSpecification.filterBy(filter);
   * return roundRepository.findAll(roundSpecification, pageable);
   * }
   */
  public RoundEntity getRoundById(Long id) {
    return roundRepository
        .findById(id)
        .orElseThrow(() -> new RoundNotFoundException(
            "Round not found",
            String.format("La tournée avec l'id %d n'a pas été trouvé.", id),
            HttpStatus.NOT_FOUND));
  }

  public RoundEntity getRoundByPublicId(String publicId) {
    return roundRepository
        .findByPublicId(publicId)
        .orElseThrow(() -> new RoundNotFoundException(
            "Round not found",
            String.format(
                "La tournée avec l'id %s n'a pas été trouvé.",
                publicId),
            HttpStatus.NOT_FOUND));
  }

  public RoundEntity getRoundByName(String name) {
    return roundRepository
        .findByName(name)
        .orElseThrow(() -> new RoundNotFoundException(
            "Round not found",
            String.format("La tournée avec le nom %s n'a pas été trouvé.", name),
            HttpStatus.NOT_FOUND));
  }

  @Transactional
  public RoundEntity createRound(RoundEntity roundEntity) {
    /**
     * Règle métier :
     * Le système doit empêcher l’association d’une tournée à un livreur si celui-ci
     * possède une tournée
     * dont la date de début ou la date de fin est comprise dans la tournée qu’on
     * tente d’associer.
     */
    if (!roundRepository
        .findByDriverIdAndStartDateOrEndDate(roundEntity.getDriver().getId(), roundEntity.getStartDate(),
            roundEntity.getEndDate())
        .isEmpty()) {
      throw new ApplicationException(
          "Association de tournée impossible",
          "Le livreur possède déjà une tournée pendant la période spécifiée.",
          HttpStatus.BAD_REQUEST);
    }
    roundEntity.setPublicId(
        randomIDGenerator.generateStringId(RandomIDGenerator.getDefaultLength()));
    roundEntity.setCreatedAt(Instant.now());
    roundEntity.setUpdatedAt(Instant.now());
    final List<DeliveryEntity> deliveryEntitiesSaved = roundEntity.getDeliveries();
    /**
     * Association clé étrangère sur DriverEntity
     */
    final DriverEntity driver = roundEntity.getDriver();
    if (driver != null) {
      final String driverPublicId = driver.getPublicId();
      final DriverEntity existingDriver = driverRepository
          .findByPublicId(driverPublicId)
          .orElseGet(() -> {
            final DriverEntity newDriver = new DriverEntity();
            newDriver.setPublicId(
                randomIDGenerator.generateStringId(
                    RandomIDGenerator.getDefaultLength()));
            newDriver.setName(driver.getName());
            newDriver.setAvailable(driver.isAvailable());
            return driverRepository.save(newDriver);
          });
      roundEntity.setDriver(existingDriver);
    } else {
      roundEntity.setDriver(null);
    }
    // Saved l'assoc pour enregistrer car quand on save, on enregistre pas l'assoc,
    // elle sera mis à jour après dans le code
    roundEntity.setDeliveries(new ArrayList<>());
    RoundEntity resultRoundEntity = roundRepository.save(roundEntity);
    // Met à jour la clé primaire
    roundEntity.setId(resultRoundEntity.getId());
    // RéAffecte l'assocation n-1
    roundEntity.setDeliveries(deliveryEntitiesSaved);

    /**
     * Vérification et association des tournées
     */
    final List<DeliveryEntity> existingDeliveries = deliveryRepository.findAll();
    final List<DeliveryEntity> updatedDeliveries = roundEntity.getDeliveries();
    if (updatedDeliveries != null) {
      /**
       * Dissociation à effectuer avant l'enregistrement des nouvelles valeurs ou de
       * la réassociation d'anciennes valeurs
       * Dissociation(Pas de suppression) des tournées qui ne sont plus présentes
       * dans la mise à jour de la tournée
       */
      Boolean exists = false;
      for (DeliveryEntity existingDelivery : existingDeliveries) {
        exists = false;
        for (DeliveryEntity updatedDelivery : updatedDeliveries) {
          if (existingDelivery.getPublicId().equals(updatedDelivery.getPublicId())) {
            exists = true;
          }
        }
        /**
         * Si dans la nouvelle liste de deliveries, il n'existe pas de delivery
         * trouvable dans
         * l'ancienne liste de deliveries
         * Alors dissociation
         */
        if (!exists) {
          final String deliveryPublicId = existingDelivery.getPublicId();
          deliveryRepository
              .findByPublicId(deliveryPublicId)
              .ifPresent(delivery -> {
                // Met à jour la clé étrangère à null pour dissocier
                delivery.setRound(null);
                deliveryRepository.save(delivery);
              });
        }
      }
      /**
       * Enregistrement des nouvelles relations ou réassociation si elles ont été
       * supprimées
       */
      final List<DeliveryEntity> newDeliveries = new ArrayList<>();
      DeliveryEntity delivery = null;
      String deliveryPublicId = "";
      Optional<DeliveryEntity> existingDeliveryOptional = null;
      for (DeliveryEntity updatedDelivery : updatedDeliveries) {
        delivery = new DeliveryEntity();
        deliveryPublicId = updatedDelivery.getPublicId();
        existingDeliveryOptional = deliveryRepository.findByPublicId(deliveryPublicId);

        /**
         * Si la relation est existe déja, alors mis à jour la clé étrangère pour la
         * réassociation de nouveau
         */
        if (existingDeliveryOptional.isPresent()) {
          delivery = existingDeliveryOptional.get();
          delivery.setRound(resultRoundEntity);
          deliveryRepository.save(delivery);
        } /**
           * Sinon, il s'agit d'une nouvelle relation, donc il faut reprendre le create du
           * service de l'entité étrangère
           */
        else {
          delivery.setPublicId(
              randomIDGenerator.generateStringId(
                  RandomIDGenerator.getDefaultLength()));
          delivery.setCreatedAt(Instant.now());
          delivery.setUpdatedAt(Instant.now());
          delivery.setPickupAddress(updatedDelivery.getPickupAddress());
          delivery.setDropoffAddress(updatedDelivery.getDropoffAddress());
          delivery.setRound(resultRoundEntity);
          deliveryRepository.save(delivery);
        }
        newDeliveries.add(delivery);
      }
      roundEntity.setDeliveries(newDeliveries);
    } else {
      roundEntity.setDeliveries(new ArrayList<DeliveryEntity>());
    }
    return roundEntity;
  }

  @Transactional
  public Iterable<RoundEntity> createRounds(
      Iterable<RoundEntity> roundsEntities) {
    List<RoundEntity> rounds = new ArrayList<RoundEntity>();
    for (RoundEntity roundEntity : roundsEntities) {
      roundEntity.setCreatedAt(Instant.now());
      roundEntity.setUpdatedAt(Instant.now());
      rounds.add(this.createRound(roundEntity));
    }
    return rounds;
  }

  @Transactional
  public RoundEntity updateRound(String publicId, RoundEntity updatedRound) {
    final RoundEntity existingRound = roundRepository
        .findByPublicId(publicId)
        .orElseThrow(() -> new RoundNotFoundException(
            "Round not found",
            String.format(
                "La tournée avec l'id %s n'a pas été trouvé.",
                publicId),
            HttpStatus.NOT_FOUND));
    /**
     * Règle métier :
     * Le système doit empêcher l’association d’une tournée à un livreur si celui-ci
     * possède une tournée
     * dont la date de début ou la date de fin est comprise dans la tournée qu’on
     * tente d’associer.
     */
    if (!roundRepository.findByDriverIdAndStartDateOrEndDate(updatedRound.getDriver().getId(),
        updatedRound.getStartDate(), updatedRound.getEndDate()).isEmpty()) {
      throw new ApplicationException(
          "Association de tournée impossible",
          "Le livreur possède déjà une tournée pendant la période spécifiée.",
          HttpStatus.BAD_REQUEST);
    }
    existingRound.setUpdatedAt(updatedRound.getUpdatedAt());
    existingRound.setName(updatedRound.getName());
    existingRound.setStartDate(updatedRound.getStartDate());
    existingRound.setEndDate(updatedRound.getEndDate());
    /**
     * Vérification et association du driver
     * Association 1-n, avec ManyToOne => DriverEntity driver;
     */
    final DriverEntity driver = updatedRound.getDriver();
    if (driver != null) {
      final String driverPublicId = updatedRound.getDriver().getPublicId();
      final DriverEntity existingDriver = driverRepository
          .findByPublicId(driverPublicId)
          .orElseGet(() -> {
            final DriverEntity newDriver = new DriverEntity();
            newDriver.setPublicId(driverPublicId);
            newDriver.setName(updatedRound.getDriver().getName());
            newDriver.setAvailable(updatedRound.getDriver().isAvailable());
            return driverRepository.save(newDriver);
          });
      existingRound.setDriver(existingDriver);
    } else {
      existingRound.setDriver(null);
    }
    /**
     * Vérification et association des livraisons
     * Association 1-n, avec OneToMany => List<DeliveryEntity>
     */
    final List<DeliveryEntity> existingDeliveries = existingRound.getDeliveries();
    final List<DeliveryEntity> updatedDeliveries = updatedRound.getDeliveries();
    if (updatedDeliveries != null) {
      /**
       * Dissociation à effectuer avant l'enregistrement des nouvelles valeurs ou de
       * la réassociation d'anciennes valeurs
       * Dissociation(Pas de suppression) des livraisons qui ne sont plus présentes
       * dans la mise à jour de la tournée
       */
      Boolean exists = false;
      for (DeliveryEntity existingDelivery : existingDeliveries) {
        exists = false;
        for (DeliveryEntity updatedDelivery : updatedDeliveries) {
          if (existingDelivery.getPublicId().equals(updatedDelivery.getPublicId())) {
            exists = true;
          }
        }
        /**
         * Si dans la nouvelle liste de deliveries, il n'existe pas de delivery
         * trouvable dans l'ancienne delivery
         * Alors dissociation
         */
        if (!exists) {
          final String deliveryPublicId = existingDelivery.getPublicId();
          deliveryRepository
              .findByPublicId(deliveryPublicId)
              .ifPresent(delivery -> {
                // Met à jour la clé étrangère à null pour dissocier
                delivery.setRound(null);
                deliveryRepository.save(delivery);
              });
        }
      }
      /**
       * Enregistrement des nouvelles relations ou réassociation si elles ont été
       * supprimées
       */
      final List<DeliveryEntity> newDeliveries = new ArrayList<>();
      DeliveryEntity delivery = null;
      String deliveryPublicId = "";
      Optional<DeliveryEntity> existingDeliveryOptional = null;
      for (DeliveryEntity updatedDelivery : updatedDeliveries) {
        delivery = new DeliveryEntity();
        deliveryPublicId = updatedDelivery.getPublicId();
        existingDeliveryOptional = deliveryRepository.findByPublicId(deliveryPublicId);
        /**
         * Si la relation est existe déja, alors mis à jour la clé étrangère pour la
         * réassociation de nouveau
         */
        if (existingDeliveryOptional.isPresent()) {
          delivery = existingDeliveryOptional.get();
          delivery.setRound(existingRound);
          deliveryRepository.save(delivery);
        } /**
           * Sinon, il s'agit d'une nouvelle relation, donc il faut reprendre le create du
           * service de l'entité étrangère
           */
        else {
          delivery.setPublicId(
              randomIDGenerator.generateStringId(
                  RandomIDGenerator.getDefaultLength()));
          delivery.setCreatedAt(Instant.now());
          delivery.setUpdatedAt(Instant.now());
          delivery.setPickupAddress(updatedDelivery.getPickupAddress());
          delivery.setDropoffAddress(updatedDelivery.getDropoffAddress());
          deliveryRepository.save(delivery);
        }
        newDeliveries.add(delivery);
      }
      existingRound.setDeliveries(newDeliveries);
    } else {
      existingRound.setDeliveries(new ArrayList<DeliveryEntity>());
    }
    return roundRepository.save(existingRound);
  }

  @Transactional
  public void removeRoundByPublicId(String publicId) {
    final RoundEntity existingRound = roundRepository
        .findByPublicId(publicId)
        .orElseThrow(() -> new RoundNotFoundException(
            "Round not found",
            String.format(
                "La tournée avec l'id %s n'a pas été trouvé.",
                publicId),
            HttpStatus.NOT_FOUND));
    roundRepository.deleteByPublicId(existingRound.getPublicId());
  }

  @Transactional
  public void removeAllRounds() {
    if (roundRepository.count() == 0) {
      throw new RoundNotFoundException(
          "Rounds not found",
          "Aucune tournée.",
          HttpStatus.NOT_FOUND);
    }
    roundRepository.deleteAll();
  }
}
