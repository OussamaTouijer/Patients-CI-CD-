package com.exemple.patient_service.service.impl;

import com.exemple.patient_service.dto.PatientDTO;
import com.exemple.patient_service.entity.Patient;
import com.exemple.patient_service.exception.BadRequestException;
import com.exemple.patient_service.exception.ResourceNotFoundException;
import com.exemple.patient_service.mapper.PatientMapper;
import com.exemple.patient_service.repository.PatientRepository;
import com.exemple.patient_service.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PatientServiceImpl implements PatientService {
    
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    
    @Override
    @Transactional
    public PatientDTO createPatient(PatientDTO patientDTO) {
        log.info("Création d'un nouveau patient");
        
        // Vérifier si le numéro de sécurité sociale existe déjà
        if (patientDTO.getNumeroSecuriteSociale() != null && 
                patientRepository.existsByNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale())) {
            throw new BadRequestException("Un patient avec ce numéro de sécurité sociale existe déjà");
        }
        
        Patient patient = patientMapper.toEntity(patientDTO);
        Patient savedPatient = patientRepository.save(patient);
        
        log.info("Patient créé avec l'ID: {}", savedPatient.getId());
        return patientMapper.toDto(savedPatient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PatientDTO getPatientById(Long id) {
        log.info("Recherche du patient avec l'ID: {}", id);
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        
        return patientMapper.toDto(patient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDTO> getAllPatients() {
        log.info("Récupération de tous les patients");
        
        List<Patient> patients = patientRepository.findAll();
        return patientMapper.toDtoList(patients);
    }
    
    @Override
    @Transactional
    public PatientDTO updatePatient(Long id, PatientDTO patientDTO) {
        log.info("Mise à jour du patient avec l'ID: {}", id);
        
        Patient existingPatient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        
        // Vérifier si le numéro de sécurité sociale a changé et existe déjà
        if (patientDTO.getNumeroSecuriteSociale() != null && 
                !patientDTO.getNumeroSecuriteSociale().equals(existingPatient.getNumeroSecuriteSociale()) && 
                patientRepository.existsByNumeroSecuriteSociale(patientDTO.getNumeroSecuriteSociale())) {
            throw new BadRequestException("Un patient avec ce numéro de sécurité sociale existe déjà");
        }
        
        patientMapper.updateEntityFromDto(existingPatient, patientDTO);
        Patient updatedPatient = patientRepository.save(existingPatient);
        
        log.info("Patient mis à jour avec succès: {}", id);
        return patientMapper.toDto(updatedPatient);
    }
    
    @Override
    @Transactional
    public void deletePatient(Long id) {
        log.info("Suppression du patient avec l'ID: {}", id);
        
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
        
        patientRepository.delete(patient);
        log.info("Patient supprimé avec succès: {}", id);
    }
    
    @Override
    @Transactional(readOnly = true)
    public PatientDTO findByNumeroSecuriteSociale(String numeroSecuriteSociale) {
        log.info("Recherche du patient avec le numéro de sécurité sociale: {}", numeroSecuriteSociale);
        
        Patient patient = patientRepository.findByNumeroSecuriteSociale(numeroSecuriteSociale)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "numéro de sécurité sociale", numeroSecuriteSociale));
        
        return patientMapper.toDto(patient);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDTO> searchPatientsByNameOrFirstname(String query) {
        log.info("Recherche de patients par nom ou prénom: {}", query);
        
        List<Patient> patients = patientRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(query, query);
        return patientMapper.toDtoList(patients);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDTO> findPatientsByBirthDateRange(LocalDate debut, LocalDate fin) {
        log.info("Recherche de patients nés entre {} et {}", debut, fin);
        
        List<Patient> patients = patientRepository.findByDateNaissanceBetween(debut, fin);
        return patientMapper.toDtoList(patients);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<PatientDTO> findPatientsByGroupeSanguin(String groupeSanguin) {
        log.info("Recherche de patients avec le groupe sanguin: {}", groupeSanguin);
        
        List<Patient> patients = patientRepository.findByGroupeSanguin(groupeSanguin);
        return patientMapper.toDtoList(patients);
    }
} 