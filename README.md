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

#### CRUD Operations
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/patients` | Créer un nouveau patient |
| `GET` | `/patients` | Obtenir tous les patients |
| `GET` | `/patients/{id}` | Obtenir un patient par ID |
| `PUT` | `/patients/{id}` | Mettre à jour un patient |
| `DELETE` | `/patients/{id}` | Supprimer un patient |

#### Recherche Avancée
| Méthode | Endpoint | Description | Paramètres |
|---------|----------|-------------|------------|
| `GET` | `/patients/search/nss/{nss}` | Recherche par NSS | `nss`: Numéro sécurité sociale |
| `GET` | `/patients/search?query={term}` | Recherche par nom/prénom | `query`: Terme de recherche |
| `GET` | `/patients/search/birthdate?debut={date1}&fin={date2}` | Recherche par période | `debut`, `fin`: Format YYYY-MM-DD |
| `GET` | `/patients/search/bloodgroup/{group}` | Recherche par groupe sanguin | `group`: Ex: A+, B-, O+ |

### 📋 Exemples d'utilisation

#### 1. Créer un patient
```bash
curl -X POST http://localhost:8888/patients \
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
curl http://localhost:8888/patients
```

#### 3. Rechercher par nom
```bash
curl "http://localhost:8888/patients/search?query=Dupont"
```

#### 4. Rechercher par groupe sanguin
```bash
curl http://localhost:8888/patients/search/bloodgroup/A+
```

## 🚀 Démarrage du Projet

### Prérequis
- Java 17+
- Maven 3.6+
- PostgreSQL 12+
- Git

### 1. Cloner le projet
```bash
git clone <repository-url>
cd Patient
```

### 2. Configuration de la base de données
```sql
-- Créer la base de données
CREATE DATABASE "patient-db";

-- Créer l'utilisateur (si nécessaire)
CREATE USER postgres WITH PASSWORD 'oussama';
GRANT ALL PRIVILEGES ON DATABASE "patient-db" TO postgres;
```

### 3. Ordre de démarrage des services

#### 3.1 Discovery Service (Eureka)
```bash
cd discovery_service
mvn spring-boot:run
```
🌐 Interface: http://localhost:8761

#### 3.2 Config Service
```bash
cd config_service
mvn spring-boot:run
```
🔧 Health Check: http://localhost:9999/actuator/health

#### 3.3 Patient Service
```bash
cd patient_service
mvn spring-boot:run
```
📊 Swagger UI: http://localhost:9006/swagger-ui.html

#### 3.4 API Gateway
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

### Problèmes courants

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

## 📈 Évolutions Futures

- [ ] Authentification et autorisation (JWT/OAuth2)
- [ ] Service de notification
- [ ] Cache distribué (Redis)
- [ ] Monitoring avancé (Micrometer + Prometheus)
- [ ] Circuit breaker (Resilience4j)
- [ ] Tests d'intégration
- [ ] Docker containerization
- [ ] CI/CD Pipeline (Jenkins)

## 👥 Équipe de Développement

- **Développeur**: Oussama Touijer
- **Architecture**: Microservices Spring Cloud
- **Version**: 1.0.0

---

📝 **Note**: Ce projet est conçu à des fins éducatives pour démontrer l'implémentation d'une architecture microservices avec Spring Cloud.