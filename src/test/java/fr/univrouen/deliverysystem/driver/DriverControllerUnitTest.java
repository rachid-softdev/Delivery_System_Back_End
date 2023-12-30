package fr.univrouen.deliverysystem.driver;

import java.util.List;
import java.util.Optional;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import fr.univrouen.deliverysystem.TestConfig;
import fr.univrouen.deliverysystem.round.RoundEntity;

@ExtendWith(SpringExtension.class)
@Import(TestConfig.class)
@WebMvcTest(DriverController.class)
public class DriverControllerUnitTest {

        private static final String BASE_URL = "/api/delivery_system/drivers";

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private DriverService driverService;

        public static final String getBaseUrl() {
                return DriverControllerUnitTest.BASE_URL;
        }

        @Test
        public void testGetDriverById_shouldReturnOk() throws Exception {
                // GIVEN
                final String publicId = "id-1";
                final String name = "John Doe";
                final boolean isAvailable = true;
                final Instant nowInstant = Instant.now();
                final List<RoundEntity> rounds = new ArrayList<RoundEntity>();
                final DriverEntity mockDriver = new DriverEntity(0L, publicId, name, isAvailable, nowInstant,
                                nowInstant,
                                rounds);
                Mockito.when(driverService.getDriverByPublicId(publicId)).thenReturn(mockDriver);
                // WHEN
                mockMvc.perform(MockMvcRequestBuilders.get(getBaseUrl() + "/" + publicId))
                                // THEN
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(isAvailable));
        }

        @Test
        public void testGetAllDrivers() throws Exception {
                // GIVEN
                final Instant nowInstant = Instant.now();
                final List<RoundEntity> rounds = new ArrayList<RoundEntity>();
                final String publicId1 = "id-1";
                final String name1 = "John Doe";
                final boolean isAvailable1 = true;
                final DriverEntity driver1 = new DriverEntity(0L, publicId1, name1, isAvailable1, nowInstant,
                                nowInstant,
                                rounds);
                final String publicId2 = "id-2";
                final String name2 = "Jane Smith";
                final boolean isAvailable2 = false;
                final DriverEntity driver2 = new DriverEntity(0L, publicId2, name2, isAvailable2, nowInstant,
                                nowInstant,
                                rounds);
                final List<DriverEntity> drivers = Arrays.asList(driver1, driver2);
                final Sort sort = Sort.by(Sort.Order.asc("id"));
                final Pageable pageable = PageRequest.of(0, 10, sort);
                // WHEN
                Mockito.when(driverService.getAllDrivers(pageable))
                                .thenReturn(new PageImpl<>(drivers, pageable, drivers.size()));
                mockMvc.perform(MockMvcRequestBuilders.get(getBaseUrl()))
                                // THEN
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(name1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[0].available").value(isAvailable1))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(name2))
                                .andExpect(MockMvcResultMatchers.jsonPath("$[1].available").value(isAvailable2));
        }

        @Test
        public void testCreateDriver() throws Exception {
                // GIVEN
                final String publicId = "id-1";
                final String name = "John Doe";
                final boolean isAvailable = true;
                final Instant nowInstant = Instant.now();
                final List<RoundEntity> rounds = new ArrayList<RoundEntity>();
                final DriverEntity mockDriver = new DriverEntity(0L, publicId, name, isAvailable, nowInstant,
                                nowInstant,
                                rounds);
                Mockito.when(driverService.createDriver(Mockito.any(DriverEntity.class))).thenReturn(mockDriver);
                // WHEN
                mockMvc.perform(MockMvcRequestBuilders.post(getBaseUrl())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"John Doe\",\"isAvailable\":true}"))
                                // THEN
                                .andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(isAvailable));
        }

        @Test
        public void testUpdateDriver() throws Exception {
                // GIVEN
                final String publicId = "id-1";
                final String name = "John Doe";
                final boolean isAvailable = true;
                final Instant nowInstant = Instant.now();
                final List<RoundEntity> rounds = new ArrayList<RoundEntity>();
                final DriverEntity mockDriver = new DriverEntity(0L, publicId, name, isAvailable, nowInstant,
                                nowInstant,
                                rounds);
                Mockito.when(driverService.updateDriver(Mockito.anyString(), Mockito.any(DriverEntity.class)))
                                .thenReturn(mockDriver);
                // WHEN
                mockMvc.perform(MockMvcRequestBuilders.put(getBaseUrl() + "/" + publicId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("{\"name\":\"John Doe\",\"isAvailable\":true}"))
                                // THEN
                                .andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.available").value(isAvailable));
        }

        @Test
        public void testDeleteDriver() throws Exception {
                // GIVEN
                final String publicId = "id-1";
                // WHEN
                mockMvc.perform(MockMvcRequestBuilders.delete(getBaseUrl() + "/" + publicId))
                                // THEN
                                .andExpect(MockMvcResultMatchers.status().isNoContent());
                Mockito.verify(driverService, Mockito.times(1)).removeDriverByPublicId(publicId);
        }
}
