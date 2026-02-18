# Skill: Revisión de Seguridad OWASP MASVS

## Cuándo se activa
Código que maneja datos sensibles, autenticación, networking, almacenamiento local o deep links.

## Qué hace
Revisa el código contra los controles de OWASP MASVS (Mobile Application Security Verification Standard) y reporta hallazgos categorizados como PASS / WARN / FAIL.

## Checklist de revisión

### Almacenamiento de datos
- [ ] Datos sensibles (tokens, passwords, PII) en Android Keystore / iOS Keychain
- [ ] No usar SharedPreferences / UserDefaults para datos sensibles
- [ ] No loggear datos PII en ningún entorno
- [ ] Backups excluyen datos sensibles

### Networking
- [ ] TLS 1.3 obligatorio
- [ ] Certificate pinning habilitado
- [ ] No cleartext traffic (network_security_config.xml / ATS)
- [ ] Validación de certificados no deshabilitada

### Autenticación y sesión
- [ ] Tokens almacenados de forma segura
- [ ] Refresh token rotation implementada
- [ ] Sesión expira después de inactividad
- [ ] Biometric auth usa APIs del sistema (no custom)

### Deep links y WebViews
- [ ] Deep links validados con App Links (Android) / Universal Links (iOS)
- [ ] WebViews: JavaScript deshabilitado por defecto
- [ ] No file:// access en WebViews
- [ ] Input validation en parámetros de deep links

### Permisos y exportación
- [ ] Permisos mínimos necesarios, solicitados en momento de uso
- [ ] Activities/ContentProviders con android:exported=false por defecto
- [ ] ProGuard/R8 habilitado en release (Android)

### Credenciales
- [ ] No API keys hardcodeadas en código fuente
- [ ] BuildConfig (Android) / xcconfig (iOS) para inyección en build time
- [ ] Secrets de CI en GitHub Secrets

## Output esperado

```
## Resultado de Auditoría OWASP MASVS

### FAIL (bloquean merge)
- [FAIL] Descripción del hallazgo — archivo:línea

### WARN (requieren justificación)
- [WARN] Descripción del hallazgo — archivo:línea

### PASS
- [PASS] Descripción del control verificado
```
