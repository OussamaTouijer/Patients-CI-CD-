package com.exemple.patient_service.dto;

import com.exemple.patient_service.enums.GenreSexe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDate;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class PatientDTOTest {

    private Validator validator;
    private PatientDTO validPatientDTO;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        validPatientDTO = PatientDTO.builder()
                .id(1L)
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .telephone("0612345678")
                .adresse("123 Rue Mohammed V, Casablanca")
                .email("ahmed.alaoui@gmail.com")
                .genre(GenreSexe.HOMME)
                .antecedentsMedicaux("Diabète")
                .numeroSecuriteSociale("123456789012345")
                .groupeSanguin("A+")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    @Test
    void builder_ShouldCreatePatientDTOWithAllFields() {
        // Then
        assertThat(validPatientDTO).isNotNull();
        assertThat(validPatientDTO.getId()).isEqualTo(1L);
        assertThat(validPatientDTO.getNom()).isEqualTo("Alaoui");
        assertThat(validPatientDTO.getPrenom()).isEqualTo("Ahmed");
        assertThat(validPatientDTO.getDateNaissance()).isEqualTo(LocalDate.of(1985, 6, 15));
        assertThat(validPatientDTO.getTelephone()).isEqualTo("0612345678");
        assertThat(validPatientDTO.getAdresse()).isEqualTo("123 Rue Mohammed V, Casablanca");
        assertThat(validPatientDTO.getEmail()).isEqualTo("ahmed.alaoui@gmail.com");
        assertThat(validPatientDTO.getGenre()).isEqualTo(GenreSexe.HOMME);
        assertThat(validPatientDTO.getAntecedentsMedicaux()).isEqualTo("Diabète");
        assertThat(validPatientDTO.getNumeroSecuriteSociale()).isEqualTo("123456789012345");
        assertThat(validPatientDTO.getGroupeSanguin()).isEqualTo("A+");
    }

    @Test
    void validation_ShouldPass_WhenAllFieldsAreValid() {
        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(validPatientDTO);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void validation_ShouldFail_WhenNomIsBlank() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("nom") && 
                          v.getMessage().contains("obligatoire")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenPrenomIsBlank() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("prenom") && 
                          v.getMessage().contains("obligatoire")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenDateNaissanceIsNull() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(null)
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateNaissance") && 
                          v.getMessage().contains("obligatoire")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenDateNaissanceIsInFuture() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.now().plusDays(1))
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("dateNaissance") && 
                          v.getMessage().contains("passé")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenTelephoneIsInvalid() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .telephone("123")
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("telephone") && 
                          v.getMessage().contains("10 chiffres")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenAdresseIsBlank() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .adresse("")
                .genre(GenreSexe.HOMME)
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("adresse") && 
                          v.getMessage().contains("obligatoire")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenEmailIsInvalid() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .email("invalid-email")
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("email") && 
                          v.getMessage().contains("invalide")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenGenreIsNull() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .genre(null)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("genre") && 
                          v.getMessage().contains("obligatoire")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenNumeroSecuriteSocialeIsInvalid() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .numeroSecuriteSociale("123")
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("numeroSecuriteSociale") && 
                          v.getMessage().contains("15 chiffres")))
                .isTrue();
    }

    @Test
    void validation_ShouldFail_WhenGroupeSanguinIsInvalid() {
        // Given
        PatientDTO patientDTO = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .groupeSanguin("INVALID")
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .build();

        // When
        Set<ConstraintViolation<PatientDTO>> violations = validator.validate(patientDTO);

        // Then
        assertThat(violations).isNotEmpty();
        assertThat(violations.stream()
                .anyMatch(v -> v.getPropertyPath().toString().equals("groupeSanguin") && 
                          v.getMessage().contains("format valide")))
                .isTrue();
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyDTO() {
        // When
        PatientDTO emptyDTO = new PatientDTO();

        // Then
        assertThat(emptyDTO).isNotNull();
        assertThat(emptyDTO.getId()).isNull();
        assertThat(emptyDTO.getNom()).isNull();
        assertThat(emptyDTO.getPrenom()).isNull();
    }

    @Test
    void allArgsConstructor_ShouldCreateDTOWithAllFields() {
        // Given
        LocalDate birthDate = LocalDate.of(1990, 3, 20);
        LocalDate createdAt = LocalDate.now();
        LocalDate updatedAt = LocalDate.now();

        // When
        PatientDTO dto = new PatientDTO(2L, "Bennani", "Fatima", birthDate, "0687654321",
                "456 Avenue Hassan II, Rabat", "fatima.bennani@gmail.com", GenreSexe.FEMME,
                "Hypertension", "987654321098765", "B+", createdAt, updatedAt);

        // Then
        assertThat(dto.getId()).isEqualTo(2L);
        assertThat(dto.getNom()).isEqualTo("Bennani");
        assertThat(dto.getPrenom()).isEqualTo("Fatima");
        assertThat(dto.getDateNaissance()).isEqualTo(birthDate);
        assertThat(dto.getTelephone()).isEqualTo("0687654321");
        assertThat(dto.getAdresse()).isEqualTo("456 Avenue Hassan II, Rabat");
        assertThat(dto.getEmail()).isEqualTo("fatima.bennani@gmail.com");
        assertThat(dto.getGenre()).isEqualTo(GenreSexe.FEMME);
        assertThat(dto.getAntecedentsMedicaux()).isEqualTo("Hypertension");
        assertThat(dto.getNumeroSecuriteSociale()).isEqualTo("987654321098765");
        assertThat(dto.getGroupeSanguin()).isEqualTo("B+");
        assertThat(dto.getCreatedAt()).isEqualTo(createdAt);
        assertThat(dto.getUpdatedAt()).isEqualTo(updatedAt);
    }
}