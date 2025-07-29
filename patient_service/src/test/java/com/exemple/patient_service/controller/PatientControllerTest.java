package com.exemple.patient_service.controller;

import com.exemple.patient_service.dto.PatientDTO;
import com.exemple.patient_service.enums.GenreSexe;
import com.exemple.patient_service.exception.BadRequestException;
import com.exemple.patient_service.exception.ResourceNotFoundException;
import com.exemple.patient_service.service.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
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
    void createPatient_ShouldReturnCreatedPatient_WhenValidData() throws Exception {
        // Given
        when(patientService.createPatient(any(PatientDTO.class))).thenReturn(patientDTO);

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Alaoui")))
                .andExpect(jsonPath("$.prenom", is("Ahmed")))
                .andExpect(jsonPath("$.genre", is("HOMME")))
                .andExpect(jsonPath("$.numeroSecuriteSociale", is("123456789012345")));

        verify(patientService).createPatient(any(PatientDTO.class));
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        // Given
        PatientDTO invalidPatient = PatientDTO.builder()
                .nom("")  // nom vide
                .prenom("Ahmed")
                .build();

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());

        verify(patientService, never()).createPatient(any(PatientDTO.class));
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenNSSAlreadyExists() throws Exception {
        // Given
        when(patientService.createPatient(any(PatientDTO.class)))
                .thenThrow(new BadRequestException("Un patient avec ce numéro de sécurité sociale existe déjà"));

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Un patient avec ce numéro de sécurité sociale existe déjà")));

        verify(patientService).createPatient(any(PatientDTO.class));
    }

    @Test
    void getPatientById_ShouldReturnPatient_WhenPatientExists() throws Exception {
        // Given
        when(patientService.getPatientById(1L)).thenReturn(patientDTO);

        // When & Then
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Alaoui")))
                .andExpect(jsonPath("$.prenom", is("Ahmed")));

        verify(patientService).getPatientById(1L);
    }

    @Test
    void getPatientById_ShouldReturnNotFound_WhenPatientNotExists() throws Exception {
        // Given
        when(patientService.getPatientById(1L))
                .thenThrow(new ResourceNotFoundException("Patient", "id", 1L));

        // When & Then
        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isNotFound());

        verify(patientService).getPatientById(1L);
    }

    @Test
    void getAllPatients_ShouldReturnAllPatients() throws Exception {
        // Given
        List<PatientDTO> patients = Arrays.asList(patientDTO, patientDTO);
        when(patientService.getAllPatients()).thenReturn(patients);

        // When & Then
        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].nom", is("Alaoui")))
                .andExpect(jsonPath("$[1].nom", is("Alaoui")));

        verify(patientService).getAllPatients();
    }

    @Test
    void updatePatient_ShouldReturnUpdatedPatient_WhenValidData() throws Exception {
        // Given
        PatientDTO updatedPatient = PatientDTO.builder()
                .id(1L)
                .nom("Bennani")
                .prenom("Fatima")
                .dateNaissance(LocalDate.of(1990, 3, 20))
                .telephone("0687654321")
                .adresse("456 Avenue Hassan II, Rabat")
                .email("fatima.bennani@gmail.com")
                .genre(GenreSexe.FEMME)
                .numeroSecuriteSociale("987654321098765")
                .groupeSanguin("B+")
                .build();

        when(patientService.updatePatient(eq(1L), any(PatientDTO.class))).thenReturn(updatedPatient);

        // When & Then
        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.nom", is("Bennani")))
                .andExpect(jsonPath("$.prenom", is("Fatima")))
                .andExpect(jsonPath("$.genre", is("FEMME")));

        verify(patientService).updatePatient(eq(1L), any(PatientDTO.class));
    }

    @Test
    void updatePatient_ShouldReturnNotFound_WhenPatientNotExists() throws Exception {
        // Given
        when(patientService.updatePatient(eq(1L), any(PatientDTO.class)))
                .thenThrow(new ResourceNotFoundException("Patient", "id", 1L));

        // When & Then
        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patientDTO)))
                .andExpect(status().isNotFound());

        verify(patientService).updatePatient(eq(1L), any(PatientDTO.class));
    }

    @Test
    void deletePatient_ShouldReturnNoContent_WhenPatientExists() throws Exception {
        // Given
        doNothing().when(patientService).deletePatient(1L);

        // When & Then
        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNoContent());

        verify(patientService).deletePatient(1L);
    }

    @Test
    void deletePatient_ShouldReturnNotFound_WhenPatientNotExists() throws Exception {
        // Given
        doThrow(new ResourceNotFoundException("Patient", "id", 1L))
                .when(patientService).deletePatient(1L);

        // When & Then
        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNotFound());

        verify(patientService).deletePatient(1L);
    }

    @Test
    void getPatientByNumeroSecuriteSociale_ShouldReturnPatient_WhenPatientExists() throws Exception {
        // Given
        String nss = "123456789012345";
        when(patientService.findByNumeroSecuriteSociale(nss)).thenReturn(patientDTO);

        // When & Then
        mockMvc.perform(get("/patients/search/nss/" + nss))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroSecuriteSociale", is(nss)))
                .andExpect(jsonPath("$.nom", is("Alaoui")));

        verify(patientService).findByNumeroSecuriteSociale(nss);
    }

    @Test
    void getPatientByNumeroSecuriteSociale_ShouldReturnNotFound_WhenPatientNotExists() throws Exception {
        // Given
        String nss = "123456789012345";
        when(patientService.findByNumeroSecuriteSociale(nss))
                .thenThrow(new ResourceNotFoundException("Patient", "numéro de sécurité sociale", nss));

        // When & Then
        mockMvc.perform(get("/patients/search/nss/" + nss))
                .andExpect(status().isNotFound());

        verify(patientService).findByNumeroSecuriteSociale(nss);
    }

    @Test
    void searchPatients_ShouldReturnPatients_WhenQueryProvided() throws Exception {
        // Given
        String query = "Ahmed";
        List<PatientDTO> patients = Arrays.asList(patientDTO);
        when(patientService.searchPatientsByNameOrFirstname(query)).thenReturn(patients);

        // When & Then
        mockMvc.perform(get("/patients/search")
                        .param("query", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].prenom", is("Ahmed")));

        verify(patientService).searchPatientsByNameOrFirstname(query);
    }

    @Test
    void searchPatientsByBirthDateRange_ShouldReturnPatients() throws Exception {
        // Given
        LocalDate debut = LocalDate.of(1980, 1, 1);
        LocalDate fin = LocalDate.of(1990, 12, 31);
        List<PatientDTO> patients = Arrays.asList(patientDTO);
        when(patientService.findPatientsByBirthDateRange(debut, fin)).thenReturn(patients);

        // When & Then
        mockMvc.perform(get("/patients/search/birthdate")
                        .param("debut", "1980-01-01")
                        .param("fin", "1990-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].dateNaissance", is("1985-06-15")));

        verify(patientService).findPatientsByBirthDateRange(debut, fin);
    }

    @Test
    void searchPatientsByBloodGroup_ShouldReturnPatients() throws Exception {
        // Given
        String groupe = "A+";
        List<PatientDTO> patients = Arrays.asList(patientDTO);
        when(patientService.findPatientsByGroupeSanguin(groupe)).thenReturn(patients);

        // When & Then
        mockMvc.perform(get("/patients/search/bloodgroup/" + groupe))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].groupeSanguin", is("A+")));

        verify(patientService).findPatientsByGroupeSanguin(groupe);
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenMissingRequiredFields() throws Exception {
        // Given
        PatientDTO invalidPatient = PatientDTO.builder()
                .nom("Alaoui")
                // missing prenom, dateNaissance, genre
                .build();

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());

        verify(patientService, never()).createPatient(any(PatientDTO.class));
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenInvalidEmail() throws Exception {
        // Given
        PatientDTO invalidPatient = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .email("invalid-email")  // email invalide
                .build();

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());

        verify(patientService, never()).createPatient(any(PatientDTO.class));
    }

    @Test
    void createPatient_ShouldReturnBadRequest_WhenInvalidTelephone() throws Exception {
        // Given
        PatientDTO invalidPatient = PatientDTO.builder()
                .nom("Alaoui")
                .prenom("Ahmed")
                .dateNaissance(LocalDate.of(1985, 6, 15))
                .genre(GenreSexe.HOMME)
                .adresse("123 Rue Test")
                .telephone("123")  // telephone invalide
                .build();

        // When & Then
        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest());

        verify(patientService, never()).createPatient(any(PatientDTO.class));
    }
}