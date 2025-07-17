@echo off
title PREPARANDO PARA LA NUBE
color 0B

echo.
echo ████████████████████████████████████████████████████████████
echo █                                                          █
echo █        🌸 FLORERIA - PREPARACION PARA LA NUBE 🌸        █
echo █                                                          █
echo ████████████████████████████████████████████████████████████
echo.

echo [INFO] Verificando que todo este listo para el deploy...
echo.

echo [1/4] Verificando Java...
java -version >nul 2>&1
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Java no encontrado
    pause
    exit /b 1
)
echo [✓] Java OK

echo.
echo [2/4] Limpiando proyecto...
call mvnw.cmd clean >nul 2>&1
echo [✓] Proyecto limpio

echo.
echo [3/4] Compilando para produccion...
call mvnw.cmd package -DskipTests -q
if %ERRORLEVEL% neq 0 (
    echo [ERROR] Fallo en la compilacion
    pause
    exit /b 1
)
echo [✓] Compilacion exitosa

echo.
echo [4/4] Verificando archivos necesarios...
if exist "railway.json" (
    echo [✓] railway.json presente
) else (
    echo [!] railway.json no encontrado
)

if exist "src\main\resources\application-railway.properties" (
    echo [✓] application-railway.properties presente
) else (
    echo [!] application-railway.properties no encontrado
)

echo.
echo ========================================
echo         PROYECTO LISTO PARA LA NUBE
echo ========================================
echo.
echo 📋 PROXIMOS PASOS:
echo.
echo 1. BACKEND (Railway):
echo    - Ve a: https://railway.app
echo    - Conecta tu GitHub
echo    - Selecciona este repositorio
echo    - Deploy automatico
echo.
echo 2. FRONTEND (Vercel):
echo    - Ve a: https://vercel.com  
echo    - Conecta tu repo de React
echo    - Deploy automatico
echo.
echo 3. CONFIGURAR VARIABLES:
echo    - En Railway: SPRING_PROFILES_ACTIVE=railway
echo    - En Vercel: REACT_APP_API_URL=tu-url-railway
echo.
echo 📖 Lee: DESPLIEGUE-NUBE-FACIL.md para guia completa
echo.
echo ========================================

pause
