@echo off
:: Script de Validación de Pruebas de Integración
:: Florería - Módulo de Productos

echo 🚀 Iniciando validación de Pruebas de Integración...
echo ==================================================

:: Limpiar y compilar
echo 📦 Limpiando y compilando proyecto...
call mvn clean compile test-compile

if %ERRORLEVEL% neq 0 (
    echo ❌ Error en la compilación. Abortando.
    exit /b 1
)

echo ✅ Compilación exitosa
echo.

:: Ejecutar pruebas de integración del repositorio
echo 🗄️  Ejecutando pruebas de ProductRepository...
call mvn test -Dtest=ProductRepositoryIntegrationTest

if %ERRORLEVEL% equ 0 (
    echo ✅ ProductRepositoryIntegrationTest: PASÓ
) else (
    echo ❌ ProductRepositoryIntegrationTest: FALLÓ
)

echo.

:: Ejecutar pruebas de integración del servicio
echo ⚙️  Ejecutando pruebas de ProductService...
call mvn test -Dtest=ProductServiceIntegrationTest

if %ERRORLEVEL% equ 0 (
    echo ✅ ProductServiceIntegrationTest: PASÓ
) else (
    echo ❌ ProductServiceIntegrationTest: FALLÓ
)

echo.

:: Ejecutar pruebas de integración del controlador
echo 🌐 Ejecutando pruebas de ProductController...
call mvn test -Dtest=ProductControllerIntegrationTest

if %ERRORLEVEL% equ 0 (
    echo ✅ ProductControllerIntegrationTest: PASÓ
) else (
    echo ❌ ProductControllerIntegrationTest: FALLÓ
)

echo.

:: Ejecutar todas las pruebas de integración juntas
echo 🔄 Ejecutando todas las pruebas de integración...
call mvn test -Dtest="com.lulu.product.integration.*Test"

if %ERRORLEVEL% equ 0 (
    echo ✅ Todas las pruebas de integración: PASARON
) else (
    echo ❌ Algunas pruebas de integración fallaron
)

echo.
echo ✨ Validación completada!
echo 📁 Revisa los reportes en target\surefire-reports\

pause
