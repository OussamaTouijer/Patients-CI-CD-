# Dockerfile for Gateway Service
FROM eclipse-temurin:21-jre-alpine

# Métadonnées
LABEL maintainer="Oussama Touijer"
LABEL service="gateway-service"
LABEL version="1.0.0"

# Variables d'environnement
ENV JAVA_OPTS="-Xmx512m -Xms256m"
ENV SERVER_PORT=8888

# Installer curl pour le health check
RUN apk add --no-cache curl

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# Créer le répertoire de travail
WORKDIR /app

# Copier le JAR
COPY target/*.jar app.jar

# Changer le propriétaire des fichiers
RUN chown -R appuser:appgroup /app

# Passer à l'utilisateur non-root
USER appuser

# Exposer le port
EXPOSE ${SERVER_PORT}

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=80s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT}/actuator/health || exit 1

# Point d'entrée
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]