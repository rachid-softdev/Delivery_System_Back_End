package fr.univrouen.deliverysystem.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;

@Component
public class RandomIDGenerator {

    private static final Random random = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Integer DEFAULT_LENGTH = 17;

    public static Integer getDefaultLength() {
        return RandomIDGenerator.DEFAULT_LENGTH;
    }

    public String generateStringId(Integer capacity) {
        if (capacity == 0 || capacity == null) {
            capacity = RandomIDGenerator.getDefaultLength();
        }
        StringBuilder id = new StringBuilder(capacity);
        for (int i = 0; i < capacity; i++) {
            id.append(ALPHABET.charAt(random.nextInt(ALPHABET.length())));
        }
        return new String(id);
    }

    public String generateRandomUUID() {
        final UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
