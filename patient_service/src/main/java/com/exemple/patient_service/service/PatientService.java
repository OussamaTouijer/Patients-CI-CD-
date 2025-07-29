package com.exemple.patient_service.service;

import com.exemple.patient_service.dto.PatientDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Service pour la gestion des patients
 */
public interface PatientService {
    
    /**
     * Créer un nouveau patient
     * @param patientDTO les données du patient à créer
     * @return le patient créé
     */
    PatientDTO createPatient(PatientDTO patientDTO);
    
    /**
     * Obtenir un patient par son ID
     * @param id l'identifiant du patient
     * @return le patient trouvé
     */
    PatientDTO getPatientById(Long id);
    
    /**
     * Obtenir tous les patients
     * @return la liste des patients
     */
    List<PatientDTO> getAllPatients();
    
    /**
     * Mettre à jour un patient existant
     * @param id l'identifiant du patient
     * @param patientDTO les nouvelles données du patient
     * @return le patient mis à jour
     */
    PatientDTO updatePatient(Long id, PatientDTO patientDTO);
    
    /**
     * Supprimer un patient
     * @param id l'identifiant du patient à supprimer
     */
    void deletePatient(Long id);
    
    /**
     * Rechercher un patient par numéro de sécurité sociale
     * @param numeroSecuriteSociale le numéro de sécurité sociale
     * @return le patient trouvé
     */
    PatientDTO findByNumeroSecuriteSociale(String numeroSecuriteSociale);
    
    /**
     * Rechercher les patients par nom ou prénom
     * @param query le terme de recherche
     * @return la liste des patients correspondants
     */
    List<PatientDTO> searchPatientsByNameOrFirstname(String query);
    
    /**
     * Rechercher les patients nés entre deux dates
     * @param debut la date de début
     * @param fin la date de fin
     * @return la liste des patients correspondants
     */
    List<PatientDTO> findPatientsByBirthDateRange(LocalDate debut, LocalDate fin);
    
    /**
     * Rechercher les patients par groupe sanguin
     * @param groupeSanguin le groupe sanguin
     * @return la liste des patients correspondants
     */
    List<PatientDTO> findPatientsByGroupeSanguin(String groupeSanguin);
} 