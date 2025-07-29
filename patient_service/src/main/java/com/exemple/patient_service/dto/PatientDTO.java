package com.exemple.patient_service.dto;

import com.exemple.patient_service.enums.GenreSexe;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Données de transfert pour un patient")
public class PatientDTO {
    
    @Schema(description = "Identifiant unique du patient", example = "1")
    private Long id;
    
    @Schema(description = "Nom de famille du patient", example = "Alaoui")
    @NotBlank(message = "Le nom est obligatoire")
    @Size(max = 100, message = "Le nom ne doit pas dépasser 100 caractères")
    private String nom;
    
    @Schema(description = "Prénom du patient", example = "Ahmed")
    @NotBlank(message = "Le prénom est obligatoire")
    @Size(max = 100, message = "Le prénom ne doit pas dépasser 100 caractères")
    private String prenom;
    
    @Schema(description = "Date de naissance du patient", example = "1985-06-15")
    @NotNull(message = "La date de naissance est obligatoire")
    @Past(message = "La date de naissance doit être dans le passé")
    private LocalDate dateNaissance;
    
    @Schema(description = "Numéro de téléphone du patient", example = "+212612345678")
    @Pattern(regexp = "^[0-9]{10}$", message = "Le numéro de téléphone doit contenir 10 chiffres")
    private String telephone;
    
    @Schema(description = "Adresse complète du patient", example = "123 Rue Mohammed V, 20000 Casablanca, Maroc")
    @NotBlank(message = "L'adresse est obligatoire")
    @Size(max = 200, message = "L'adresse ne doit pas dépasser 200 caractères")
    private String adresse;
    
    @Schema(description = "Adresse email du patient", example = "ahmed.alaoui@gmail.com")
    @Email(message = "Le format de l'email est invalide")
    @Size(max = 200, message = "L'email ne doit pas dépasser 200 caractères")
    private String email;
    
    @Schema(description = "Genre du patient", example = "HOMME")
    @NotNull(message = "Le genre est obligatoire")
    private GenreSexe genre;
    
    @Schema(description = "Antécédents médicaux du patient", example = "Diabète de type 2")
    @Size(max = 200, message = "Les antécédents médicaux ne doivent pas dépasser 200 caractères")
    private String antecedentsMedicaux;
    
    @Schema(description = "Numéro de sécurité sociale du patient", example = "123456789012345")
    @Pattern(regexp = "^[0-9]{15}$", message = "Le numéro de sécurité sociale doit contenir 15 chiffres")
    private String numeroSecuriteSociale;
    
    @Schema(description = "Groupe sanguin du patient", example = "A+")
    @Pattern(regexp = "^(A|B|AB|O)[+-]$", message = "Le groupe sanguin doit être au format valide (ex: A+, O-, AB+)")
    private String groupeSanguin;
    
    @Schema(description = "Date de création du patient", example = "2024-01-15")
    private LocalDate createdAt;
    
    @Schema(description = "Date de dernière modification du patient", example = "2024-01-20")
    private LocalDate updatedAt;
} 