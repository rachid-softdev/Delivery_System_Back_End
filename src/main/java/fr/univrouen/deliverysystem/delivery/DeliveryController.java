package fr.univrouen.deliverysystem.delivery;

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

@Tag(name = "Deliveries", description = "Deliveries APIs")
@RestController
@RequestMapping("/api/delivery_system/deliveries")
@Validated
public class DeliveryController implements Serializable {

        private static final long serialVersionUID = 1L;

        private static Log log = LogFactory.getLog(DeliveryController.class);

        private final DeliveryService deliveryService;
        private final DeliveryMapper deliveryMapper;

        public DeliveryController(@Autowired DeliveryService deliveryService,
                        @Autowired DeliveryMapper deliveryMapper) {
                this.deliveryService = deliveryService;
                this.deliveryMapper = deliveryMapper;
        }

        @Operation(summary = "Récupères toutes les livraisons", description = "Récupères une liste de toutes les livraisons", tags = {
                        "deliveries", "get" }, responses = {
                                        @ApiResponse(responseCode = "200", content = {
                                                        @Content(schema = @Schema(implementation = DeliveryResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @GetMapping({ "/all" })
        public ResponseEntity<List<DeliveryResponse>> getAllDeliveries() {
                final List<DeliveryEntity> deliverysEntities = deliveryService.getAllDeliveries();
                final List<DeliveryResponse> deliverysResponses = new ArrayList<>(deliverysEntities.size());
                for (DeliveryEntity deliveryEntity : deliverysEntities) {
                        deliverysResponses.add(deliveryMapper.toDeliveryResponse(deliveryEntity));
                }
                log.info("getAllDeliveries: " + deliverysResponses.size() + " deliveries loaded.");
                return ResponseEntity.status(HttpStatus.OK).body(deliverysResponses);
        }

        @Operation(summary = "Récupère toutes les livraisons avec pagination", description = "Récupère une liste de toutes les livraisons avec pagination.", tags = {
                        "deliveries", "get" }, responses = {
                                        @ApiResponse(responseCode = "200", content = {
                                                        @Content(schema = @Schema(implementation = DeliveryResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @GetMapping({ "", "/" })
        public ResponseEntity<Page<DeliveryResponse>> getAllDeliveries(
                        Pageable pageable,
                        @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") @Size(min = 1, max = 50, message = "Le paramètre sortBy parameter doit contenir entre 1 and 50 caractères.") String sortBy,
                        @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$", message = "Le paramètre sortOrder doit être 'asc' ou 'desc'.") String sortOrder) {
                final Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
                final Page<DeliveryEntity> deliverysEntities = deliveryService.getAllDeliveries(pageable);
                Page<DeliveryResponse> deliverysResponses = deliverysEntities
                                .map(deliveryMapper::toDeliveryResponse);
                log.info("getAllDeliveriesPaginated: " + deliverysResponses.getNumberOfElements()
                                + " livraisons chargés avec "
                                + deliverysResponses.getTotalPages() + " page(s).");
                return ResponseEntity.status(HttpStatus.OK).body(deliverysResponses);
        }

        @Operation(summary = "Récupère une livraison grâce à son identifiant publique", description = "Récupère une livraison en indiquant son identifiant publique.", tags = {
                        "deliveries", "get" }, responses = {
                                        @ApiResponse(responseCode = "200", content = {
                                                        @Content(schema = @Schema(implementation = DeliveryResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @GetMapping("/{id}")
        public ResponseEntity<DeliveryResponse> getDeliveryByPublicId(
                        @PathVariable(name = "id") @NotBlank(message = "L'identifiant de la livraison ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant de la livraison doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant de la livraison doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId) {
                final DeliveryEntity delivery = deliveryService.getDeliveryByPublicId(publicId);
                return ResponseEntity.status(HttpStatus.OK).body(deliveryMapper.toDeliveryResponse(delivery));
        }

        @Operation(summary = "Crée une nouvelle livraison", description = "Créer une nouvelle livraison avec ses informations.", tags = {
                        "deliveries", "post" }, responses = {
                                        @ApiResponse(responseCode = "201", content = {
                                                        @Content(schema = @Schema(implementation = DeliveryResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @PostMapping({ "", "/" })
        public ResponseEntity<DeliveryResponse> createDelivery(@Valid @RequestBody DeliveryRequest delivery) {
                final DeliveryEntity createdDelivery = deliveryService
                                .createDelivery(deliveryMapper.toDeliveryEntity(delivery));
                final DeliveryResponse deliveryResponse = deliveryMapper.toDeliveryResponse(createdDelivery);
                log.info("createDelivery: " + deliveryResponse.toString());
                return ResponseEntity.status(HttpStatus.CREATED).body(deliveryResponse);
        }

        @Operation(summary = "Met à jour une livraison existante", description = "Met à jour une livraison existante en donnant son identifiant publique et ses informations.", tags = {
                        "deliveries", "put" }, responses = {
                                        @ApiResponse(responseCode = "202", content = {
                                                        @Content(schema = @Schema(implementation = DeliveryResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @PutMapping("/{id}")
        public ResponseEntity<DeliveryResponse> updateDeliveryByPublicId(
                        @PathVariable(name = "id") @NotBlank(message = "L'identifiant de la livraison ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant de la livraison doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant de la livraison doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId,
                        @Valid @RequestBody DeliveryRequest delivery) {
                final DeliveryEntity updatedDelivery = deliveryService.updateDelivery(publicId,
                                deliveryMapper.toDeliveryEntity(delivery));
                final DeliveryResponse deliveryResponse = deliveryMapper.toDeliveryResponse(updatedDelivery);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(deliveryResponse);
        }

        @Operation(summary = "Supprime une livraison", description = "Supprime une livraison en indiquant son identifiant publique.", tags = {
                        "deliveries", "delete" }, responses = {
                                        @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
                        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteDeliveryByPublicId(
                        @PathVariable(name = "id") @NotBlank(message = "L'identifiant de la livraison ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant de la livraison doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant de la livraison doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId) {
                if (publicId != null) {
                        deliveryService.removeDeliveryByPublicId(publicId);
                        log.info(String.format("deleteDelivery: La livraison avec l'id %s a été supprimé.", publicId));
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.badRequest().build();
        }

}
