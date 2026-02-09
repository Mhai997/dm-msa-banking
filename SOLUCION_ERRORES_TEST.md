# Solución: Error de Paquetes de Test no Encontrados

## Problema
El compilador no encuentra:
- `org.springframework.boot.test.autoconfigure.web.servlet`
- `org.springframework.boot.test.mock.mockito`

## Causa
El archivo `pom.xml` tenía dependencias de test inválidas que no existen en Maven Central. Además, el IDE necesita sincronizar/reimportar las dependencias después de cambiar el `pom.xml`.

## Solución Paso a Paso

### Opción 1: Sincronización automática (Recomendado)

1. **Abre PowerShell en la raíz del proyecto**:
   - Windows: `Win + R` → Escribe `powershell` → `Enter`
   - Navega a: `cd "C:\Users\jorge\ejericio upgrade\dm-msa-banking"`

2. **Ejecuta el script de sincronización**:
   ```powershell
   .\sync-maven.ps1
   ```
   - Este script:
     - Limpia el proyecto (`mvnw clean`)
     - Descarga todas las dependencias (`mvnw dependency:resolve`)
     - Compila sin tests (`mvnw compile -DskipTests`)

3. **Reinicia el IDE** (JetBrains/IntelliJ):
   - Si está abierto, ciérralo y vuelve a abrirlo.

4. **Espera a que el IDE reindexe** (puede tardar 1-2 minutos):
   - Verás en la barra de progreso "Indexing..." abajo a la derecha.

5. **Verifica que los errores desaparezcan**:
   - Los imports rojo de `WebMvcTest` y `MockBean` deberían desaparecer.

### Opción 2: Sincronización manual desde el IDE

Si prefieres no abrir PowerShell separada:

1. En JetBrains IDE:
   - View → Tool Windows → Maven (o pulsa Alt+8)
   
2. En el panel Maven que aparece a la derecha:
   - Haz clic en el icono circular de "Reload Projects" (refresh)
   
3. Espera a que Maven descargue y resincroni­ce.

### Opción 3: Comandos individuales en PowerShell

Si prefieres ejecutar por separado:

```powershell
# 1. Limpia el proyecto
.\mvnw.cmd clean

# 2. Descarga dependencias
.\mvnw.cmd dependency:resolve

# 3. Compila
.\mvnw.cmd compile -DskipTests
```

## ¿Qué cambié en el pom.xml?

**Eliminé**:
- `spring-boot-starter-data-jpa-test` (NO EXISTE)
- `spring-boot-starter-flyway-test` (NO EXISTE)
- `spring-boot-starter-validation-test` (NO EXISTE)
- `spring-boot-starter-webmvc-test` (NO EXISTE)

**Cambié**:
- `spring-boot-starter-webmvc` → `spring-boot-starter-web` (es la forma correcta)

**Mantuve/Añadí**:
- `spring-boot-starter-test` (contiene JUnit, Mockito, AssertJ, etc.)
- `spring-boot-test` (anotaciones de test)
- `spring-boot-test-autoconfigure` (auto-configuración para tests)

## Qué debería pasar después

Una vez sincronizado:
- ✅ El import `org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest` se resuelve.
- ✅ El import `org.springframework.boot.test.mock.mockito.MockBean` se resuelve.
- ✅ La anotación `@WebMvcTest(ClientController.class)` funciona.
- ✅ Puedes ejecutar los tests: `.\mvnw.cmd test`

## Si aún hay problemas

Si después de ejecutar `sync-maven.ps1` y reiniciar el IDE sigues viendo errores:

1. **Limpia el caché del IDE**:
   - File → Invalidate Caches → "Invalidate and Restart"
   
2. **Verifica Java 17**:
   - File → Project Structure → Project SDK → Asegúrate que sea "17" (no "11" ni otra versión)

3. **Ejecuta desde terminal separada** (fuera del IDE):
   ```powershell
   .\mvnw.cmd clean verify
   ```
   - Si no hay errores, el problema es solo de sincronización del IDE.

---

**Resumen**: Ejecuta `.\sync-maven.ps1` en PowerShell, reinicia el IDE, y debería estar listo. 🚀

