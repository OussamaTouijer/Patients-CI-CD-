package com.exemple.patient_service.service.impl;

import com.exemple.patient_service.dto.PatientDTO;
import com.exemple.patient_service.entity.Patient;
import com.exemple.patient_service.enums.GenreSexe;
import com.exemple.patient_service.exception.BadRequestException;
import com.exemple.patient_service.exception.ResourceNotFoundException;
import com.exemple.patient_service.mapper.PatientMapper;
import com.exemple.patient_service.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientDTO patientDTO;

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
                .numeroSecuriteSociale("123456789012345")
                .groupeSanguin("A+")
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .build();
    }

    @Test
    void createPatient_ShouldCreatePatient_WhenValidData() {
        // Given
        when(patientRepository.existsByNumeroSecuriteSociale(anyString())).thenReturn(false);
        when(patientMapper.toEntity(any(PatientDTO.class))).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toDto(any(Patient.class))).thenReturn(patientDTO);

        // When
        PatientDTO result = patientService.createPatient(patientDTO);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNom()).isEqualTo("Alaoui");
        assertThat(result.getPrenom()).isEqualTo("Ahmed");
        verify(patientRepository).existsByNumeroSecuriteSociale("123456789012345");
        verify(patientRepository).save(any(Patient.class));
        verify(patientMapper).toEntity(patientDTO);
        verify(patientMapper).toDto(patient);
    }

    @Test
    void createPatient_ShouldThrowBadRequestException_WhenNSSAlreadyExists() {
        // Given
        when(patientRepository.existsByNumeroSecuriteSociale(anyString())).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> patientService.createPatient(patientDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Un patient avec ce numéro de sécurité sociale existe déjà");

        verify(patientRepository).existsByNumeroSecuriteSociale("123456789012345");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenPatientExists() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(any(Patient.class))).thenReturn(patientDTO);

        // When
        PatientDTO result = patientService.getPatientById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getNom()).isEqualTo("Alaoui");
        verify(patientRepository).findById(1L);
        verify(patientMapper).toDto(patient);
    }

    @Test
    void getPatientById_ShouldThrowResourceNotFoundException_WhenPatientNotExists() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.getPatientById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient")
                .hasMessageContaining("id")
                .hasMessageContaining("1");

        verify(patientRepository).findById(1L);
        verify(patientMapper, never()).toDto(any(Patient.class));
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() {
        // Given
        List<Patient> patients = Arrays.asList(patient, patient);
        List<PatientDTO> patientDTOs = Arrays.asList(patientDTO, patientDTO);
        when(patientRepository.findAll()).thenReturn(patients);
        when(patientMapper.toDtoList(patients)).thenReturn(patientDTOs);

        // When
        List<PatientDTO> result = patientService.getAllPatients();

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(2);
        verify(patientRepository).findAll();
        verify(patientMapper).toDtoList(patients);
    }

    @Test
    void updatePatient_ShouldUpdatePatient_WhenValidData() {
        // Given
        PatientDTO updateDTO = PatientDTO.builder()
                .nom("Bennani")
                .prenom("Fatima")
                .dateNaissance(LocalDate.of(1990, 3, 20))
                .numeroSecuriteSociale("987654321098765")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByNumeroSecuriteSociale("987654321098765")).thenReturn(false);
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toDto(any(Patient.class))).thenReturn(patientDTO);

        // When
        PatientDTO result = patientService.updatePatient(1L, updateDTO);

        // Then
        assertThat(result).isNotNull();
        verify(patientRepository).findById(1L);
        verify(patientRepository).existsByNumeroSecuriteSociale("987654321098765");
        verify(patientMapper).updateEntityFromDto(patient, updateDTO);
        verify(patientRepository).save(patient);
        verify(patientMapper).toDto(patient);
    }

    @Test
    void updatePatient_ShouldThrowResourceNotFoundException_WhenPatientNotExists() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.updatePatient(1L, patientDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient")
                .hasMessageContaining("id")
                .hasMessageContaining("1");

        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void updatePatient_ShouldThrowBadRequestException_WhenNSSAlreadyExistsForOtherPatient() {
        // Given
        PatientDTO updateDTO = PatientDTO.builder()
                .numeroSecuriteSociale("987654321098765")
                .build();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.existsByNumeroSecuriteSociale("987654321098765")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> patientService.updatePatient(1L, updateDTO))
                .isInstanceOf(BadRequestException.class)
                .hasMessage("Un patient avec ce numéro de sécurité sociale existe déjà");

        verify(patientRepository).findById(1L);
        verify(patientRepository).existsByNumeroSecuriteSociale("987654321098765");
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void deletePatient_ShouldDeletePatient_WhenPatientExists() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // When
        patientService.deletePatient(1L);

        // Then
        verify(patientRepository).findById(1L);
        verify(patientRepository).delete(patient);
    }

    @Test
    void deletePatient_ShouldThrowResourceNotFoundException_WhenPatientNotExists() {
        // Given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.deletePatient(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient")
                .hasMessageContaining("id")
                .hasMessageContaining("1");

        verify(patientRepository).findById(1L);
        verify(patientRepository, never()).delete(any(Patient.class));
    }

    @Test
    void findByNumeroSecuriteSociale_ShouldReturnPatient_WhenPatientExists() {
        // Given
        String nss = "123456789012345";
        when(patientRepository.findByNumeroSecuriteSociale(nss)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(any(Patient.class))).thenReturn(patientDTO);

        // When
        PatientDTO result = patientService.findByNumeroSecuriteSociale(nss);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getNumeroSecuriteSociale()).isEqualTo(nss);
        verify(patientRepository).findByNumeroSecuriteSociale(nss);
        verify(patientMapper).toDto(patient);
    }

    @Test
    void findByNumeroSecuriteSociale_ShouldThrowResourceNotFoundException_WhenPatientNotExists() {
        // Given
        String nss = "123456789012345";
        when(patientRepository.findByNumeroSecuriteSociale(nss)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> patientService.findByNumeroSecuriteSociale(nss))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Patient")
                .hasMessageContaining("numéro de sécurité sociale")
                .hasMessageContaining(nss);

        verify(patientRepository).findByNumeroSecuriteSociale(nss);
        verify(patientMapper, never()).toDto(any(Patient.class));
    }

    @Test
    void searchPatientsByNameOrFirstname_ShouldReturnPatients() {
        // Given
        String query = "Ahmed";
        List<Patient> patients = Arrays.asList(patient);
        List<PatientDTO> patientDTOs = Arrays.asList(patientDTO);
        when(patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query))
                .thenReturn(patients);
        when(patientMapper.toDtoList(patients)).thenReturn(patientDTOs);

        // When
        List<PatientDTO> result = patientService.searchPatientsByNameOrFirstname(query);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(patientRepository).findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query);
        verify(patientMapper).toDtoList(patients);
    }

    @Test
    void findPatientsByBirthDateRange_ShouldReturnPatients() {
        // Given
        LocalDate debut = LocalDate.of(1980, 1, 1);
        LocalDate fin = LocalDate.of(1990, 12, 31);
        List<Patient> patients = Arrays.asList(patient);
        List<PatientDTO> patientDTOs = Arrays.asList(patientDTO);
        when(patientRepository.findByDateNaissanceBetween(debut, fin)).thenReturn(patients);
        when(patientMapper.toDtoList(patients)).thenReturn(patientDTOs);

        // When
        List<PatientDTO> result = patientService.findPatientsByBirthDateRange(debut, fin);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(patientRepository).findByDateNaissanceBetween(debut, fin);
        verify(patientMapper).toDtoList(patients);
    }

    @Test
    void findPatientsByGroupeSanguin_ShouldReturnPatients() {
        // Given
        String groupeSanguin = "A+";
        List<Patient> patients = Arrays.asList(patient);
        List<PatientDTO> patientDTOs = Arrays.asList(patientDTO);
        when(patientRepository.findByGroupeSanguin(groupeSanguin)).thenReturn(patients);
        when(patientMapper.toDtoList(patients)).thenReturn(patientDTOs);

        // When
        List<PatientDTO> result = patientService.findPatientsByGroupeSanguin(groupeSanguin);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).hasSize(1);
        verify(patientRepository).findByGroupeSanguin(groupeSanguin);
        verify(patientMapper).toDtoList(patients);
    }
}