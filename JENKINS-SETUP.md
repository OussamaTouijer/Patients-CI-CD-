# 🚀 Guide de Configuration Jenkins CI/CD

## 📋 Vue d'ensemble

Ce guide explique comment configurer Jenkins pour le pipeline CI/CD des microservices Patient selon les spécifications du projet.

## 🔧 Prérequis Jenkins

### 1. Accès à Jenkins
- **URL**: http://localhost:2001
- **Container**: `jenkins-docker`
- **Status**: ✅ Déjà démarré

### 2. Plugins requis
Les plugins suivants doivent être installés dans Jenkins :

#### Plugins essentiels
- **Pipeline**: Pipeline as Code
- **Git**: Integration Git/GitHub
- **Docker Pipeline**: Support Docker dans les pipelines
- **HTML Publisher**: Publication des rapports HTML
- **JUnit**: Rapports de tests JUnit
- **Jacoco**: Rapports de couverture de code
- **OWASP Dependency-Check**: Analyse de sécurité
- **Credentials Binding**: Gestion sécurisée des credentials

#### Plugins recommandés
- **Blue Ocean**: Interface moderne pour les pipelines
- **Slack Notification**: Notifications Slack (optionnel)
- **Build Timeout**: Timeout automatique des builds
- **Timestamper**: Horodatage des logs

## 🔐 Configuration des Credentials

### 1. Credentials Docker Hub

#### Étapes de configuration:
1. Aller dans **Manage Jenkins** → **Manage Credentials**
2. Cliquer sur **(global)** → **Add Credentials**
3. Configurer:
   - **Kind**: Username with password
   - **Username**: Votre nom d'utilisateur Docker Hub
   - **Password**: Votre mot de passe Docker Hub (ou token)
   - **ID**: `docker-hub-credentials`
   - **Description**: Docker Hub Credentials for Patient Microservices

#### Commande de test:
```bash
# Tester l'accès Docker Hub
docker login -u VOTRE_USERNAME
```

### 2. Credentials GitHub

#### Étapes de configuration:
1. **Manage Jenkins** → **Manage Credentials**
2. **(global)** → **Add Credentials**
3. Configurer:
   - **Kind**: Username with password (ou SSH Username with private key)
   - **Username**: Votre nom d'utilisateur GitHub
   - **Password**: Personal Access Token GitHub
   - **ID**: `github-credentials`
   - **Description**: GitHub Credentials for Patient Microservices

#### Génerer un Personal Access Token GitHub:
1. GitHub → **Settings** → **Developer settings** → **Personal access tokens**
2. **Generate new token** avec les permissions:
   - `repo` (Full control of private repositories)
   - `workflow` (Update GitHub Actions workflows)

## 🛠️ Configuration des Outils

### 1. Configuration Java (OpenJDK 21)

#### Installation dans Jenkins:
1. **Manage Jenkins** → **Global Tool Configuration**
2. **JDK** → **Add JDK**
3. Configurer:
   - **Name**: `OpenJDK-21`
   - **JAVA_HOME**: `/opt/java/openjdk` (dans le container)
   - Ou cocher **Install automatically** et choisir OpenJDK 21

### 2. Configuration Maven

#### Installation dans Jenkins:
1. **Global Tool Configuration** → **Maven**
2. **Add Maven**
3. Configurer:
   - **Name**: `Maven-3.9`
   - **Version**: 3.9.x (dernière version)
   - Cocher **Install automatically**

### 3. Configuration Docker

#### Vérifier l'accès Docker:
```bash
# Dans le container Jenkins, vérifier Docker
docker exec -it jenkins-docker docker --version
docker exec -it jenkins-docker docker ps
```

Si Docker n'est pas accessible, redémarrer le container avec le socket Docker:
```bash
docker run -d \
  --name jenkins-docker \
  -p 2001:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  -v $(which docker):/usr/bin/docker \
  jenkins/jenkins:lts
```

## 📋 Configuration du Pipeline

### 1. Créer un nouveau Pipeline Job

1. **New Item** → **Pipeline**
2. **Nom**: `Patient-Microservices-Pipeline`
3. **Description**: Pipeline CI/CD pour les microservices Patient

### 2. Configuration Pipeline

#### Option A: Pipeline from SCM
- **Definition**: Pipeline script from SCM
- **SCM**: Git
- **Repository URL**: URL de votre repository GitHub
- **Credentials**: Sélectionner `github-credentials`
- **Script Path**: `Jenkinsfile`

#### Option B: Pipeline Script (pour test)
- **Definition**: Pipeline script
- Copier le contenu du Jenkinsfile

### 3. Configuration des Triggers

- **Build Triggers**:
  - ☑️ **GitHub hook trigger for GITScm polling**
  - ☑️ **Poll SCM**: `H/5 * * * *` (vérifier toutes les 5 minutes)

## 🧪 Test du Pipeline

### 1. Test manuel
1. Aller dans le job `Patient-Microservices-Pipeline`
2. Cliquer **Build Now**
3. Surveiller l'exécution dans **Console Output**

### 2. Vérifications attendues

#### Étapes réussies:
- ✅ **Checkout Code**: Clone du repository
- ✅ **Build & Test Services**: Compilation et tests unitaires
- ✅ **Docker Build & Push**: Images Docker créées et publiées
- ✅ **Deploy to Test Environment**: Services déployés
- ✅ **Integration Tests**: Tests d'intégration passés

#### Rapports générés:
- 📊 **Test Results**: Résultats des tests JUnit
- 📈 **Coverage Report**: Rapport de couverture Jacoco
- 🔐 **Security Scan**: Rapport OWASP Dependency Check
- 🏗️ **Artifacts**: JAR files archivés

## 🚨 Dépannage

### Problèmes courants

#### 1. Erreur "Docker not found"
```bash
# Vérifier l'accès Docker dans Jenkins
docker exec -it jenkins-docker which docker
docker exec -it jenkins-docker docker ps
```

**Solution**: Redémarrer Jenkins avec le socket Docker monté

#### 2. Erreur de credentials
```
ERROR: Invalid credentials
```

**Solution**: 
1. Vérifier les credentials dans **Manage Credentials**
2. Tester l'accès manuellement
3. Régénérer les tokens si nécessaire

#### 3. Erreur de réseau Docker
```
ERROR: Cannot connect to Docker daemon
```

**Solution**:
```bash
# Redémarrer le container Jenkins avec les bonnes permissions
docker stop jenkins-docker
docker rm jenkins-docker

docker run -d \
  --name jenkins-docker \
  -p 2001:8080 \
  -p 50000:50000 \
  -v jenkins_home:/var/jenkins_home \
  -v /var/run/docker.sock:/var/run/docker.sock \
  --group-add $(getent group docker | cut -d: -f3) \
  jenkins/jenkins:lts
```

#### 4. Timeout des builds
**Solution**: Augmenter le timeout dans le pipeline:
```groovy
pipeline {
    agent any
    options {
        timeout(time: 30, unit: 'MINUTES')
    }
    // ...
}
```

## 📊 Monitoring et Rapports

### Rapports disponibles

1. **Test Results**: `/job/Patient-Microservices-Pipeline/lastBuild/testReport/`
2. **Coverage Report**: `/job/Patient-Microservices-Pipeline/lastBuild/Patient_Service_-_Coverage_Report/`
3. **Console Output**: Logs détaillés de chaque build
4. **Build History**: Historique des builds avec statuts

### Métriques suivies

- ✅ **Success Rate**: Pourcentage de builds réussis
- ⏱️ **Build Duration**: Temps d'exécution moyen
- 🧪 **Test Coverage**: Pourcentage de couverture de code
- 🔐 **Security Issues**: Nombre de vulnérabilités détectées

## 🔄 Workflow Complet

### Déclenchement automatique
1. **Commit/Push** sur la branche `main`
2. **GitHub Webhook** → Jenkins (si configuré)
3. **Pipeline Start** → Checkout → Build → Test → Docker → Deploy
4. **Notification** → Succès/Échec

### Processus manuel
1. **Jenkins UI** → `Patient-Microservices-Pipeline`
2. **Build Now**
3. **Monitor Progress** dans Blue Ocean ou Console
4. **Review Reports** après completion

---

## 🎯 Checklist de Configuration

- [ ] Jenkins accessible sur http://localhost:2001
- [ ] Plugins installés (Pipeline, Git, Docker, etc.)
- [ ] Credentials Docker Hub configurés (`docker-hub-credentials`)
- [ ] Credentials GitHub configurés (`github-credentials`)
- [ ] Java OpenJDK 21 configuré (`OpenJDK-21`)
- [ ] Maven 3.9 configuré (`Maven-3.9`)
- [ ] Accès Docker vérifié dans le container Jenkins
- [ ] Pipeline Job créé (`Patient-Microservices-Pipeline`)
- [ ] Jenkinsfile présent dans le repository
- [ ] Premier build test exécuté avec succès

Une fois cette checklist complétée, le pipeline CI/CD sera entièrement opérationnel ! 🚀