pipeline {
    agent any
    
    environment {
        // Variables d'environnement pour Docker Hub
        DOCKER_HUB_REPO = 'oussama/patient-microservices'
        DOCKER_HUB_CREDENTIALS = credentials('docker-hub-credentials')
        
        // Variables pour GitHub
        GITHUB_CREDENTIALS = credentials('github-credentials')
        
        // Variables pour les services
        SERVICES = 'discovery_service,config_service,patient_service,gateway_service'
        
        // Version basée sur le build number
        VERSION = "${BUILD_NUMBER}"
        
        // Java et Maven
        JAVA_HOME = '/opt/java/openjdk'
        MAVEN_OPTS = '-Xmx1024m -Xms512m'
    }
    
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    
    stages {
        stage('📥 Checkout Code') {
            steps {
                echo '🔄 Clonage du code depuis GitHub...'
                git branch: 'main',
                    credentialsId: 'github-credentials',
                    url: 'https://github.com/OussamaTouijer/Patients-CI-CD-.git'
                
                script {
                    // Récupérer les informations du commit
                    env.GIT_COMMIT_SHORT = sh(
                        script: 'git rev-parse --short HEAD',
                        returnStdout: true
                    ).trim()
                    env.GIT_BRANCH_NAME = sh(
                        script: 'git rev-parse --abbrev-ref HEAD',
                        returnStdout: true
                    ).trim()
                }
                
                echo "📋 Commit: ${env.GIT_COMMIT_SHORT}"
                echo "🌿 Branche: ${env.GIT_BRANCH_NAME}"
            }
        }
        
        stage('🔍 Code Quality & Security Checks') {
            parallel {
                stage('🔐 Security Scan') {
                    steps {
                        echo '🔐 Vérification de sécurité des dépendances...'
                        script {
                            def services = env.SERVICES.split(',')
                            services.each { service ->
                                dir(service.trim()) {
                                    sh '''
                                        echo "🔍 Analyse de sécurité pour ${service}..."
                                        mvn org.owasp:dependency-check-maven:check -DskipTests || true
                                    '''
                                }
                            }
                        }
                    }
                }
                
                stage('📊 Code Analysis') {
                    steps {
                        echo '📊 Analyse statique du code...'
                        script {
                            def services = env.SERVICES.split(',')
                            services.each { service ->
                                dir(service.trim()) {
                                    sh '''
                                        echo "📈 Analyse pour ${service}..."
                                        mvn spotbugs:check -DskipTests || true
                                    '''
                                }
                            }
                        }
                    }
                }
            }
        }
        
        stage('🏗️ Build & Test Services') {
            parallel {
                stage('🔧 Discovery Service') {
                    steps {
                        dir('discovery_service') {
                            echo '🏗️ Construction du Discovery Service...'
                            sh '''
                                mvn clean compile -DskipTests
                                mvn test
                                mvn package -DskipTests
                            '''
                            
                            // Publier les résultats de test
                            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                            
                            // Archiver les artefacts
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
                
                stage('⚙️ Config Service') {
                    steps {
                        dir('config_service') {
                            echo '🏗️ Construction du Config Service...'
                            sh '''
                                mvn clean compile -DskipTests
                                mvn test
                                mvn package -DskipTests
                            '''
                            
                            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
                
                stage('🏥 Patient Service') {
                    steps {
                        dir('patient_service') {
                            echo '🏗️ Construction du Patient Service...'
                            sh '''
                                mvn clean compile -DskipTests
                                mvn test
                                mvn package -DskipTests
                            '''
                            
                            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
                
                stage('🚪 Gateway Service') {
                    steps {
                        dir('gateway_service') {
                            echo '🏗️ Construction du Gateway Service...'
                            sh '''
                                mvn clean compile -DskipTests
                                mvn test
                                mvn package -DskipTests
                            '''
                            
                            publishTestResults testResultsPattern: 'target/surefire-reports/*.xml'
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                        }
                    }
                }
            }
        }
        
        stage('📊 Test Reports & Coverage') {
            steps {
                echo '📊 Génération des rapports de test et couverture...'
                
                script {
                    def services = env.SERVICES.split(',')
                    services.each { service ->
                        dir(service.trim()) {
                            sh '''
                                echo "📈 Rapport de couverture pour ${service}..."
                                mvn jacoco:report || true
                            '''
                        }
                    }
                }
                
                // Publier les rapports de couverture
                publishHTML([
                    allowMissing: false,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'patient_service/target/site/jacoco',
                    reportFiles: 'index.html',
                    reportName: 'Patient Service - Coverage Report'
                ])
            }
        }
        
        stage('🐳 Docker Build & Push') {
            steps {
                echo '🐳 Construction et publication des images Docker...'
                
                script {
                    // Login vers Docker Hub
                    sh 'echo $DOCKER_HUB_CREDENTIALS_PSW | docker login -u $DOCKER_HUB_CREDENTIALS_USR --password-stdin'
                    
                    def services = env.SERVICES.split(',')
                    services.each { service ->
                        def serviceName = service.trim()
                        def imageName = "${DOCKER_HUB_REPO}-${serviceName}:${VERSION}"
                        def latestImageName = "${DOCKER_HUB_REPO}-${serviceName}:latest"
                        
                        echo "🏗️ Construction de l'image Docker pour ${serviceName}..."
                        
                        dir(serviceName) {
                            sh """
                                docker build -t ${imageName} .
                                docker tag ${imageName} ${latestImageName}
                                
                                echo "📤 Publication de l'image ${imageName}..."
                                docker push ${imageName}
                                docker push ${latestImageName}
                                
                                echo "🧹 Nettoyage de l'image locale..."
                                docker rmi ${imageName} ${latestImageName} || true
                            """
                        }
                    }
                }
            }
        }
        
        stage('🚀 Deploy to Test Environment') {
            when {
                branch 'main'
            }
            steps {
                echo '🚀 Déploiement vers l\'environnement de test...'
                
                script {
                    sh '''
                        echo "🛑 Arrêt des services existants..."
                        docker-compose down || true
                        
                        echo "🔄 Mise à jour des images..."
                        docker-compose pull
                        
                        echo "🚀 Démarrage des services mis à jour..."
                        docker-compose up -d
                        
                        echo "⏳ Attente du démarrage des services..."
                        sleep 60
                        
                        echo "🔍 Vérification des services..."
                        docker-compose ps
                    '''
                }
            }
        }
        
        stage('🧪 Integration Tests') {
            when {
                branch 'main'
            }
            steps {
                echo '🧪 Exécution des tests d\'intégration...'
                
                script {
                    // Test de l'API Gateway
                    sh '''
                        echo "🔍 Test de l'API Gateway..."
                        
                        # Attendre que les services soient prêts
                        for i in {1..10}; do
                            if curl -f http://localhost:8888/patient-service/patients > /dev/null 2>&1; then
                                echo "✅ API Gateway opérationnelle"
                                break
                            else
                                echo "⏳ Attente de l'API Gateway... ($i/10)"
                                sleep 30
                            fi
                        done
                        
                        # Tests d'intégration
                        echo "🧪 Test de récupération des patients..."
                        PATIENTS_COUNT=$(curl -s http://localhost:8888/patient-service/patients | jq length || echo "0")
                        
                        if [ "$PATIENTS_COUNT" -gt "0" ]; then
                            echo "✅ Tests d'intégration réussis - $PATIENTS_COUNT patients trouvés"
                        else
                            echo "❌ Tests d'intégration échoués"
                            exit 1
                        fi
                        
                        # Test de santé des services
                        curl -f http://localhost:8761 || exit 1
                        curl -f http://localhost:9999/actuator/health || exit 1
                        curl -f http://localhost:9006/actuator/health || exit 1
                        curl -f http://localhost:8888/actuator/health || exit 1
                        
                        echo "✅ Tous les tests d'intégration sont passés"
                    '''
                }
            }
        }
    }
    
    post {
        always {
            echo '🧹 Nettoyage post-build...'
            
            // Nettoyer les images Docker locales pour économiser l'espace
            sh '''
                docker system prune -f || true
            '''
            
            // Publier les résultats de test JUnit
            publishTestResults testResultsPattern: '**/target/surefire-reports/*.xml', allowEmptyResults: true
            
            // Archiver les logs
            archiveArtifacts artifacts: '**/target/*.log', allowEmptyArchive: true
        }
        
        success {
            echo '🎉 Pipeline exécuté avec succès!'
            
            // Notification de succès (peut être configuré avec Slack, Teams, etc.)
            script {
                def message = """
                🎉 **Build Réussi** 
                
                📋 **Détails:**
                - 🏗️ **Projet:** Patient Microservices
                - 🔢 **Build:** #${BUILD_NUMBER}
                - 🌿 **Branche:** ${env.GIT_BRANCH_NAME}
                - 📝 **Commit:** ${env.GIT_COMMIT_SHORT}
                - ⏱️ **Durée:** ${currentBuild.durationString}
                
                ✅ **Services déployés:**
                - Discovery Service
                - Config Service  
                - Patient Service
                - Gateway Service
                
                🔗 **URLs:**
                - API: http://localhost:8888/patient-service/patients
                - Eureka: http://localhost:8761
                - Swagger: http://localhost:9006/swagger-ui.html
                """
                
                echo message
            }
        }
        
        failure {
            echo '❌ Pipeline échoué!'
            
            // Log des erreurs pour diagnostic
            sh '''
                echo "🔍 Diagnostic des erreurs..."
                docker-compose logs --tail=50 || true
                docker ps -a || true
            '''
            
            script {
                def message = """
                ❌ **Build Échoué**
                
                📋 **Détails:**
                - 🏗️ **Projet:** Patient Microservices  
                - 🔢 **Build:** #${BUILD_NUMBER}
                - 🌿 **Branche:** ${env.GIT_BRANCH_NAME}
                - 📝 **Commit:** ${env.GIT_COMMIT_SHORT}
                - ⏱️ **Durée:** ${currentBuild.durationString}
                
                🔍 **Vérifier:**
                - Les logs Jenkins
                - Les tests unitaires
                - La configuration Docker
                """
                
                echo message
            }
        }
        
        unstable {
            echo '⚠️ Build instable - certains tests ont échoué'
        }
        
        aborted {
            echo '🚫 Build annulé'
        }
    }
}