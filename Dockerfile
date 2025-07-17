# Etapa 1: Construcción
FROM maven:3.9.6-eclipse-temurin-17 AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .

# Descargar dependencias (esto se puede cachear)
RUN mvn dependency:go-offline -B

# Copiar código fuente
COPY src ./src

# Compilar la aplicación
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:17-jre-alpine

# Instalar curl para health checks
RUN apk --no-cache add curl

# Crear directorio de la aplicación
WORKDIR /app

# Crear usuario no root para seguridad
RUN addgroup -g 1001 -S appgroup && \
    adduser -S appuser -u 1001 -G appgroup

# Copiar el JAR desde la etapa de construcción
COPY --from=build /app/target/prueba-0.0.1-SNAPSHOT.jar floreria-app.jar

# Crear directorio para uploads y asignar permisos
RUN mkdir -p uploads && \
    chown -R appuser:appgroup /app

# Cambiar a usuario no root
USER appuser

# Exponer puerto
EXPOSE 8080

# Variables de entorno para Railway
ENV SPRING_PROFILES_ACTIVE=railway

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "floreria-app.jar"]
