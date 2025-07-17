#!/bin/bash
echo "🌱 Starting Floreria App..."
echo "🐘 Database URL: ${DATABASE_URL}"
echo "🌍 Active Profile: ${SPRING_PROFILES_ACTIVE}"
echo "🔧 Java Memory: -Xmx512m -Xms256m"

# Iniciar la aplicación
exec java -Xmx512m -Xms256m -Dspring.profiles.active=railway -jar floreria-app.jar
