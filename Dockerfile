# ---------- Build stage ----------
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# 1) Copiamos solo pom primero para cachear dependencias
COPY pom.xml ./
RUN mvn -q -DskipTests dependency:go-offline

# 2) Copiamos el código y compilamos
COPY src ./src
RUN mvn -q -DskipTests clean package

# ---------- Runtime stage ----------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia el jar generado (usa wildcard para no depender del nombre exacto)
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Perfil por defecto (puedes cambiarlo a "development" o "docker")
ENV SPRING_PROFILES_ACTIVE=development

ENTRYPOINT ["java","-jar","/app/app.jar"]
