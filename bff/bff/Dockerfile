FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app

# Descarga dependencias primero para aprovechar la cache de Docker.
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline

# Compila el BFF, que es el intermediario entre React y los microservicios.
COPY src ./src
RUN mvn -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar
EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
