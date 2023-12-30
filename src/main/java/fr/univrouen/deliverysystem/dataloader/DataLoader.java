package fr.univrouen.deliverysystem.dataloader;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

import com.github.javafaker.Faker;

import fr.univrouen.deliverysystem.delivery.DeliveryEntity;
import fr.univrouen.deliverysystem.delivery.DeliveryRepository;
import fr.univrouen.deliverysystem.driver.DriverEntity;
import fr.univrouen.deliverysystem.driver.DriverRepository;
import fr.univrouen.deliverysystem.round.RoundEntity;
import fr.univrouen.deliverysystem.round.RoundRepository;
import fr.univrouen.deliverysystem.utils.RandomIDGenerator;

/**
 * Classe de données
 * Utilisation des repositories au lieu des services
 * car il y a pas de contraintes sur les règles métiers
 */
@Component
public class DataLoader {

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private RoundRepository roundRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Autowired
    private RandomIDGenerator randomIDGenerator;

    @PostConstruct
    public void loadData() {
        loadDrivers();
        loadRounds();
        loadDeliveries();
    }

    private void loadDrivers() {
        final Faker faker = new Faker(new Locale("fr"));
        final List<DriverEntity> drivers = new ArrayList<>();
        final int numberOfDrivers = 15;
        final Random random = new Random();
        for (int i = 0; i < numberOfDrivers; i++) {
            DriverEntity driver = new DriverEntity();
            driver.setPublicId(randomIDGenerator.generateRandomUUID());
            driver.setName(faker.name().name());
            driver.setAvailable(random.nextBoolean());
            drivers.add(driver);
        }
        driverRepository.saveAll(drivers);
        /**
         * final List<DriverEntity> defaultDrivers = new ArrayList<>();
         * final LocalDateTime firstDateTime = LocalDateTime.of(1970, 9, 1, 12, 0);
         * final LocalDateTime secondDateTime = LocalDateTime.of(1970, 9, 2, 14, 30);
         * final LocalDateTime thirdDateTime = LocalDateTime.of(1970, 9, 3, 10, 15);
         * final Instant firstInstant = firstDateTime.toInstant(ZoneOffset.UTC);
         * final Instant secondInstant = secondDateTime.toInstant(ZoneOffset.UTC);
         * final Instant thirdInstant = thirdDateTime.toInstant(ZoneOffset.UTC);
         * defaultDrivers.add(new DriverEntity(randomIDGenerator.generateRandomUUID(),
         * faker.name().name(), new Random().nextBoolean(), firstInstant,
         * firstInstant));
         * defaultDrivers.add(new DriverEntity(randomIDGenerator.generateRandomUUID(),
         * faker.name().name(), new Random().nextBoolean(), secondInstant,
         * secondInstant));
         * defaultDrivers.add(new DriverEntity(randomIDGenerator.generateRandomUUID(),
         * faker.name().name(), new Random().nextBoolean(), thirdInstant,
         * thirdInstant));
         * driverRepository.createDrivers(defaultDrivers);
         */
    }

    @PreDestroy
    public void removeData() {
        deliveryRepository.deleteAll();
        roundRepository.deleteAll();
        driverRepository.deleteAll();
    }

    private void loadRounds() {
        final Faker faker = new Faker(new Locale("fr"));
        final List<RoundEntity> rounds = new ArrayList<>();
        final int numberOfRounds = 15;
        final List<DriverEntity> drivers = driverRepository.findAll();
        final Random random = new Random();
        Instant nowInstant = null;
        int randomDays = 0;
        RoundEntity round = null;
        for (int i = 0; i < numberOfRounds; i++) {
            round = new RoundEntity();
            round.setPublicId(randomIDGenerator.generateRandomUUID());
            round.setName(faker.company().name());
            nowInstant = Instant.now();
            round.setStartDate(nowInstant);
            randomDays = faker.number().numberBetween(1, 101);
            round.setEndDate(nowInstant.plus(randomDays, ChronoUnit.DAYS));
            // Attribution d'un livreur de manière aléatoire
            if (!drivers.isEmpty()) {
                final int index = random.nextInt(drivers.size());
                final DriverEntity randomDriver = drivers.get(index);
                round.setDriver(randomDriver);
                drivers.remove(index);
            }
            rounds.add(round);
        }
        roundRepository.saveAll(rounds);
    }

    private void loadDeliveries() {
        final Faker faker = new Faker(new Locale("fr"));
        final List<DeliveryEntity> deliveries = new ArrayList<>();
        final int numberOfDeliveries = 15;
        final List<RoundEntity> allRounds = roundRepository.findAll();
        final Random random = new Random();
        for (int i = 0; i < numberOfDeliveries; i++) {
            DeliveryEntity delivery = new DeliveryEntity();
            delivery.setPublicId(randomIDGenerator.generateRandomUUID());
            delivery.setPickupAddress(faker.address().fullAddress());
            delivery.setDropoffAddress(faker.address().fullAddress());
            // Attribution aléatoire d'une tournée
            if (!allRounds.isEmpty()) {
                final int index = random.nextInt(allRounds.size());
                final RoundEntity randomRound = allRounds.get(index);
                delivery.setRound(randomRound);
                allRounds.remove(index);
            }
            deliveries.add(delivery);
        }
        deliveryRepository.saveAll(deliveries);
    }

}
