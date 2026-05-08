FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Cache de dependencias Maven.
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Compila el API Gateway, punto de entrada opcional para la arquitectura.
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8085

ENTRYPOINT ["java", "-jar", "app.jar"]
