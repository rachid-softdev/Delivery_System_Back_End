package fr.univrouen.deliverysystem.delivery;

import fr.univrouen.deliverysystem.round.RoundEntity;
import fr.univrouen.deliverysystem.round.RoundRepository;
import fr.univrouen.deliverysystem.utils.RandomIDGenerator;
import jakarta.transaction.Transactional;
import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class DeliveryService implements Serializable {

  private static final long serialVersionUID = 1L;

  private final DeliveryRepository deliveryRepository;
  private final RoundRepository roundRepository;
  private final RandomIDGenerator randomIDGenerator;

  public DeliveryService(
    @Autowired DeliveryRepository deliveryRepository,
    @Autowired RoundRepository roundRepository,
    @Autowired RandomIDGenerator randomIDGenerator
  ) {
    this.deliveryRepository = deliveryRepository;
    this.roundRepository = roundRepository;
    this.randomIDGenerator = randomIDGenerator;
  }

  public List<DeliveryEntity> getAllDeliveries() {
    return deliveryRepository.findAll();
  }

  public Page<DeliveryEntity> getAllDeliveries(Pageable pageable) {
    return deliveryRepository.findAll(pageable);
  }

  /*
   * public Page<DeliveryEntity> searchDeliveries(DeliveryFilter filter, Pageable
   * pageable)
   * {
   * Specification<DeliveryEntity> deliverySpecification =
   * DeliveryFilterSpecification.filterBy(filter);
   * return deliveryRepository.findAll(deliverySpecification, pageable);
   * }
   */

  public DeliveryEntity getDeliveryById(Long id) {
    return deliveryRepository
      .findById(id)
      .orElseThrow(() ->
        new DeliveryNotFoundException(
          "Round not found",
          String.format("La livraison avec l'id %d n'a pas été trouvé.", id),
          HttpStatus.NOT_FOUND
        )
      );
  }

  public DeliveryEntity getDeliveryByPublicId(String publicId) {
    return deliveryRepository
      .findByPublicId(publicId)
      .orElseThrow(() ->
        new DeliveryNotFoundException(
          "Delivery not found",
          String.format(
            "La livraison avec l'id %s n'a pas été trouvé.",
            publicId
          ),
          HttpStatus.NOT_FOUND
        )
      );
  }

  @Transactional
  public DeliveryEntity createDelivery(DeliveryEntity deliveryEntity) {
    deliveryEntity.setPublicId(
      randomIDGenerator.generateStringId(RandomIDGenerator.getDefaultLength())
    );
    deliveryEntity.setCreatedAt(Instant.now());
    deliveryEntity.setUpdatedAt(Instant.now());
    /**
     * Association clé étrangère sur RoundEntity
     */
    final RoundEntity round = deliveryEntity.getRound();
    if (round != null) {
      final String roundPublicId = round.getPublicId();
      final RoundEntity existingRound = roundRepository
        .findByPublicId(roundPublicId)
        .orElseGet(() -> {
          final RoundEntity newRound = new RoundEntity();
          newRound.setPublicId(
            randomIDGenerator.generateStringId(
              RandomIDGenerator.getDefaultLength()
            )
          );
          newRound.setName(round.getName());
          newRound.setStartDate(round.getStartDate());
          newRound.setEndDate(round.getEndDate());
          return roundRepository.save(newRound);
        });
      deliveryEntity.setRound(existingRound);
    } else {
      deliveryEntity.setRound(null);
    }
    return deliveryRepository.save(deliveryEntity);
  }

  @Transactional
  public Iterable<DeliveryEntity> createDeliveries(
    Iterable<DeliveryEntity> deliveriesEntities
  ) {
    List<DeliveryEntity> deliveries = new ArrayList<>();
    for (DeliveryEntity deliveryEntity : deliveriesEntities) {
      deliveryEntity.setPublicId(
        randomIDGenerator.generateStringId(RandomIDGenerator.getDefaultLength())
      );
      this.createDelivery(deliveryEntity);
      deliveries.add(deliveryEntity);
    }
    return deliveries; // deliveryRepository.saveAll(deliveriesEntities);
  }

  @Transactional
  public DeliveryEntity updateDelivery(
    String publicId,
    DeliveryEntity updatedDelivery
  ) {
    final DeliveryEntity existingDelivery = deliveryRepository
      .findByPublicId(publicId)
      .orElseThrow(() ->
        new DeliveryNotFoundException(
          "Delivery not found",
          String.format(
            "La livraison avec l'id %s n'a pas été trouvé.",
            publicId
          ),
          HttpStatus.NOT_FOUND
        )
      );
    existingDelivery.setUpdatedAt(Instant.now());
    existingDelivery.setPickupAddress(updatedDelivery.getPickupAddress());
    existingDelivery.setDropoffAddress(updatedDelivery.getDropoffAddress());
    /**
     * Vérification et association de la tournée
     * Association 1-n, avec ManyToOne => RoundEntity round;
     */
    final RoundEntity round = updatedDelivery.getRound();
    if (round != null) {
      final String roundPublicId = round.getPublicId();
      final RoundEntity existingRound = roundRepository
        .findByPublicId(roundPublicId)
        .orElseGet(() -> {
          final RoundEntity newRound = new RoundEntity();
          newRound.setPublicId(
            randomIDGenerator.generateStringId(
              RandomIDGenerator.getDefaultLength()
            )
          );
          newRound.setName(round.getName());
          newRound.setStartDate(round.getStartDate());
          newRound.setEndDate(round.getEndDate());
          return roundRepository.save(newRound);
        });
      existingDelivery.setRound(existingRound);
    } else {
      existingDelivery.setRound(null);
    }
    return deliveryRepository.save(existingDelivery);
  }

  @Transactional
  public void removeDeliveryByPublicId(String publicId) {
    final DeliveryEntity existingDelivery = deliveryRepository
      .findByPublicId(publicId)
      .orElseThrow(() ->
        new DeliveryNotFoundException(
          "Delivery not found",
          String.format(
            "La livraison avec l'id %s n'a pas été trouvé.",
            publicId
          ),
          HttpStatus.NOT_FOUND
        )
      );
    deliveryRepository.deleteByPublicId(existingDelivery.getPublicId());
  }

  @Transactional
  public void removeAllDeliveries() {
    if (deliveryRepository.count() == 0) {
      throw new DeliveryNotFoundException(
        "Deliveries not found",
        "Aucune livraison.",
        HttpStatus.NOT_FOUND
      );
    }
    deliveryRepository.deleteAll();
  }
}
