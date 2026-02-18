# Seguridad y Privacidad (OWASP MASVS)

## Almacenamiento de datos
- Datos sensibles (tokens, passwords, PII) → Android Keystore / iOS Keychain
- NUNCA en SharedPreferences, UserDefaults, ni logs
- Backups deben excluir datos sensibles
- Cache de imágenes/datos: no incluir contenido sensible

## Networking
- TLS 1.3 obligatorio
- Certificate pinning habilitado
- No cleartext traffic:
  - Android: network_security_config.xml
  - iOS: ATS (App Transport Security)
- No deshabilitar validación de certificados (ni en debug)

## Autenticación y sesión
- Tokens almacenados de forma segura (Keystore/Keychain)
- Refresh token rotation implementada
- Sesión expira después de periodo de inactividad
- Biometric auth: usar APIs del sistema (no custom)
- Rate limiting en intentos de login

## Deep links
- Android: App Links con verificación de dominio
- iOS: Universal Links con apple-app-site-association
- Validar TODOS los parámetros de deep links (input validation)
- No exponer rutas internas sensibles vía deep links

## WebViews
- JavaScript deshabilitado por defecto
- No permitir file:// access
- Habilitar JavaScript solo si es estrictamente necesario
- Validar URLs antes de cargar en WebView

## Permisos
- Solicitar el mínimo necesario
- Solicitar en el momento de uso (no al inicio de la app)
- Explicar al usuario por qué se necesita el permiso

## Exportación de componentes
- Android: Activities/ContentProviders con android:exported=false por defecto
- Solo exportar lo estrictamente necesario
- Intent filters: validar datos recibidos

## Ofuscación
- Android: ProGuard/R8 habilitado en release
- iOS: Bitcode (si aplica)

## Credenciales y secrets
- NUNCA hardcodear API keys en código fuente
- Android: BuildConfig para inyección en build time
- iOS: xcconfig para inyección en build time
- CI/CD: GitHub Secrets (nunca en el repo)
- .gitignore: incluir archivos de configuración local con secrets

## Logging
- NUNCA loggear datos PII en ningún entorno (ni debug)
- NUNCA loggear tokens, passwords, o datos sensibles
- Usar niveles de log apropiados (no verbose en producción)

## Revisión continua
- Ejecutar `/security` antes de cada commit
- FAIL crítico bloquea el merge
- WARN requiere justificación documentada en el PR