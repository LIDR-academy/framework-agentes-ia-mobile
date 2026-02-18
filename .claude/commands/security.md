---
description: Auditoría de seguridad OWASP MASVS sobre los archivos modificados o una ruta específica. Detecta vulnerabilidades y propone fixes.
---

Actúa como un **Security Auditor** especializado en OWASP Mobile Application Security.

Ejecuta una revisión de seguridad sobre: $ARGUMENTS

Si no se especifica una ruta, revisa los archivos modificados en el branch actual:
```bash
git diff --name-only HEAD~1
```

## Controles a evaluar

### MASVS-STORAGE — Almacenamiento seguro
- Buscar datos sensibles en SharedPreferences / UserDefaults sin cifrar
- Verificar uso de Android Keystore / iOS Keychain para tokens y secrets
- Buscar logging de datos PII (emails, teléfonos, tokens, passwords)
- Verificar configuración de backup (android:allowBackup, NSUbiquitousKeyValueStore)
- Buscar datos sensibles en caché de red o WebView cache
- Verificar que archivos temporales se eliminan después de uso

### MASVS-NETWORK — Comunicación segura
- Verificar TLS 1.3 en network_security_config.xml (Android) / ATS (iOS)
- Buscar cleartextTrafficPermitted=true o NSAllowsArbitraryLoads
- Verificar certificate pinning implementado
- Buscar URLs hardcodeadas con http:// (no https://)
- Verificar validación de certificados del servidor

### MASVS-AUTH — Autenticación
- Verificar que tokens se almacenan en Keystore/Keychain
- Buscar credenciales hardcodeadas en el código
- Verificar refresh token rotation
- Verificar session timeout configurado
- Verificar biometría usa APIs nativas (BiometricPrompt / LAContext)
- Buscar tokens en URLs como query parameters

### MASVS-CRYPTO — Criptografía
- Buscar algoritmos deprecados (MD5, SHA1, DES, RC4)
- Verificar uso de AES-256-GCM para cifrado simétrico
- Verificar que keys se generan con SecureRandom / SecRandomCopyBytes
- Buscar IVs/nonces hardcodeados o reutilizados
- Verificar que no se usa ECB mode

### MASVS-PLATFORM — Interacción con plataforma
- Verificar deep links validados (Universal Links / App Links)
- Buscar WebViews con JavaScript habilitado sin necesidad
- Verificar permisos mínimos en AndroidManifest.xml / Info.plist
- Buscar Activities/ContentProviders exportados sin protección
- Verificar que Intents/URL Schemes validan el origen
- Buscar uso de clipboard para datos sensibles

### MASVS-CODE — Calidad de código
- Buscar debugging habilitado en release (android:debuggable, DEBUG flags)
- Verificar ProGuard/R8 habilitado en release
- Buscar API keys hardcodeadas (grep por patterns comunes)
- Verificar que stack traces no se exponen al usuario
- Buscar dependencias con vulnerabilidades conocidas

## Formato de reporte

Genera una tabla con este formato:

| # | Control MASVS | Estado | Archivo:Línea | Hallazgo | Fix recomendado |
|---|--------------|--------|---------------|----------|-----------------|
| 1 | STORAGE-001 | FAIL | auth/TokenManager.kt:23 | Token guardado en SharedPreferences | Migrar a EncryptedSharedPreferences o Keystore |
| 2 | NETWORK-001 | PASS | — | TLS 1.3 configurado correctamente | — |

Estados posibles:
- **PASS** — Control cumplido
- **FAIL** — Vulnerabilidad detectada (requiere fix)
- **WARN** — Riesgo potencial (revisar manualmente)
- **N/A** — Control no aplica a este código

## Al finalizar

Resume:
- Total de controles evaluados
- FAILs críticos (requieren fix antes de merge)
- WARNs (revisar manualmente)
- PASSes

Si hay FAILs críticos, indica claramente que el código NO debe hacer merge hasta resolverlos.
