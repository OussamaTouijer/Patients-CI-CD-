package com.exemple.patient_service.mapper;

import com.exemple.patient_service.dto.PatientDTO;
import com.exemple.patient_service.entity.Patient;
import com.exemple.patient_service.enums.GenreSexe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PatientMapperTest {

    private PatientMapper patientMapper;
    private Patient patient;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patientMapper = new PatientMapper();

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
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();

        patientDTO = PatientDTO.builder()
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
    void toDto_ShouldConvertPatientToDTO_WhenPatientIsValid() {
        // When
        PatientDTO result = patientMapper.toDto(patient);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(patient.getId());
        assertThat(result.getNom()).isEqualTo(patient.getNom());
        assertThat(result.getPrenom()).isEqualTo(patient.getPrenom());
        assertThat(result.getDateNaissance()).isEqualTo(patient.getDateNaissance());
        assertThat(result.getTelephone()).isEqualTo(patient.getTelephone());
        assertThat(result.getAdresse()).isEqualTo(patient.getAdresse());
        assertThat(result.getEmail()).isEqualTo(patient.getEmail());
        assertThat(result.getGenre()).isEqualTo(patient.getGenre());
        assertThat(result.getAntecedentsMedicaux()).isEqualTo(patient.getAntecedentsMedicaux());
        assertThat(result.getNumeroSecuriteSociale()).isEqualTo(patient.getNumeroSecuriteSociale());
        assertThat(result.getGroupeSanguin()).isEqualTo(patient.getGroupeSanguin());
        assertThat(result.getCreatedAt()).isEqualTo(patient.getCreatedAt());
        assertThat(result.getUpdatedAt()).isEqualTo(patient.getUpdatedAt());
    }

    @Test
    void toDto_ShouldReturnNull_WhenPatientIsNull() {
        // When
        PatientDTO result = patientMapper.toDto(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void toEntity_ShouldConvertDTOToPatient_WhenDTOIsValid() {
        // When
        Patient result = patientMapper.toEntity(patientDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(patientDTO.getId());
        assertThat(result.getNom()).isEqualTo(patientDTO.getNom());
        assertThat(result.getPrenom()).isEqualTo(patientDTO.getPrenom());
        assertThat(result.getDateNaissance()).isEqualTo(patientDTO.getDateNaissance());
        assertThat(result.getTelephone()).isEqualTo(patientDTO.getTelephone());
        assertThat(result.getAdresse()).isEqualTo(patientDTO.getAdresse());
        assertThat(result.getEmail()).isEqualTo(patientDTO.getEmail());
        assertThat(result.getGenre()).isEqualTo(patientDTO.getGenre());
        assertThat(result.getAntecedentsMedicaux()).isEqualTo(patientDTO.getAntecedentsMedicaux());
        assertThat(result.getNumeroSecuriteSociale()).isEqualTo(patientDTO.getNumeroSecuriteSociale());
        assertThat(result.getGroupeSanguin()).isEqualTo(patientDTO.getGroupeSanguin());
    }

    @Test
    void toEntity_ShouldReturnNull_WhenDTOIsNull() {
        // When
        Patient result = patientMapper.toEntity(null);

        // Then
        assertThat(result).isNull();
    }

    @Test
    void updateEntityFromDto_ShouldUpdateExistingPatient_WhenDTOIsValid() {
        // Given
        Patient existingPatient = Patient.builder()
                .id(1L)
                .nom("OldNom")
                .prenom("OldPrenom")
                .dateNaissance(LocalDate.of(1980, 1, 1))
                .telephone("0600000000")
                .adresse("Old Address")
                .email("old@email.com")
                .genre(GenreSexe.FEMME)
                .antecedentsMedicaux("Old medical history")
                .numeroSecuriteSociale("000000000000000")
                .groupeSanguin("O-")
                .build();

        PatientDTO updateDTO = PatientDTO.builder()
                .nom("NewNom")
                .prenom("NewPrenom")
                .dateNaissance(LocalDate.of(1990, 12, 31))
                .telephone("0699999999")
                .adresse("New Address")
                .email("new@email.com")
                .genre(GenreSexe.HOMME)
                .antecedentsMedicaux("New medical history")
                .numeroSecuriteSociale("111111111111111")
                .groupeSanguin("AB+")
                .build();

        // When
        patientMapper.updateEntityFromDto(existingPatient, updateDTO);

        // Then
        assertThat(existingPatient.getId()).isEqualTo(1L); // ID should not change
        assertThat(existingPatient.getNom()).isEqualTo("NewNom");
        assertThat(existingPatient.getPrenom()).isEqualTo("NewPrenom");
        assertThat(existingPatient.getDateNaissance()).isEqualTo(LocalDate.of(1990, 12, 31));
        assertThat(existingPatient.getTelephone()).isEqualTo("0699999999");
        assertThat(existingPatient.getAdresse()).isEqualTo("New Address");
        assertThat(existingPatient.getEmail()).isEqualTo("new@email.com");
        assertThat(existingPatient.getGenre()).isEqualTo(GenreSexe.HOMME);
        assertThat(existingPatient.getAntecedentsMedicaux()).isEqualTo("New medical history");
        assertThat(existingPatient.getNumeroSecuriteSociale()).isEqualTo("111111111111111");
        assertThat(existingPatient.getGroupeSanguin()).isEqualTo("AB+");
    }

    @Test
    void updateEntityFromDto_ShouldNotChangeEntity_WhenDTOIsNull() {
        // Given
        Patient originalPatient = Patient.builder()
                .id(1L)
                .nom("OriginalNom")
                .prenom("OriginalPrenom")
                .build();

        // When
        patientMapper.updateEntityFromDto(originalPatient, null);

        // Then
        assertThat(originalPatient.getNom()).isEqualTo("OriginalNom");
        assertThat(originalPatient.getPrenom()).isEqualTo("OriginalPrenom");
    }

    @Test
    void toDtoList_ShouldConvertListOfPatientsToListOfDTOs() {
        // Given
        Patient patient2 = Patient.builder()
                .id(2L)
                .nom("Bennani")
                .prenom("Fatima")
                .dateNaissance(LocalDate.of(1990, 3, 20))
                .genre(GenreSexe.FEMME)
                .adresse("456 Avenue Hassan II, Rabat")
                .build();

        List<Patient> patients = Arrays.asList(patient, patient2);

        // When
        List<PatientDTO> result = patientMapper.toDtoList(patients);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getNom()).isEqualTo("Alaoui");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getNom()).isEqualTo("Bennani");
    }

    @Test
    void toDtoList_ShouldReturnEmptyList_WhenInputIsEmpty() {
        // Given
        List<Patient> emptyList = Arrays.asList();

        // When
        List<PatientDTO> result = patientMapper.toDtoList(emptyList);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).isEmpty();
    }
}