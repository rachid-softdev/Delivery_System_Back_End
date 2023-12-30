package fr.univrouen.deliverysystem.round;

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

@Tag(name = "Rounds", description = "Rounds APIs")
@RestController
@RequestMapping("/api/delivery_system/rounds")
@Validated
public class RoundController implements Serializable {

        private static final long serialVersionUID = 1L;

        private static Log log = LogFactory.getLog(RoundController.class);

        private final RoundService roundService;
        private final RoundMapper roundMapper;

        public RoundController(@Autowired RoundService roundService, @Autowired RoundMapper roundMapper) {
                this.roundService = roundService;
                this.roundMapper = roundMapper;
        }

        @Operation(summary = "Récupères toutes les tournées", description = "Récupères une liste de toutes les tournées", tags = {
                        "rounds", "get" }, responses = {
                                        @ApiResponse(responseCode = "200", content = {
                                                        @Content(schema = @Schema(implementation = RoundResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @GetMapping({ "/all" })
        public ResponseEntity<List<RoundResponse>> getAllRounds() {
                final List<RoundEntity> roundsEntities = roundService.getAllRounds();
                final List<RoundResponse> roundsResponses = new ArrayList<>(roundsEntities.size());
                for (RoundEntity roundEntity : roundsEntities) {
                        roundsResponses.add(roundMapper.toRoundResponse(roundEntity));
                }
                log.info("getAllRounds: " + roundsResponses.size() + " rounds loaded.");
                return ResponseEntity.status(HttpStatus.OK).body(roundsResponses);
        }

        @Operation(summary = "Récupère toutes les tournées avec pagination", description = "Récupère une liste de toutes les tournées avec pagination.", tags = {
                        "rounds", "get" }, responses = {
                                        @ApiResponse(responseCode = "200", content = {
                                                        @Content(schema = @Schema(implementation = RoundResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @GetMapping({ "", "/" })
        public ResponseEntity<Page<RoundResponse>> getAllRounds(
                        Pageable pageable,
                        @RequestParam(name = "search_date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant searchDate,
                        @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") @Size(min = 1, max = 50, message = "Le paramètre sortBy parameter doit contenir entre 1 and 50 caractères.") String sortBy,
                        @RequestParam(name = "sortOrder", required = false, defaultValue = "asc") @Pattern(regexp = "^(asc|desc)$", message = "Le paramètre sortOrder doit être 'asc' ou 'desc'.") String sortOrder) {
                final Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
                final RoundFilter filter = RoundFilter.builder().searchDate(searchDate).build();
                final Page<RoundEntity> roundsEntities = roundService.getAllRounds(filter, pageable);
                Page<RoundResponse> roundsResponses = roundsEntities
                                .map(roundMapper::toRoundResponse);
                log.info("getAllRoundsPaginated: " + roundsResponses.getNumberOfElements() + " tournées chargés avec "
                                + roundsResponses.getTotalPages() + " page(s).");
                return ResponseEntity.status(HttpStatus.OK).body(roundsResponses);
        }

        @Operation(summary = "Récupère un tournée grâce à son identifiant publique", description = "Récupère un tournée en indiquant son identifiant publique.", tags = {
                        "rounds", "get" }, responses = {
                                        @ApiResponse(responseCode = "200", content = {
                                                        @Content(schema = @Schema(implementation = RoundResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "404", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @GetMapping("/{public_id}")
        public ResponseEntity<RoundResponse> getRoundByPublicId(
                        @PathVariable(name = "public_id") @NotBlank(message = "L'identifiant de la tournée ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant de la tournée doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant de la tournée de la tournée doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId) {
                final RoundEntity round = roundService.getRoundByPublicId(publicId);
                return ResponseEntity.status(HttpStatus.OK).body(roundMapper.toRoundResponse(round));
        }

        @Operation(summary = "Crée une nouvelle tournée", description = "Créer une nouvelle tournée avec ses informations.", tags = {
                        "rounds", "post" }, responses = {
                                        @ApiResponse(responseCode = "201", content = {
                                                        @Content(schema = @Schema(implementation = RoundResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @PostMapping({ "", "/" })
        public ResponseEntity<RoundResponse> createRound(@Valid @RequestBody RoundRequest round) {
                final RoundEntity createdRound = roundService.createRound(roundMapper.toRoundEntity(round));
                final RoundResponse roundResponse = roundMapper.toRoundResponse(createdRound);
                log.info("createRound: " + roundResponse.toString());
                return ResponseEntity.status(HttpStatus.CREATED).body(roundResponse);
        }

        @Operation(summary = "Met à jour une tournée existante", description = "Met à jour une tournée existante en donnant son identifiant publique et ses informations.", tags = {
                        "rounds", "put" }, responses = {
                                        @ApiResponse(responseCode = "202", content = {
                                                        @Content(schema = @Schema(implementation = RoundResponse.class), mediaType = "application/json") }),
                                        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "500", content = { @Content(schema = @Schema()) })
                        })
        @PutMapping("/{id}")
        public ResponseEntity<RoundResponse> updateRound(
                        @PathVariable(name = "id") @NotBlank(message = "L'identifiant de la tournée ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant de la tournée doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant de la tournée doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId,
                        @Valid @RequestBody RoundRequest round) {
                final RoundEntity updatedRound = roundService.updateRound(publicId,
                                roundMapper.toRoundEntity(round));
                final RoundResponse roundResponse = roundMapper.toRoundResponse(updatedRound);
                return ResponseEntity.status(HttpStatus.ACCEPTED).body(roundResponse);
        }

        @Operation(summary = "Supprime une tournée", description = "Supprime une tournée en indiquant son identifiant publique.", tags = {
                        "rounds", "delete" }, responses = {
                                        @ApiResponse(responseCode = "204", content = { @Content(schema = @Schema()) }),
                                        @ApiResponse(responseCode = "400", content = { @Content(schema = @Schema()) })
                        })
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteRound(
                        @PathVariable(name = "id") @NotBlank(message = "L'identifiant de la tournée ne doit pas être vide.") @Size(min = 1, max = 50, message = "L'identifiant de la tournée doit contenir entre 1 and 50 caractères.") @Pattern(regexp = "^[0-9a-zA-Z-]+$", message = "L'identifiant de la tournée de la tournée doit contenir que des lettres minuscules/majuscules et des chiffres et tirets.") String publicId) {
                if (publicId != null) {
                        roundService.removeRoundByPublicId(publicId);
                        log.info(String.format("deleteRound: La tournée avec l'id %s a été supprimé.", publicId));
                        return ResponseEntity.noContent().build();
                }
                return ResponseEntity.badRequest().build();
        }

}
