package com.exemple.patient_service.entity;

import com.exemple.patient_service.enums.GenreSexe;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nom;
    
    @Column(nullable = false, length = 100)
    private String prenom;
    
    @Column(nullable = false)
    private LocalDate dateNaissance;
    
    @Column(length = 20)
    private String telephone;
    
    @Column(nullable = false, length = 200)
    private String adresse;
    
    @Column(length = 200)
    private String email;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private GenreSexe genre;
    
    @Column(length = 200)
    private String antecedentsMedicaux;
    
    @Column(name = "numero_securite_sociale", length = 20, unique = true)
    private String numeroSecuriteSociale;
    
    @Column(name = "groupe_sanguin", length = 5)
    private String groupeSanguin;
    
    @Column(name = "created_at")
    private LocalDate createdAt;
    
    @Column(name = "updated_at")
    private LocalDate updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDate.now();
        this.updatedAt = LocalDate.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
} 