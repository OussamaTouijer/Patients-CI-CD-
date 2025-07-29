package com.exemple.patient_service.repository;

import com.exemple.patient_service.entity.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    
    // Trouver un patient par numéro de sécurité sociale
    Optional<Patient> findByNumeroSecuriteSociale(String numeroSecuriteSociale);
    
    // Vérifier si un numéro de sécurité sociale existe déjà
    boolean existsByNumeroSecuriteSociale(String numeroSecuriteSociale);
    
    // Recherche de patients par nom ou prénom (insensible à la casse)
    List<Patient> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);
    
    // Recherche de patients nés entre deux dates
    List<Patient> findByDateNaissanceBetween(LocalDate debut, LocalDate fin);
    
    // Recherche de patients par groupe sanguin
    List<Patient> findByGroupeSanguin(String groupeSanguin);
    
    // Recherche de patients par nom, prénom et date de naissance
    Optional<Patient> findByNomAndPrenomAndDateNaissance(String nom, String prenom, LocalDate dateNaissance);
} 