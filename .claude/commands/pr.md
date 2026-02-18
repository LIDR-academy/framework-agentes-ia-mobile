---
description: Prepara un PR completo con descripción, checklist de Definition of Done, y validaciones automáticas. Ejecutar cuando el feature está listo para review.
---

Actúa como el **PR Guardian** definido en AGENTS.md (§3.5).

Prepara el PR para el feature actual. $ARGUMENTS

## Proceso

### 1. Recolecta información

- Lee el spec.md del feature en `docs/features/FEAT-XXX/`
- Lista los archivos modificados con `git diff --name-only main`
- Cuenta los commits con `git log --oneline main..HEAD`
- Identifica el ticket asociado desde los commits o el spec

### 2. Ejecuta validaciones

Antes de generar el PR, ejecuta estas verificaciones:

```bash
# Android
./gradlew ktlintCheck
./gradlew testDebugUnitTest

# iOS
swiftlint
xcodebuild test -scheme App -destination 'platform=iOS Simulator,name=iPhone 16'
```

Reporta el resultado de cada una.

### 3. Genera el documento del PR

Crea `docs/features/FEAT-XXX/pr.md` con esta estructura:

```markdown
# PR: FEAT-XXX — [Título del feature]

## Descripción
[Qué hace este cambio y por qué. 2-3 párrafos máximo.]

## Ticket
[Link al ticket en Jira/Linear/GitHub Issues]

## Cambios realizados
[Resumen por área: UI, Data, Domain, Tests, Config]

## Screenshots / Recordings
| Estado | Android | iOS |
|--------|---------|-----|
| Loading | [screenshot] | [screenshot] |
| Content | [screenshot] | [screenshot] |
| Error | [screenshot] | [screenshot] |
| Empty | [screenshot] | [screenshot] |

(Nota: indicar al desarrollador que agregue los screenshots manualmente)

## Archivos modificados
[Lista agrupada por capa: presentation/, domain/, data/, tests/]

## Breaking changes
[Ninguno / Lista de cambios que rompen compatibilidad]

## Migration notes
[Ninguno / Pasos necesarios para migrar]

## Definition of Done

### Funcionalidad
- [ ] Todos los Acceptance Criteria de spec.md cumplidos
- [ ] 4 estados de UI implementados (Loading/Content/Error/Empty)
- [ ] Manejo de errores robusto (no crashes, no estados indefinidos)
- [ ] Feature flag wrapping (si aplica)

### Calidad de código
- [ ] ktlint / swiftlint pasa sin errores
- [ ] Tests unitarios pasan
- [ ] Cobertura ≥ 80% en ViewModels y UseCases
- [ ] No hay TODOs sin issue asociado
- [ ] Conventional commits en todos los commits

### Arquitectura
- [ ] /arch validado — Clean Architecture respetada
- [ ] No dependencias nuevas sin aprobación
- [ ] DTOs no expuestos fuera de data layer

### Seguridad (OWASP MASVS)
- [ ] /security ejecutado — sin FAILs críticos
- [ ] Datos sensibles en Keystore/Keychain
- [ ] No PII en logs
- [ ] Network security config correcta

### Accesibilidad
- [ ] Content descriptions / accessibility labels
- [ ] Dynamic Type support
- [ ] Touch targets ≥ 48dp / 44pt
- [ ] Color contrast ≥ 4.5:1

### Observabilidad
- [ ] Analytics events implementados según spec.md
- [ ] Crash reporting configurado para nuevos flujos

### Internacionalización
- [ ] Todos los strings en resources (no hardcoded)
- [ ] Plurales manejados correctamente
```

### 4. Resumen final

Después de generar el PR, muestra un resumen:

```
PR listo para review:
- Archivos modificados: X
- Tests ejecutados: X passed / X failed
- Lint: PASS / FAIL
- Security: X PASS / X FAIL / X WARN
- Architecture: PASS / FAIL
- DoD items completados: X/Y

Pendiente del desarrollador:
- [ ] Agregar screenshots de los 4 estados
- [ ] Resolver FAILs de seguridad (si hay)
- [ ] Asignar reviewers
```
