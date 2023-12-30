package fr.univrouen.deliverysystem.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import fr.univrouen.deliverysystem.round.RoundEntity;
import fr.univrouen.deliverysystem.round.RoundRepository;
import fr.univrouen.deliverysystem.utils.RandomIDGenerator;
import jakarta.transaction.Transactional;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DriverService implements Serializable {

    private static final long serialVersionUID = 1L;

    private final DriverRepository driverRepository;
    private final RoundRepository roundRepository;
    private final RandomIDGenerator randomIDGenerator;

    public DriverService(@Autowired DriverRepository driverRepository, @Autowired RoundRepository roundRepository,
            @Autowired RandomIDGenerator randomIDGenerator) {
        this.driverRepository = driverRepository;
        this.roundRepository = roundRepository;
        this.randomIDGenerator = randomIDGenerator;
    }

    public List<DriverEntity> getAllDrivers() {
        return driverRepository.findAll();
    }

    public Page<DriverEntity> getAllDrivers(Pageable pageable) {
        return driverRepository.findAll(pageable);
    }

    public Page<DriverEntity> searchDrivers(DriverFilter filter, Pageable pageable) {
        Specification<DriverEntity> driverSpecification = DriverFilterSpecification.filterBy(filter);
        return driverRepository.findAll(driverSpecification, pageable);
    }

    public DriverEntity getDriverByPublicId(String publicId) {
        return driverRepository.findByPublicId(publicId).orElseThrow(() -> new DriverNotFoundException(
                "Driver not found",
                String.format("Le livreur avec l'id %s n'a pas été trouvé.", publicId),
                HttpStatus.NOT_FOUND));
    }

    public DriverEntity getDriverByName(String name) {
        return driverRepository.findByName(name).orElseThrow(() -> new DriverNotFoundException(
                "Driver not found",
                String.format("Le livreur avec le nom %s n'a pas été trouvé.", name),
                HttpStatus.NOT_FOUND));
    }

    @Transactional
    public DriverEntity createDriver(DriverEntity driverEntity) {
        driverEntity.setPublicId(randomIDGenerator.generateStringId(RandomIDGenerator.getDefaultLength()));
        driverEntity.setCreatedAt(Instant.now());
        driverEntity.setUpdatedAt(Instant.now());
        return driverRepository.save(driverEntity);
    }

    @Transactional
    public Iterable<DriverEntity> createDrivers(Iterable<DriverEntity> driversEntities) {
        for (DriverEntity driverEntity : driversEntities) {
            driverEntity.setCreatedAt(Instant.now());
            driverEntity.setUpdatedAt(Instant.now());
        }
        return driverRepository.saveAll(driversEntities);
    }

    @Transactional
    public DriverEntity updateDriver(String publicId, DriverEntity updatedDriver) {
        final DriverEntity existingDriver = driverRepository.findByPublicId(publicId)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Driver not found",
                        String.format("Le livreur avec l'id %s n'a pas été trouvé.", publicId),
                        HttpStatus.NOT_FOUND));
        existingDriver.setUpdatedAt(Instant.now());
        existingDriver.setName(updatedDriver.getName());
        existingDriver.setAvailable(updatedDriver.isAvailable());
        /**
         * Vérification et association des livraisons
         * Association 1-n, avec OneToMany => List<RoundEntity>
         */
        final List<RoundEntity> existingRounds = existingDriver.getRounds();
        final List<RoundEntity> updatedRounds = updatedDriver.getRounds();
        if (updatedRounds != null) {
            /**
             * Dissociation à effectuer avant l'enregistrement des nouvelles valeurs ou de
             * la réassociation d'anciennes valeurs
             * Dissociation(Pas de suppression) des livraisons qui ne sont plus présentes
             * dans la mise à jour de la tournée
             */
            Boolean exists = false;
            for (RoundEntity existingRound : existingRounds) {
                exists = false;
                for (RoundEntity updatedRound : updatedRounds) {
                    if (existingRound.getPublicId().equals(updatedRound.getPublicId())) {
                        exists = true;
                    }
                }
                /**
                 * Si dans la nouvelle liste de rounds, il n'existe pas de round trouvable dans
                 * l'ancienne liste de rounds
                 * Alors dissociation
                 */
                if (!exists) {
                    final String roundPublicId = existingRound.getPublicId();
                    roundRepository.findByPublicId(roundPublicId).ifPresent(round -> {
                        // Met à jour la clé étrangère à null pour dissocier
                        round.setDriver(null);
                        roundRepository.save(round);
                    });
                }
            }
            /**
             * Enregistrement des nouvelles relations ou réassociation si elles ont été
             * supprimées
             */
            final List<RoundEntity> newRounds = new ArrayList<>();
            RoundEntity round = null;
            String roundPublicId = "";
            Optional<RoundEntity> existingRoundOptional = null;
            for (RoundEntity updatedRound : updatedRounds) {
                round = new RoundEntity();
                roundPublicId = updatedRound.getPublicId();
                existingRoundOptional = roundRepository.findByPublicId(roundPublicId);
                /**
                 * Si la relation est existe déja, alors mis à jour la clé étrangère pour la
                 * réassociation de nouveau
                 */
                if (existingRoundOptional.isPresent()) {
                    round = existingRoundOptional.get();
                    round.setDriver(existingDriver);
                    roundRepository.save(round);
                }
                /**
                 * Sinon, il s'agit d'une nouvelle relation, donc il faut reprendre le create du
                 * service de l'entité étrangère
                 */
                else {
                    round.setPublicId(randomIDGenerator.generateStringId(RandomIDGenerator.getDefaultLength()));
                    round.setCreatedAt(Instant.now());
                    round.setUpdatedAt(Instant.now());
                    round.setName(updatedRound.getName());
                    round.setStartDate(updatedRound.getStartDate());
                    round.setEndDate(updatedRound.getEndDate());
                    roundRepository.save(round);
                }
                newRounds.add(round);
            }
            existingDriver.setRounds(newRounds);
        } else {
            existingDriver.setRounds(new ArrayList<RoundEntity>());
        }
        return driverRepository.save(existingDriver);
    }

    @Transactional
    public void removeDriverByPublicId(String publicId) {
        final DriverEntity existingDriver = driverRepository.findByPublicId(publicId)
                .orElseThrow(() -> new DriverNotFoundException(
                        "Driver not found",
                        String.format("Le livreur avec l'id %s n'a pas été trouvé.", publicId),
                        HttpStatus.NOT_FOUND));
        driverRepository.deleteByPublicId(existingDriver.getPublicId());
    }

    @Transactional
    public void removeAllDrivers() {
        if (driverRepository.count() == 0) {
            throw new DriverNotFoundException(
                    "Drivers not found",
                    "Aucun livreur.",
                    HttpStatus.NOT_FOUND);
        }
        driverRepository.deleteAll();
    }

}
