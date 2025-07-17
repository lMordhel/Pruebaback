#!/bin/bash

# Script de Validación de Pruebas de Integración
# Florería - Módulo de Productos

echo "🚀 Iniciando validación de Pruebas de Integración..."
echo "=================================================="

# Limpiar y compilar
echo "📦 Limpiando y compilando proyecto..."
mvn clean compile test-compile

if [ $? -ne 0 ]; then
    echo "❌ Error en la compilación. Abortando."
    exit 1
fi

echo "✅ Compilación exitosa"
echo ""

# Ejecutar pruebas de integración del repositorio
echo "🗄️  Ejecutando pruebas de ProductRepository..."
mvn test -Dtest=ProductRepositoryIntegrationTest

if [ $? -eq 0 ]; then
    echo "✅ ProductRepositoryIntegrationTest: PASÓ"
else
    echo "❌ ProductRepositoryIntegrationTest: FALLÓ"
fi

echo ""

# Ejecutar pruebas de integración del servicio
echo "⚙️  Ejecutando pruebas de ProductService..."
mvn test -Dtest=ProductServiceIntegrationTest

if [ $? -eq 0 ]; then
    echo "✅ ProductServiceIntegrationTest: PASÓ"
else
    echo "❌ ProductServiceIntegrationTest: FALLÓ"
fi

echo ""

# Ejecutar pruebas de integración del controlador
echo "🌐 Ejecutando pruebas de ProductController..."
mvn test -Dtest=ProductControllerIntegrationTest

if [ $? -eq 0 ]; then
    echo "✅ ProductControllerIntegrationTest: PASÓ"
else
    echo "❌ ProductControllerIntegrationTest: FALLÓ"
fi

echo ""

# Ejecutar todas las pruebas de integración juntas
echo "🔄 Ejecutando todas las pruebas de integración..."
mvn test -Dtest="com.lulu.product.integration.*Test"

if [ $? -eq 0 ]; then
    echo "✅ Todas las pruebas de integración: PASARON"
else
    echo "❌ Algunas pruebas de integración fallaron"
fi

echo ""
echo "📊 Generando reporte de cobertura..."
mvn jacoco:report

echo ""
echo "✨ Validación completada!"
echo "📁 Revisa los reportes en target/site/jacoco/"
