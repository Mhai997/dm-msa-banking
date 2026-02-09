@echo off
REM Sincronización rápida del proyecto Maven
REM Ejecuta: mvnw clean dependency:resolve compile -DskipTests

echo Sincronizando proyecto...
call .\mvnw.cmd clean dependency:resolve compile -DskipTests

if %ERRORLEVEL% EQU 0 (
    echo.
    echo [OK] Proyecto sincronizado exitosamente.
    echo.
    echo Ahora:
    echo 1. Reinicia tu IDE (JetBrains/IntelliJ)
    echo 2. Los imports de testing deberían resolverse automáticamente
    echo 3. Puedes ejecutar tests con: .\mvnw.cmd test
    pause
) else (
    echo.
    echo [ERROR] Hubo un problema durante la sincronización.
    echo Intenta ejecutar manualmente:
    echo   .\mvnw.cmd clean
    echo   .\mvnw.cmd dependency:resolve
    pause
)

