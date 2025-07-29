# 🚀 Jenkins CI/CD - Guide de Démarrage Rapide

## ⚡ Configuration en 5 minutes

### 1. 🐳 Vérifier Jenkins
```bash
# Vérifier que Jenkins est en cours d'exécution
docker ps | grep jenkins

# Si Jenkins n'est pas démarré :
./scripts/setup-jenkins.sh    # Linux/Mac
scripts\setup-jenkins.bat     # Windows
```

### 2. 🌐 Accéder à Jenkins
- **URL**: http://localhost:2001
- **Statut**: ✅ Déjà configuré et opérationnel

### 3. 🔐 Configurer les Credentials

#### GitHub Credentials
1. **Manage Jenkins** → **Manage Credentials** → **(global)** → **Add Credentials**
2. Configuration:
   - **Kind**: Username with password
   - **Username**: `OussamaTouijer`
   - **Password**: [Votre GitHub Personal Access Token]
   - **ID**: `github-credentials`

#### Docker Hub Credentials
1. **Add Credentials** (même menu)
2. Configuration:
   - **Kind**: Username with password
   - **Username**: [Votre username Docker Hub]
   - **Password**: [Votre password Docker Hub]
   - **ID**: `docker-hub-credentials`

### 4. 🔧 Créer le Pipeline Job

#### Étapes rapides:
1. **New Item** → **Pipeline** → Nom: `Patient-Microservices-Pipeline`
2. **Pipeline** section:
   - **Definition**: Pipeline script from SCM  
   - **SCM**: Git
   - **Repository URL**: `https://github.com/OussamaTouijer/Patients-CI-CD-.git`
   - **Credentials**: Sélectionner `github-credentials`
   - **Branch**: `*/main`
   - **Script Path**: `Jenkinsfile`

3. **Save** → **Build Now**

### 5. ✅ Vérification du Pipeline

#### Pipeline attendu:
```
📥 Checkout Code           (2 min)
🔍 Code Quality & Security (3 min)
🏗️ Build & Test Services   (5 min)
📊 Test Reports & Coverage (2 min)
🐳 Docker Build & Push     (4 min)
🚀 Deploy to Test Environment (3 min)
🧪 Integration Tests       (2 min)
```

#### URLs de vérification après build:
- 🎯 **API**: http://localhost:8888/patient-service/patients
- 📊 **Eureka**: http://localhost:8761
- 📚 **Swagger**: http://localhost:9006/swagger-ui.html

## 🐛 Résolution Rapide des Problèmes

### ❌ "Docker not found in Jenkins"
```bash
./scripts/fix-jenkins-docker.sh
```

### ❌ "Invalid credentials"
1. Vérifier les credentials dans **Manage Credentials**
2. Tester manuellement : `docker login` et `git clone`

### ❌ "Build timeout"
1. Vérifier les ressources système (RAM > 4GB)
2. Augmenter le timeout Jenkins si nécessaire

### ❌ "Services not starting"
```bash
# Vérifier les ports disponibles
docker ps
netstat -tulpn | grep -E "(8761|9999|9006|8888|5432)"

# Redémarrer les services si nécessaire  
docker-compose down && docker-compose up -d
```

## 📋 Checklist de Validation

- [ ] ✅ Jenkins accessible sur http://localhost:2001
- [ ] ✅ Credentials GitHub et Docker Hub configurés
- [ ] ✅ Pipeline `Patient-Microservices-Pipeline` créé
- [ ] ✅ Premier build exécuté avec succès (≈ 20 minutes)
- [ ] ✅ Tous les services déployés et accessibles
- [ ] ✅ Tests d'intégration passés
- [ ] ✅ Images Docker publiées (si configuré)

## 🎯 Résultat Final

Après configuration réussie :
```
🎉 Pipeline CI/CD Opérationnel

📋 Auto-trigger: ✅ Sur chaque push 'main'
🏗️ Build Maven: ✅ Tous les services
🧪 Tests: ✅ Unitaires + Intégration  
🐳 Docker: ✅ Images construites et déployées
📊 Reports: ✅ Coverage, Security, Tests
🚀 Deploy: ✅ Environment de test mis à jour

⏱️ Durée totale: ~20 minutes
🔄 Fréquence: Automatique sur commit
```

---

💡 **Conseil**: Une fois configuré, le pipeline se déclenche automatiquement à chaque push sur la branche `main` de votre repository GitHub.