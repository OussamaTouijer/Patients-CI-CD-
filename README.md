# 🏥 Microservices Patient - Architecture Spring Cloud

## 📋 Description du Projet

Ce projet implémente une architecture de microservices pour la gestion des patients dans un système de santé. L'architecture suit les principes de Spring Cloud avec des services découplés, une configuration centralisée et une API Gateway pour l'exposition uniforme des endpoints.

## 🏗️ Architecture des Microservices

```
┌─────────────────┐    ┌──────────────────┐    ┌─────────────────┐
│   Client Web    │    │   Mobile App     │    │  External API   │
└─────────┬───────┘    └────────┬─────────┘    └─────────┬───────┘
          │                     │                        │
          └─────────────────────┼────────────────────────┘
                                │
                    ┌───────────▼───────────┐
                    │    API Gateway        │
                    │    (Port: 8888)       │
                    └───────────┬───────────┘
                                │
          ┌─────────────────────┼─────────────────────┐
          │                     │                     │
┌─────────▼─────────┐  ┌────────▼────────┐  ┌────────▼────────┐
│  Patient Service  │  │ Config Service  │  │Discovery Service│
│   (Port: 9006)    │  │  (Port: 9999)   │  │  (Port: 8761)   │
└─────────┬─────────┘  └─────────────────┘  └─────────────────┘
          │
    ┌─────▼─────┐
    │PostgreSQL │
    │    DB     │
    └───────────┘
```

## 🔧 Services et Ports

| Service | Port | Description | Status |
|---------|------|-------------|--------|
| **Discovery Service** | 8761 | Eureka Server pour l'enregistrement des services | ✅ |
| **Config Service** | 9999 | Serveur de configuration centralisée | ✅ |
| **API Gateway** | 8888 | Point d'entrée unique pour tous les services | ✅ |
| **Patient Service** | 9006 | Service métier pour la gestion des patients | ✅ |
| **PostgreSQL** | 5432 | Base de données relationnelle | ✅ |

## 📡 Endpoints API via Gateway

### Base URL
```
http://localhost:8888
```

### 🏥 Patient Service Endpoints

#### 🔄 Routage Dynamique
L'API Gateway utilise un **routage dynamique** avec découverte automatique via Eureka. Les services sont accessibles via:
- **Format**: `/[nom-service-lowercase]/[endpoint]`
- **Exemple**: `/patient-service/patients`

#### CRUD Operations
| Méthode | Endpoint Gateway | Endpoint Direct | Description |
|---------|------------------|-----------------|-------------|
| `POST` | `/patient-service/patients` | `http://localhost:9006/patients` | Créer un nouveau patient |
| `GET` | `/patient-service/patients` | `http://localhost:9006/patients` | Obtenir tous les patients |
| `GET` | `/patient-service/patients/{id}` | `http://localhost:9006/patients/{id}` | Obtenir un patient par ID |
| `PUT` | `/patient-service/patients/{id}` | `http://localhost:9006/patients/{id}` | Mettre à jour un patient |
| `DELETE` | `/patient-service/patients/{id}` | `http://localhost:9006/patients/{id}` | Supprimer un patient |

#### Recherche Avancée
| Méthode | Endpoint Gateway | Description | Paramètres |
|---------|------------------|-------------|------------|
| `GET` | `/patient-service/patients/search/nss/{nss}` | Recherche par NSS | `nss`: Numéro sécurité sociale |
| `GET` | `/patient-service/patients/search?query={term}` | Recherche par nom/prénom | `query`: Terme de recherche |
| `GET` | `/patient-service/patients/search/birthdate?debut={date1}&fin={date2}` | Recherche par période | `debut`, `fin`: Format YYYY-MM-DD |
| `GET` | `/patient-service/patients/search/bloodgroup/{group}` | Recherche par groupe sanguin | `group`: Ex: A+, B-, O+ |

### 📋 Exemples d'utilisation

#### 1. Créer un patient
```bash
curl -X POST http://localhost:8888/patient-service/patients \
  -H "Content-Type: application/json" \
  -d '{
    "nom": "Dupont",
    "prenom": "Jean",
    "dateNaissance": "1985-03-15",
    "numeroSecuriteSociale": "185031234567890",
    "adresse": "123 Rue de la Paix",
    "telephone": "0123456789",
    "email": "jean.dupont@email.com",
    "groupeSanguin": "A+"
  }'
```

#### 2. Obtenir tous les patients
```bash
curl http://localhost:8888/patient-service/patients
```

#### 3. Rechercher par nom
```bash
curl "http://localhost:8888/patient-service/patients/search?query=Dupont"
```

#### 4. Rechercher par groupe sanguin
```bash
curl http://localhost:8888/patient-service/patients/search/bloodgroup/A+
```

## 🚀 Démarrage du Projet

### 🐳 Option 1: Docker (Recommandé) ⭐

#### Prérequis Docker
- Docker Desktop ou Docker Engine
- Docker Compose v2+
- 4 GB RAM minimum
- 10 GB d'espace disque libre

#### 🚀 Démarrage rapide avec Docker
```bash
# 1. Construction des JARs (une seule fois)
for service in discovery_service config_service patient_service gateway_service; do
    cd $service && mvn clean package -DskipTests && cd ..
done

# 2. Lancement de tous les services
docker-compose up -d

# 3. Vérification du statut
docker-compose ps

# 4. Voir les logs en temps réel
docker-compose logs -f
```

#### ⚡ Services prêts automatiquement
Après `docker-compose up -d`, tous les services démarrent avec:
- ✅ **Health checks automatiques** - Démarrage ordonné des dépendances
- ✅ **Données de test préchargées** - 50 patients générés automatiquement  
- ✅ **Configuration optimisée** - Variables d'environnement Docker
- ✅ **Réseau isolé** - Communication sécurisée entre conteneurs

#### 🔍 URLs d'accès rapide
Une fois les services démarrés (≈ 2 minutes) :
```bash
# API Principal via Gateway
curl http://localhost:8888/patient-service/patients

# Eureka Dashboard  
open http://localhost:8761

# Documentation Swagger
open http://localhost:9006/swagger-ui.html
```

---

### ⚙️ Option 2: Démarrage Manuel (Développement)

#### Prérequis
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Git

#### 1. Cloner le projet
```bash
git clone <repository-url>
cd Patient
```

#### 2. Configuration de la base de données
```sql
-- Créer la base de données
CREATE DATABASE "patient-db";

-- Créer l'utilisateur (si nécessaire)
CREATE USER postgres WITH PASSWORD 'oussama';
GRANT ALL PRIVILEGES ON DATABASE "patient-db" TO postgres;
```

#### 3. Ordre de démarrage des services

##### 3.1 Discovery Service (Eureka)
```bash
cd discovery_service
mvn spring-boot:run
```
🌐 Interface: http://localhost:8761

##### 3.2 Config Service
```bash
cd config_service
mvn spring-boot:run
```
🔧 Health Check: http://localhost:9999/actuator/health

##### 3.3 Patient Service
```bash
cd patient_service
mvn spring-boot:run
```
📊 Swagger UI: http://localhost:9006/swagger-ui.html

##### 3.4 API Gateway
```bash
cd gateway_service
mvn spring-boot:run
```
🚪 Gateway: http://localhost:8888

## 📊 Monitoring et Observabilité

### Eureka Dashboard
- **URL**: http://localhost:8761
- **Description**: Visualisation des services enregistrés

### Swagger Documentation
- **URL**: http://localhost:9006/swagger-ui.html
- **Description**: Documentation interactive des APIs Patient

### Actuator Endpoints
| Service | URL | Description |
|---------|-----|-------------|
| Config Service | http://localhost:9999/actuator/health | État du service |
| Patient Service | http://localhost:9006/actuator/health | État du service |
| Patient Service | http://localhost:9006/actuator/env | Variables d'environnement |

## 🔧 Configuration

### 🔄 Configuration du Routage Dynamique

L'API Gateway utilise la **découverte automatique des services** via Eureka pour le routage dynamique.

#### Configuration Gateway (application.yml)
```yaml
spring:
  cloud:
    gateway:
      discovery:
        locator:
          enabled: true                    # Active la découverte automatique
          lower-case-service-id: true      # Convertit les noms en minuscules
      globalcors:
        corsConfigurations:
          '[/**]':
            allowedOrigins: "*"
            allowedHeaders: "*"
            allowedMethods:
              - GET
              - POST
              - PUT
              - DELETE
```

#### Versions Compatibles
| Composant | Version | Remarque |
|-----------|---------|----------|
| Spring Boot | 3.1.12 | Version compatible avec le routage dynamique |
| Spring Cloud | 2022.0.4 | Version testée et stable |
| Java | 21 | Support complet |

#### Fonctionnement du Routage
1. **Enregistrement**: Les services s'enregistrent automatiquement dans Eureka
2. **Découverte**: L'API Gateway interroge Eureka pour les services disponibles
3. **Routage**: Les requêtes `/[service-name]/[endpoint]` sont automatiquement routées
4. **Load Balancing**: Répartition automatique si plusieurs instances

### Configuration centralisée
Les configurations sont stockées dans le repository Git:
- **Repository**: https://github.com/OussamaTouijer/patient-config-repos.git
- **Fichiers**:
  - `application.properties`: Configuration globale
  - `patient-service.properties`: Configuration spécifique au Patient Service

### Variables d'environnement
| Variable | Valeur par défaut | Description |
|----------|-------------------|-------------|
| `CONFIG_SERVICE_URL` | http://localhost:9999 | URL du Config Service |
| `DISCOVERY_SERVICE_URL` | http://localhost:8761/eureka | URL d'Eureka |

## 🐛 Dépannage

### 🐳 Problèmes Docker Courants

#### 1. Services qui ne démarrent pas
```bash
# Vérifier le statut des conteneurs
docker-compose ps

# Voir les logs d'erreur
docker-compose logs [service-name]

# Redémarrer un service spécifique
docker-compose restart [service-name]
```

#### 2. Erreur "Port already in use"
```bash
# Trouver le processus utilisant le port
netstat -tulpn | grep :8761

# Arrêter le service local et utiliser Docker
docker-compose down && docker-compose up -d
```

#### 3. Health checks en échec
```bash
# Vérifier les health checks
docker-compose ps

# Attendre le démarrage complet (2-3 minutes)
# Si problème persiste, rebuilder:
docker-compose build --no-cache
docker-compose up -d
```

#### 4. Données de test manquantes
```bash
# Vérifier que PostgreSQL est bien démarré
docker-compose logs postgres-db

# Redémarrer le patient-service pour recharger les données
docker-compose restart patient-service
```

### 🔧 Problèmes courants (Démarrage manuel)

#### 1. Erreur de connexion à la base de données
```
Failed to configure a DataSource: 'url' attribute is not specified
```
**Solution**: Vérifier que PostgreSQL est démarré et accessible sur le port 5432

#### 2. Service non trouvé dans Eureka
**Solution**: 
1. Vérifier que le Discovery Service est démarré
2. Attendre 30 secondes pour l'enregistrement automatique
3. Consulter http://localhost:8761 pour voir les services enregistrés

#### 3. Configuration non chargée
**Solution**:
1. Vérifier que le Config Service est démarré
2. Tester l'endpoint: http://localhost:9999/patient_service/default
3. Redémarrer le service après le Config Service

### 🔄 Problèmes de Routage Dynamique

#### 4. Erreur "Empty reply from server" via Gateway
```
curl: (52) Empty reply from server
```
**Solution**:
1. Vérifier que le routage dynamique est activé dans `application.yml`
2. Utiliser l'URL correcte: `/patient-service/patients` (service en minuscules)
3. Vérifier les versions de compatibilité (Spring Boot 3.1.12 + Spring Cloud 2022.0.4)

#### 5. Erreur "NoSuchMethodError: headerSet()"
```
java.lang.NoSuchMethodError: 'java.util.Set org.springframework.http.HttpHeaders.headerSet()'
```
**Solution**: Problème de compatibilité de versions
1. Utiliser Spring Boot 3.1.12 (pas 3.3.x)
2. Utiliser Spring Cloud 2022.0.4 (pas 2023.x)
3. Rebuilder le projet: `mvn clean install`

#### 6. Service accessible directement mais pas via Gateway
**Diagnostic**:
```bash
# Tester le service direct
curl http://localhost:9006/patients

# Tester via Gateway
curl http://localhost:8888/patient-service/patients

# Vérifier l'enregistrement Eureka
curl http://localhost:8761/eureka/apps
```

**Solution**:
1. Vérifier que tous les services sont UP dans Eureka
2. Attendre 30 secondes après le démarrage pour la propagation
3. Redémarrer le Gateway Service en dernier

## 🔐 Sécurité

### CORS Configuration
L'API Gateway est configurée pour accepter les requêtes cross-origin:
- **Origins**: `*` (à restreindre en production)
- **Methods**: GET, POST, PUT, DELETE
- **Headers**: `*`

### Recommandations pour la production
1. Configurer HTTPS
2. Restreindre les origins CORS
3. Ajouter l'authentification JWT
4. Implémenter rate limiting
5. Sécuriser les endpoints Actuator

## 🛠️ Commandes Docker Utiles

### Gestion des services
```bash
# Démarrer tous les services
docker-compose up -d

# Arrêter tous les services  
docker-compose down

# Redémarrer un service spécifique
docker-compose restart patient-service

# Voir les logs d'un service
docker-compose logs patient-service

# Reconstruire les images
docker-compose build

# Forcer la reconstruction sans cache
docker-compose build --no-cache
```

### Maintenance
```bash
# Nettoyer les conteneurs arrêtés
docker system prune -f

# Voir l'utilisation des ressources
docker-compose top

# Accéder au shell d'un conteneur
docker-compose exec patient-service sh
```

## 📈 Fonctionnalités Implémentées et Évolutions

### ✅ Fonctionnalités Actuelles
- [x] **🐳 Containerisation Docker** - Déploiement complet avec docker-compose
- [x] **⚡ Health Checks** - Contrôles de santé automatiques pour tous les services
- [x] **🔄 Routage Dynamique** - API Gateway avec découverte automatique via Eureka
- [x] **🏗️ Architecture microservices** complète (Config, Discovery, Gateway, Patient Service)  
- [x] **📋 Configuration centralisée** avec Git repository
- [x] **🗄️ Base de données PostgreSQL** avec JPA/Hibernate et données de test
- [x] **📚 Documentation Swagger/OpenAPI** interactive
- [x] **📊 Monitoring** avec Actuator endpoints
- [x] **🌐 Support CORS** pour les applications web
- [x] **🔧 Gestion des erreurs** centralisée et validation des données

### 🚀 Évolutions Futures Prévues
- [ ] 🔐 Authentification et autorisation (JWT/OAuth2)
- [ ] 📧 Service de notification (email/SMS)
- [ ] 🚀 Cache distribué (Redis)
- [ ] 📊 Monitoring avancé (Micrometer + Prometheus + Grafana)
- [ ] 🛡️ Circuit breaker (Resilience4j)
- [ ] 🧪 Tests d'intégration et tests de charge
- [x] 🚀 **CI/CD Pipeline (Jenkins)** avec déploiement automatisé
- [ ] ☸️ Déploiement Kubernetes (K8s)
- [ ] 📈 Métriques business et alerting

## 🚀 CI/CD avec Jenkins

### 📋 Pipeline Automatisé

Le projet inclut un **pipeline Jenkins complet** qui automatise :

- ✅ **Checkout & Build** - Clone du code et compilation Maven
- ✅ **Tests Unitaires** - Exécution avec rapports JUnit et Jacoco
- ✅ **Analyse de Sécurité** - Scan OWASP des dépendances
- ✅ **Build Docker** - Construction et publication des images
- ✅ **Déploiement Auto** - Mise à jour de l'environnement de test
- ✅ **Tests d'Intégration** - Validation end-to-end des APIs

### 🔧 Configuration Jenkins

#### Accès Jenkins
- **URL**: http://localhost:2001
- **Repository**: https://github.com/OussamaTouijer/Patients-CI-CD-.git
- **Pipeline**: Déclenché automatiquement sur push `main`

#### Scripts de configuration fournis
```bash
# Configuration automatique (Linux/Mac)
chmod +x scripts/setup-jenkins.sh
./scripts/setup-jenkins.sh

# Configuration automatique (Windows)
scripts\setup-jenkins.bat

# Correction accès Docker si nécessaire
./scripts/fix-jenkins-docker.sh
```

#### Guide complet
📚 **Voir [JENKINS-SETUP.md](JENKINS-SETUP.md)** pour la configuration détaillée des credentials et plugins.

### 📊 Rapports Générés

Le pipeline génère automatiquement :
- 🧪 **Rapports de tests** JUnit avec métriques détaillées
- 📈 **Couverture de code** Jacoco (objectif: ≥ 80%)
- 🔐 **Rapport de sécurité** OWASP Dependency Check
- 🐳 **Images Docker** publiées automatiquement
- 📋 **Logs détaillés** de déploiement et tests d'intégration

## 👥 Équipe de Développement

- **Développeur**: Oussama Touijer
- **Architecture**: Microservices Spring Cloud
- **Version**: 1.0.0
- **Dernière mise à jour**: Juillet 2025

## 🎯 État du Projet

### ✅ **Production Ready Features**
- 🐳 **Containerisation complète** - Déploiement Docker optimisé
- ⚡ **Health checks robustes** - Monitoring automatique des services
- 🔄 **Service discovery** - Routage dynamique avec load balancing
- 📊 **Observabilité** - Logs, métriques et documentation complète
- 🛡️ **Gestion d'erreurs** - Validation et responses HTTP standardisées

### 📈 **Métriques du Projet**
- **Services**: 4 microservices + 1 base de données
- **Tests unitaires**: 15+ tests avec Mockito/JUnit
- **Coverage**: ~80% couverture de code
- **API Endpoints**: 10+ endpoints REST documentés
- **Démarrage**: < 3 minutes avec Docker

---

📝 **Note**: Ce projet démontre une architecture microservices **production-ready** avec Spring Cloud, incluant tous les aspects essentiels : containerisation, monitoring, service discovery, et gestion d'erreurs robuste.

