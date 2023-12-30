package fr.univrouen.deliverysystem.driver;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Tag(name = "Drivers", description = "Drivers APIs")
@RestController
@RequestMapping("/api/delivery_system/drivers")
@Validated
public class DriverController implements Serializable {

    private static final long serialVersionUID = 1L;

    private static Log log = LogFactory.getLog(DriverController.class);

    private final DriverService driverService;
    private final DriverMapper driverMapper;

    public DriverController(@Autowired DriverService driverService, @Autowired DriverMapper driverMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @Operation(summary = "Récupères tous les livreurs", description = "Récupères une liste de tous les livreurs", tags = {
            "drivers", "get" }, responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
            })
    @GetMapping({ "/all" })
    public ResponseEntity<List<DriverResponse>> getAllDrivers() {
        final List<DriverEntity> driversEntities = driverService.getAllDrivers();
        final List<DriverResponse> driversResponses = new ArrayList<>(driversEntities.size());
        for (DriverEntity driverEntity : driversEntities) {
            driversResponses.add(driverMapper.toDriverResponse(driverEntity));
        }
        log.info("getAllDrivers: " + driversResponses.size() + " drivers loaded.");
        return ResponseEntity.status(HttpStatus.OK).body(driversResponses);
    }

    @Operation(summary = "Récupère tous les livreurs avec pagination", description = "Récupère une liste de tous les livreurs avec pagination.", tags = {
            "drivers", "get" }, responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
            })
    @GetMapping({ "", "/" })
    public ResponseEntity<Page<DriverResponse>> getAllDrivers(
            Pageable pageable,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") @Size(min = 1, max = 50, message = "Le paramètre sortBy parameter doit contenir entre 1 and 50 caractères.") String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$", message = "Le paramètre sortOrder doit être 'asc' ou 'desc'.") String sortOrder) {
        final Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        final Page<DriverEntity> driversEntities = driverService.getAllDrivers(pageable);
        Page<DriverResponse> driversResponses = driversEntities
                .map(driverMapper::toDriverResponse);
        log.info("getAllDriversPaginated: " + driversResponses.getNumberOfElements() + " livreurs chargés avec "
                + driversResponses.getTotalPages() + " page(s).");
        return ResponseEntity.status(HttpStatus.OK).body(driversResponses);
    }

    @Operation(summary = "Récupère un livreur grâce à son identifiant publique", description = "Récupère un livreur en indiquant son identifiant publique.", tags = {
            "drivers", "get" }, responses = {
                    @ApiResponse(responseCode = "200", content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
            })
    @GetMapping("/test/{id}")
    public ResponseEntity<DriverResponse> getDriverById(
            @PathVariable(name = "id") @NotBlank(message = "L'identifiant du livreur ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant du livreur doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant du livreur doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId) {
        final DriverEntity driver = driverService.getDriverByPublicId(publicId);
        return ResponseEntity.status(HttpStatus.OK).body(driverMapper.toDriverResponse(driver));
    }

    @Operation(summary = "Crée un nouveau livreur", description = "Créer un nouveau livreur avec ses informations.", tags = {
            "drivers", "post" }, responses = {
                    @ApiResponse(responseCode = "201", content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
            })
    @PostMapping({ "", "/" })
    public ResponseEntity<DriverResponse> createDriver(@Valid @RequestBody DriverRequest driver) {
        final DriverEntity createdDriver = driverService.createDriver(driverMapper.toDriverEntity(driver));
        final DriverResponse driverResponse = driverMapper.toDriverResponse(createdDriver);
        log.info("createDriver: " + driverResponse.toString());
        return ResponseEntity.status(HttpStatus.CREATED).body(driverResponse);
    }

    @Operation(summary = "Met à jour un livreur existant", description = "Met à jour un livreur existant en donnant son identifiant publique et ses informations.", tags = {
            "drivers", "put" }, responses = {
                    @ApiResponse(responseCode = "202", content = {
                            @Content(schema = @Schema(implementation = DriverResponse.class), mediaType = "application/json") }),
                    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
            })
    @PutMapping("/{id}")
    public ResponseEntity<DriverResponse> updateDriver(
            @PathVariable(name = "id") @NotBlank(message = "L'identifiant du livreur ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant du livreur doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant du livreur du livreur doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId,
            @Valid @RequestBody DriverRequest driver) {
        final DriverEntity updatedDriver = driverService.updateDriver(publicId,
                driverMapper.toDriverEntity(driver));
        final DriverResponse driverResponse = driverMapper.toDriverResponse(updatedDriver);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(driverResponse);
    }

    @Operation(summary = "Supprime un livreur", description = "Supprime un livreur en indiquant son identifiant publique.", tags = {
            "drivers", "delete" }, responses = {
                    @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
                    @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDriver(
            @PathVariable(name = "id") @NotBlank(message = "L'identifiant du livreur ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant du livreur doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant du livreur du livreur doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId) {
        if (publicId != null) {
            driverService.removeDriverByPublicId(publicId);
            log.info(String.format("deleteDriver: Le livreur avec l'id %s a été supprimé.", publicId));
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping({ "/search" })
    public ResponseEntity<Page<DriverResponse>> searchDrivers(
            Pageable pageable,
            @RequestParam(name = "available", required = false) Boolean isAvailable,
            @RequestParam(name = "created_at_after", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAtAfter,
            @RequestParam(name = "created_at_before", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAtBefore,
            @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
            @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") String sortOrder) {
        final Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        final DriverFilter driverFilter = DriverFilter.builder()
                .createdAtAfter(createdAtAfter)
                .createdAtBefore(createdAtBefore)
                .isAvailable(isAvailable)
                .build();
        final Page<DriverEntity> driversEntities = driverService.searchDrivers(
                driverFilter,
                pageable);
        final Page<DriverResponse> driversResponses = driversEntities
                .map(driverMapper::toDriverResponse);
        log.info("searchDrivers: " + driversResponses.getNumberOfElements() + " livreurs chargés avec "
                + driversResponses.getTotalPages()
                + " page(s).");
        return ResponseEntity.status(HttpStatus.OK).body(driversResponses);
    }
}
