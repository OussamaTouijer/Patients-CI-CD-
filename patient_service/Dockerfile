FROM maven:3.8.6-eclipse-temurin-17-alpine AS build

WORKDIR /app
COPY ./pom.xml ./
COPY ./mvnw .
COPY ./.mvn ./.mvn
RUN mvn dependency:go-offline -B

COPY ./src ./src
RUN mvn package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar patient-service.jar

EXPOSE 9006

ENTRYPOINT ["java", "-jar", "patient-service.jar"] 