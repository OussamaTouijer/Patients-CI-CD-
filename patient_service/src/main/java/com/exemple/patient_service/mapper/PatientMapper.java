package com.exemple.patient_service.mapper;

import com.exemple.patient_service.dto.PatientDTO;
import com.exemple.patient_service.entity.Patient;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PatientMapper {
    
    /**
     * Convertit une entité Patient en DTO
     * @param patient l'entité à convertir
     * @return le DTO correspondant
     */
    public PatientDTO toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        
        return PatientDTO.builder()
                .id(patient.getId())
                .nom(patient.getNom())
                .prenom(patient.getPrenom())
                .dateNaissance(patient.getDateNaissance())
                .telephone(patient.getTelephone())
                .adresse(patient.getAdresse())
                .email(patient.getEmail())
                .genre(patient.getGenre())
                .antecedentsMedicaux(patient.getAntecedentsMedicaux())
                .numeroSecuriteSociale(patient.getNumeroSecuriteSociale())
                .groupeSanguin(patient.getGroupeSanguin())
                .createdAt(patient.getCreatedAt())
                .updatedAt(patient.getUpdatedAt())
                .build();
    }
    
    /**
     * Convertit un DTO en entité Patient
     * @param patientDTO le DTO à convertir
     * @return l'entité correspondante
     */
    public Patient toEntity(PatientDTO patientDTO) {
        if (patientDTO == null) {
            return null;
        }
        
        return Patient.builder()
                .id(patientDTO.getId())
                .nom(patientDTO.getNom())
                .prenom(patientDTO.getPrenom())
                .dateNaissance(patientDTO.getDateNaissance())
                .telephone(patientDTO.getTelephone())
                .adresse(patientDTO.getAdresse())
                .email(patientDTO.getEmail())
                .genre(patientDTO.getGenre())
                .antecedentsMedicaux(patientDTO.getAntecedentsMedicaux())
                .numeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale())
                .groupeSanguin(patientDTO.getGroupeSanguin())
                .build();
    }
    
    /**
     * Met à jour une entité existante avec les données du DTO
     * @param existingPatient l'entité existante à mettre à jour
     * @param patientDTO le DTO contenant les nouvelles données
     */
    public void updateEntityFromDto(Patient existingPatient, PatientDTO patientDTO) {
        if (patientDTO == null) {
            return;
        }
        
        existingPatient.setNom(patientDTO.getNom());
        existingPatient.setPrenom(patientDTO.getPrenom());
        existingPatient.setDateNaissance(patientDTO.getDateNaissance());
        existingPatient.setTelephone(patientDTO.getTelephone());
        existingPatient.setAdresse(patientDTO.getAdresse());
        existingPatient.setEmail(patientDTO.getEmail());
        existingPatient.setGenre(patientDTO.getGenre());
        existingPatient.setAntecedentsMedicaux(patientDTO.getAntecedentsMedicaux());
        existingPatient.setNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale());
        existingPatient.setGroupeSanguin(patientDTO.getGroupeSanguin());
    }
    
    /**
     * Convertit une liste d'entités en liste de DTOs
     * @param patients la liste d'entités
     * @return la liste de DTOs
     */
    public List<PatientDTO> toDtoList(List<Patient> patients) {
        return patients.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
} 