package com.exemple.patient_service.controller;

import com.exemple.patient_service.dto.PatientDTO;
import com.exemple.patient_service.service.PatientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Patient", description = "API de gestion des patients")
public class PatientController {
    
    private final PatientService patientService;
    
    /**
     * Créer un nouveau patient
     * @param patientDTO les données du patient
     * @return le patient créé
     */
    @PostMapping
    @Operation(summary = "Créer un nouveau patient", description = "Crée un nouveau patient avec les informations fournies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Patient créé avec succès",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides",
                    content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "409", description = "Numéro de sécurité sociale déjà existant")
    })
    public ResponseEntity<PatientDTO> createPatient(
            @Parameter(description = "Données du patient à créer", required = true)
            @Valid @RequestBody PatientDTO patientDTO) {
        log.info("REST request pour créer un nouveau patient");
        PatientDTO createdPatient = patientService.createPatient(patientDTO);
        return new ResponseEntity<>(createdPatient, HttpStatus.CREATED);
    }
    
    /**
     * Obtenir un patient par son ID
     * @param id l'identifiant du patient
     * @return le patient trouvé
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtenir un patient par ID", description = "Récupère les informations d'un patient par son identifiant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient trouvé",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    public ResponseEntity<PatientDTO> getPatientById(
            @Parameter(description = "ID du patient", required = true, example = "1")
            @PathVariable Long id) {
        log.info("REST request pour obtenir le patient avec l'ID: {}", id);
        PatientDTO patient = patientService.getPatientById(id);
        return ResponseEntity.ok(patient);
    }
    
    /**
     * Obtenir tous les patients
     * @return la liste des patients
     */
    @GetMapping
    @Operation(summary = "Obtenir tous les patients", description = "Récupère la liste de tous les patients")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Liste des patients récupérée",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    })
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        log.info("REST request pour obtenir tous les patients");
        List<PatientDTO> patients = patientService.getAllPatients();
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Mettre à jour un patient
     * @param id l'identifiant du patient
     * @param patientDTO les nouvelles données du patient
     * @return le patient mis à jour
     */
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un patient", description = "Met à jour les informations d'un patient existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient mis à jour avec succès",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données invalides"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé"),
            @ApiResponse(responseCode = "409", description = "Numéro de sécurité sociale déjà existant")
    })
    public ResponseEntity<PatientDTO> updatePatient(
            @Parameter(description = "ID du patient", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Nouvelles données du patient", required = true)
            @Valid @RequestBody PatientDTO patientDTO) {
        log.info("REST request pour mettre à jour le patient avec l'ID: {}", id);
        PatientDTO updatedPatient = patientService.updatePatient(id, patientDTO);
        return ResponseEntity.ok(updatedPatient);
    }
    
    /**
     * Supprimer un patient
     * @param id l'identifiant du patient
     * @return réponse vide avec statut 204
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un patient", description = "Supprime un patient de la base de données")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Patient supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    public ResponseEntity<Void> deletePatient(
            @Parameter(description = "ID du patient", required = true, example = "1")
            @PathVariable Long id) {
        log.info("REST request pour supprimer le patient avec l'ID: {}", id);
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Rechercher un patient par numéro de sécurité sociale
     * @param nss le numéro de sécurité sociale
     * @return le patient trouvé
     */
    @GetMapping("/search/nss/{nss}")
    @Operation(summary = "Rechercher par numéro de sécurité sociale", description = "Trouve un patient par son numéro de sécurité sociale")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patient trouvé",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class))),
            @ApiResponse(responseCode = "404", description = "Patient non trouvé")
    })
    public ResponseEntity<PatientDTO> getPatientByNumeroSecuriteSociale(
            @Parameter(description = "Numéro de sécurité sociale", required = true, example = "123456789012345")
            @PathVariable String nss) {
        log.info("REST request pour obtenir le patient avec le NSS: {}", nss);
        PatientDTO patient = patientService.findByNumeroSecuriteSociale(nss);
        return ResponseEntity.ok(patient);
    }
    
    /**
     * Rechercher des patients par nom ou prénom
     * @param query le terme de recherche
     * @return la liste des patients correspondants
     */
    @GetMapping("/search")
    @Operation(summary = "Rechercher par nom ou prénom", description = "Recherche des patients par nom ou prénom (recherche insensible à la casse)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients trouvés",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    })
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @Parameter(description = "Terme de recherche", required = true, example = "Ahmed")
            @RequestParam String query) {
        log.info("REST request pour rechercher des patients avec le terme: {}", query);
        List<PatientDTO> patients = patientService.searchPatientsByNameOrFirstname(query);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Rechercher des patients nés entre deux dates
     * @param debut la date de début
     * @param fin la date de fin
     * @return la liste des patients correspondants
     */
    @GetMapping("/search/birthdate")
    @Operation(summary = "Rechercher par période de naissance", description = "Trouve les patients nés entre deux dates")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients trouvés",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    })
    public ResponseEntity<List<PatientDTO>> searchPatientsByBirthDateRange(
            @Parameter(description = "Date de début (format: YYYY-MM-DD)", required = true, example = "1980-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @Parameter(description = "Date de fin (format: YYYY-MM-DD)", required = true, example = "1990-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin) {
        log.info("REST request pour rechercher des patients nés entre {} et {}", debut, fin);
        List<PatientDTO> patients = patientService.findPatientsByBirthDateRange(debut, fin);
        return ResponseEntity.ok(patients);
    }
    
    /**
     * Rechercher des patients par groupe sanguin
     * @param groupe le groupe sanguin
     * @return la liste des patients correspondants
     */
    @GetMapping("/search/bloodgroup/{groupe}")
    @Operation(summary = "Rechercher par groupe sanguin", description = "Trouve les patients ayant un groupe sanguin spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Patients trouvés",
                    content = @Content(schema = @Schema(implementation = PatientDTO.class)))
    })
    public ResponseEntity<List<PatientDTO>> searchPatientsByBloodGroup(
            @Parameter(description = "Groupe sanguin", required = true, example = "A+")
            @PathVariable String groupe) {
        log.info("REST request pour rechercher des patients avec le groupe sanguin: {}", groupe);
        List<PatientDTO> patients = patientService.findPatientsByGroupeSanguin(groupe);
        return ResponseEntity.ok(patients);
    }
} 