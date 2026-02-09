# Script para sincronizar y descargar dependencias del proyecto Maven
# Ejecutar este script en PowerShell

Write-Host "Limpiando proyecto..." -ForegroundColor Green
.\mvnw.cmd clean

Write-Host "Descargando dependencias..." -ForegroundColor Green
.\mvnw.cmd dependency:resolve

Write-Host "Compilando (sin tests)..." -ForegroundColor Green
.\mvnw.cmd compile -DskipTests

Write-Host "¡Listo! Si usas IntelliJ/JetBrains IDE, ahora reimporta el proyecto Maven (View > Tool Windows > Maven, y pulsa el icono de refresh)." -ForegroundColor Cyan
Write-Host "Después, deberías ver que los imports de testing se resuelven correctamente." -ForegroundColor Cyan

