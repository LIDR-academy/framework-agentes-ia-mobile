# Convenciones Generales de Ingeniería

## Naming
- Archivos: PascalCase (`UserProfileScreen.kt`, `UserProfileView.swift`)
- Funciones/variables: camelCase
- Constantes: UPPER_SNAKE_CASE
- Packages/modules: lowercase.separated.by.dots

## Commits
Conventional Commits obligatorio:
- `feat(modulo): descripción` — Nueva funcionalidad
- `fix(modulo): descripción` — Corrección de bug
- `refactor(modulo): descripción` — Refactoring sin cambio funcional
- `test(modulo): descripción` — Agregar o modificar tests
- `docs(modulo): descripción` — Documentación
- `chore(modulo): descripción` — Tareas de mantenimiento

## Strings
- NUNCA hardcodear strings visibles al usuario
- Android: strings.xml con naming `screen_element_description`
- iOS: Localizable.strings o String Catalogs con la misma convención
- Plurales: quantity strings (Android) / stringsdict (iOS)

## Imports
- No wildcard imports (no `import java.util.*`)
- Orden: stdlib → third-party → proyecto

## Logging
- Usar logger del proyecto (no `println`, `print`, `Log.d`)
- No loggear datos PII en ningún entorno
- No dejar logs de debug en código de producción

## TODOs
- Nunca dejar TODOs sin issue asociado
- Formato: `// TODO(FEAT-XXX): descripción`