package fr.univrouen.deliverysystem.driver;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.univrouen.deliverysystem.TestConfig;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class DriverIntegrationTest {

    private static final String BASE_URL = "/api/delivery_system/drivers";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    public static final String getBaseUrl() {
        return DriverIntegrationTest.BASE_URL;
    }

    @Test
    public void testGetDriverById() {
        // GIVEN
        final String publicId = "id-1";
        // WHEN
        final ResponseEntity<DriverEntity> response = restTemplate.getForEntity(getBaseUrl() + "/" + publicId,
                DriverEntity.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final DriverEntity driver = response.getBody();
        assertThat(driver).isNotNull();
        assertThat(driver.getPublicId()).isEqualTo(publicId);
        assertThat(driver.getName()).isEqualTo("John Doe");
        assertThat(driver.isAvailable()).isTrue();
    }

    @Test
    public void testGetDriverByIdWithFakeId() {
        // GIVEN
        final String publicId = "id-fake";
        // WHEN
        final ResponseEntity<DriverEntity> response = restTemplate.getForEntity(getBaseUrl() + "/" + publicId,
                DriverEntity.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenExistingDrivers_whenGetAllDrivers_thenReturnDrivers() {
        // GIVEN
        // WHEN
        final ResponseEntity<List> response = restTemplate.getForEntity(getBaseUrl(), List.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotEmpty();
    }

    @Test
    public void givenExistingDriverId_whenGetDriverById_thenReturnDriver() {
        // GIVEN
        // WHEN
        final Long existingDriverId = 1L;
        final ResponseEntity<DriverEntity> response = restTemplate
                .getForEntity(getBaseUrl() + "/" + existingDriverId, DriverEntity.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void givenNonExistingDriverId_whenGetDriverById_thenReturnNotFound() {
        // GIVEN
        // WHEN
        String unknowDriverId = "unknow-id";
        ResponseEntity<Void> response = restTemplate.getForEntity(getBaseUrl() + "/" + unknowDriverId,
                Void.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenNewDriver_whenCreateDriver_thenReturnCreatedDriver() {
        // GIVEN
        // WHEN
        ResponseEntity<DriverEntity> response = restTemplate.postForEntity(getBaseUrl(),
                new DriverEntity(), DriverEntity.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void givenUpdatedDriver_whenUpdateDriver_thenReturnUpdatedDriver() {
        // GIVEN
        // WHEN
        Long existingDriverId = 1L;
        ResponseEntity<DriverEntity> response = restTemplate.postForEntity(
                getBaseUrl() + existingDriverId, new DriverEntity(), DriverEntity.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void givenExistingDriverId_whenDeleteDriver_thenReturnNoContent() {
        // GIVEN
        // WHEN
        Long existingDriverId = 1L;
        ResponseEntity<Void> response = restTemplate.exchange(getBaseUrl() + "/" + existingDriverId,
                HttpMethod.DELETE, null, Void.class);
        // THEN
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

}
