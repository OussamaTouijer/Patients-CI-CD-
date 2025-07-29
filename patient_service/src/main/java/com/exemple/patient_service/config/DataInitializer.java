package com.exemple.patient_service.config;

import com.exemple.patient_service.entity.Patient;
import com.exemple.patient_service.enums.GenreSexe;
import com.exemple.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {
    
    private final PatientRepository patientRepository;
    private final Random random = new Random();

    @Bean
    @Profile("!prod")
    public CommandLineRunner initDatabase() {
        return args -> {
            // Vérifier si la base de données est déjà remplie
            if (patientRepository.count() > 0) {
                log.info("La base de données contient déjà {} patients, initialisation ignorée.", patientRepository.count());
                return;
            }

            log.info("Initialisation de la base de données avec 50 patients marocains...");

            List<Patient> patients = new ArrayList<>();
            
            // Noms marocains courants
            String[] noms = {"Alaoui", "Bennamyni", "Cherkaoui", "Daoudi", "El Amrani", "Fassi", "Gharbi", "Hassani",
                           "Idrissi", "Jabri", "Khalil", "Lahlou", "Mansouri", "Naciri", "Ouali", "Pacha", 
                           "Qadiri", "Rahmani", "Saidi", "Tazi", "Uthman", "Vaziri", "Wahbi", "Yousfi", "Zahraoui"};
                           
            String[] prenoms = {"Ahmed", "Mohammed", "Hassan", "Youssef", "Omar", "Fatima", "Amina", "Khadija", 
                              "Zineb", "Sara", "Layla", "Nour", "Yasmin", "Hanae", "Salma", "Imane", 
                              "Karim", "Adil", "Bilal", "Tarik", "Samir", "Nabil", "Rachid", "Jamal", "Hicham"};
                              
            String[] rues = {"Rue Mohammed V", "Avenue Hassan II", "Boulevard Mohammed VI", "Rue Al Qods", 
                            "Avenue Palestine", "Boulevard Zerktouni", "Rue Ibn Batouta", "Avenue Ibn Sina", 
                            "Boulevard Al Massira", "Rue Al Akkari", "Avenue Al Fida", "Boulevard Al Wahda"};
                            
            String[] villes = {"Casablanca", "Rabat", "Fès", "Marrakech", "Agadir", "Tanger", "Meknès", 
                              "Oujda", "Kénitra", "Tétouan", "Safi", "El Jadida", "Béni Mellal", "Taza"};
                              
            String[] groupesSanguins = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
            
            String[] antecedents = {"Diabète de type 2", "Hypertension artérielle", "Asthme", "Allergie aux arachides", 
                                  "Opération appendicite", "Fracture bras droit", "Aucun", "Migraine chronique", 
                                  "Glaucome", "Maladie de Crohn", "Aucun antécédent", "Allergie au lactose"};

            // Générer 50 patients avec des données aléatoires marocaines
            for (int i = 0; i < 50; i++) {
                GenreSexe genre = (random.nextBoolean()) ? GenreSexe.HOMME : GenreSexe.FEMME;
                
                String nom = noms[random.nextInt(noms.length)];
                String prenom;
                
                // Choisir un prénom en fonction du genre
                if (genre == GenreSexe.HOMME) {
                    prenom = prenoms[random.nextInt(15)]; // Prénoms masculins
                } else {
                    prenom = prenoms[random.nextInt(10) + 15]; // Prénoms féminins
                }
                
                // Générer une date de naissance entre 1950 et 2005
                int year = 1950 + random.nextInt(55);
                int month = 1 + random.nextInt(12);
                int day = 1 + random.nextInt(28); // Pour éviter les problèmes de jours invalides
                LocalDate dateNaissance = LocalDate.of(year, month, day);
                
                // Adresse marocaine
                String rue = rues[random.nextInt(rues.length)];
                String numero = (1 + random.nextInt(100)) + "";
                String ville = villes[random.nextInt(villes.length)];
                String codePostal = String.format("%05d", 10000 + random.nextInt(90000));
                String adresse = numero + " " + rue + ", " + codePostal + " " + ville + ", Maroc";
                
                // Email basé sur le nom et prénom
                String email = prenom.toLowerCase() + "." + nom.toLowerCase() + "@gmail.com";
                
                // Numéro de téléphone marocain (format +212)
                String telephone = "+212" + (random.nextInt(5) + 6) + generateRandomDigits(8);
                
                // Numéro de sécurité sociale marocain (format simplifié)
                String nss = (genre == GenreSexe.HOMME ? "1" : "2") + 
                             String.format("%02d", year % 100) +
                             String.format("%02d", month) +
                             String.format("%02d", random.nextInt(95) + 1) + // région
                             generateRandomDigits(6);
                             
                String groupeSanguin = groupesSanguins[random.nextInt(groupesSanguins.length)];
                String antecedentsMedicaux = antecedents[random.nextInt(antecedents.length)];
                
                Patient patient = Patient.builder()
                        .nom(nom)
                        .prenom(prenom)
                        .dateNaissance(dateNaissance)
                        .telephone(telephone)
                        .adresse(adresse)
                        .email(email)
                        .genre(genre)
                        .antecedentsMedicaux(antecedentsMedicaux)
                        .numeroSecuriteSociale(nss)
                        .groupeSanguin(groupeSanguin)
                        .build();
                        
                patients.add(patient);
            }
            
            patientRepository.saveAll(patients);
            log.info("50 patients marocains ont été créés avec succès dans la base de données.");
        };
    }
    
    /**
     * Génère une chaîne de chiffres aléatoires de longueur spécifiée
     */
    private String generateRandomDigits(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
} 