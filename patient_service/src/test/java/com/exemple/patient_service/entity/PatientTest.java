package com.exemple.patient_service.entity;

import com.exemple.patient_service.enums.GenreSexe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class PatientTest {

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = Patient.builder()
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
                .build();
    }

    @Test
    void builder_ShouldCreatePatientWithAllFields() {
        // Then
        assertThat(patient).isNotNull();
        assertThat(patient.getId()).isEqualTo(1L);
        assertThat(patient.getNom()).isEqualTo("Alaoui");
        assertThat(patient.getPrenom()).isEqualTo("Ahmed");
        assertThat(patient.getDateNaissance()).isEqualTo(LocalDate.of(1985, 6, 15));
        assertThat(patient.getTelephone()).isEqualTo("0612345678");
        assertThat(patient.getAdresse()).isEqualTo("123 Rue Mohammed V, Casablanca");
        assertThat(patient.getEmail()).isEqualTo("ahmed.alaoui@gmail.com");
        assertThat(patient.getGenre()).isEqualTo(GenreSexe.HOMME);
        assertThat(patient.getAntecedentsMedicaux()).isEqualTo("Diabète");
        assertThat(patient.getNumeroSecuriteSociale()).isEqualTo("123456789012345");
        assertThat(patient.getGroupeSanguin()).isEqualTo("A+");
    }

    @Test
    void noArgsConstructor_ShouldCreateEmptyPatient() {
        // When
        Patient emptyPatient = new Patient();

        // Then
        assertThat(emptyPatient).isNotNull();
        assertThat(emptyPatient.getId()).isNull();
        assertThat(emptyPatient.getNom()).isNull();
        assertThat(emptyPatient.getPrenom()).isNull();
    }

    @Test
    void allArgsConstructor_ShouldCreatePatientWithAllFields() {
        // Given
        LocalDate birthDate = LocalDate.of(1990, 3, 20);
        LocalDate createdAt = LocalDate.now();
        LocalDate updatedAt = LocalDate.now();

        // When
        Patient patient = new Patient(2L, "Bennani", "Fatima", birthDate, "0687654321",
                "456 Avenue Hassan II, Rabat", "fatima.bennani@gmail.com", GenreSexe.FEMME,
                "Hypertension", "987654321098765", "B+", createdAt, updatedAt);

        // Then
        assertThat(patient.getId()).isEqualTo(2L);
        assertThat(patient.getNom()).isEqualTo("Bennani");
        assertThat(patient.getPrenom()).isEqualTo("Fatima");
        assertThat(patient.getDateNaissance()).isEqualTo(birthDate);
        assertThat(patient.getTelephone()).isEqualTo("0687654321");
        assertThat(patient.getAdresse()).isEqualTo("456 Avenue Hassan II, Rabat");
        assertThat(patient.getEmail()).isEqualTo("fatima.bennani@gmail.com");
        assertThat(patient.getGenre()).isEqualTo(GenreSexe.FEMME);
        assertThat(patient.getAntecedentsMedicaux()).isEqualTo("Hypertension");
        assertThat(patient.getNumeroSecuriteSociale()).isEqualTo("987654321098765");
        assertThat(patient.getGroupeSanguin()).isEqualTo("B+");
        assertThat(patient.getCreatedAt()).isEqualTo(createdAt);
        assertThat(patient.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Given
        Patient patient = new Patient();
        LocalDate birthDate = LocalDate.of(1995, 12, 10);

        // When
        patient.setId(3L);
        patient.setNom("El Amrani");
        patient.setPrenom("Omar");
        patient.setDateNaissance(birthDate);
        patient.setTelephone("0654321987");
        patient.setAdresse("789 Boulevard Zerktouni, Marrakech");
        patient.setEmail("omar.elamrani@gmail.com");
        patient.setGenre(GenreSexe.HOMME);
        patient.setAntecedentsMedicaux("Asthme");
        patient.setNumeroSecuriteSociale("456789123456789");
        patient.setGroupeSanguin("O+");

        // Then
        assertThat(patient.getId()).isEqualTo(3L);
        assertThat(patient.getNom()).isEqualTo("El Amrani");
        assertThat(patient.getPrenom()).isEqualTo("Omar");
        assertThat(patient.getDateNaissance()).isEqualTo(birthDate);
        assertThat(patient.getTelephone()).isEqualTo("0654321987");
        assertThat(patient.getAdresse()).isEqualTo("789 Boulevard Zerktouni, Marrakech");
        assertThat(patient.getEmail()).isEqualTo("omar.elamrani@gmail.com");
        assertThat(patient.getGenre()).isEqualTo(GenreSexe.HOMME);
        assertThat(patient.getAntecedentsMedicaux()).isEqualTo("Asthme");
        assertThat(patient.getNumeroSecuriteSociale()).isEqualTo("456789123456789");
        assertThat(patient.getGroupeSanguin()).isEqualTo("O+");
    }

    @Test
    void onCreate_ShouldSetCreatedAtAndUpdatedAt() {
        // Given
        Patient newPatient = new Patient();
        LocalDate beforeCreate = LocalDate.now();

        // When
        newPatient.onCreate();

        // Then
        LocalDate afterCreate = LocalDate.now();
        assertThat(newPatient.getCreatedAt()).isNotNull();
        assertThat(newPatient.getUpdatedAt()).isNotNull();
        assertThat(newPatient.getCreatedAt()).isBetween(beforeCreate, afterCreate);
        assertThat(newPatient.getUpdatedAt()).isBetween(beforeCreate, afterCreate);
        assertThat(newPatient.getCreatedAt()).isEqualTo(newPatient.getUpdatedAt());
    }

    @Test
    void onUpdate_ShouldUpdateOnlyUpdatedAt() {
        // Given
        Patient existingPatient = new Patient();
        existingPatient.onCreate(); // Set initial dates
        LocalDate originalCreatedAt = existingPatient.getCreatedAt();
        
        // Wait a bit to ensure different timestamps
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        LocalDate beforeUpdate = LocalDate.now();

        // When
        existingPatient.onUpdate();

        // Then
        LocalDate afterUpdate = LocalDate.now();
        assertThat(existingPatient.getCreatedAt()).isEqualTo(originalCreatedAt); // Should not change
        assertThat(existingPatient.getUpdatedAt()).isNotNull();
        assertThat(existingPatient.getUpdatedAt()).isBetween(beforeUpdate, afterUpdate);
    }
}