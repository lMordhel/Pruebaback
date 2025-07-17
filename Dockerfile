# Etapa 1: Construcción
FROM maven:3.9.4-openjdk-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Descargar dependencias (esto se puede cachear)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM openjdk:17-jdk-slim

# Crear directorio de la aplicación
WORKDIR /app

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/prueba-0.0.1-SNAPSHOT.jar floreria-app.jar

# Crear directorio para uploads
RUN mkdir -p uploads

# Exponer puerto
EXPOSE 8080

# Variables de entorno para Railway
ENV SPRING_PROFILES_ACTIVE=prod

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "floreria-app.jar"]
