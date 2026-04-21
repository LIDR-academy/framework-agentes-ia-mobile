# Framework de Desarrollo Mobile Nativo con Agentes IA

Proyecto de app movil nativa (Android) que implementa un sistema de desarrollo asistido por agentes IA. Incluye un flujo completo de 5 etapas — desde la especificacion del ticket hasta el PR listo para merge — con validaciones de arquitectura, seguridad y calidad integradas en cada paso.

## Stack Tecnologico

| Tecnologia | Version |
|------------|---------|
| Kotlin | 2.0.21 |
| Jetpack Compose | Material 3 (BOM 2024.09.00) |
| Retrofit + Gson | 2.11.0 |
| OkHttp | 4.12.0 |
| Coroutines + Flow | 1.9.0 |
| Gradle (AGP) | 8.13.2 |
| compileSdk / minSdk / targetSdk | 36 / 25 / 36 |
| Testing | JUnit 4, MockK 1.13.12, Turbine 1.2.0 |

## Arquitectura

Clean Architecture con regla de dependencia estricta:

```
presentation → domain ← data
```

Cada feature se estructura asi:

```
feature-xxx/
  presentation/     → Screens, ViewModels, UiStates
  domain/           → Models, UseCases, Repository interfaces
  data/             → Repository impl, DTOs, API services, Mappers
```

**Reglas fundamentales:**
- `domain/` NUNCA importa de `presentation/` ni `data/`
- `presentation/` NUNCA importa directamente de `data/`
- DTOs NUNCA se exponen fuera de `data/`
- ViewModels exponen un solo UiState (sealed class)
- Toda pantalla tiene 4 estados: Loading, Content, Error, Empty
- UseCases tienen una sola responsabilidad (`operator fun invoke()`)

## Estructura del Proyecto

```
framework-agentes-ia-mobile/
├── CLAUDE.md                    # Contexto del proyecto para el agente IA
├── AGENTS.md                    # Source of truth: roles, contratos, gates
├── .claude/
│   ├── commands/                # 6 comandos invocables con /nombre
│   │   ├── spec.md              # /spec — Genera especificacion desde ticket
│   │   ├── ui.md                # /ui — Traduce diseno a codigo nativo
│   │   ├── api.md               # /api — Genera consumo de endpoints REST
│   │   ├── arch.md              # /arch — Valida Clean Architecture
│   │   ├── security.md          # /security — Auditoria OWASP MASVS
│   │   └── pr.md                # /pr — Prepara PR con DoD checklist
│   └── skills/                  # 4 skills especializados
│       ├── owasp-mobile/        # Revision de seguridad OWASP
│       ├── api-consume/         # Consumo seguro de APIs REST
│       ├── arch-check/          # Validacion de arquitectura limpia
│       └── figma-to-ui/         # Traduccion de diseno a codigo nativo
├── rules/                       # Estandares por plataforma
│   ├── engineering.md           # Convenciones generales (naming, commits, strings)
│   ├── mobile_android.md        # Reglas especificas Android
│   ├── mobile_ios.md            # Reglas especificas iOS
│   └── security_privacy.md     # Seguridad y privacidad (OWASP MASVS)
├── docs/
│   ├── decisions.md             # ADRs (Architecture Decision Records)
│   └── features/
│       └── TEMPLATE/            # 7 plantillas para documentacion de features
│           ├── spec.md          # Especificacion (User Stories, ACs, UI States)
│           ├── risks.md         # Registro de riesgos
│           ├── tasks.md         # Desglose por vertical slices
│           ├── qa_plan.md       # Plan de QA
│           ├── ui_contract.md   # Contrato de UI (componentes, tokens, estados)
│           ├── arch.md          # Documento de arquitectura
│           └── pr.md            # Documento del Pull Request
└── app/                         # Codigo fuente Android
    └── src/main/java/proyecto/com/framework-agentes-ia-mobile/
        ├── MainActivity.kt
        └── ui/theme/            # Design System
            ├── Color.kt
            ├── Theme.kt
            └── Type.kt
```

## Sistema de Agentes

Definido en **AGENTS.md**, el flujo de desarrollo completo tiene 5 etapas con gates explicitos entre cada una.

### Etapa 1 — Spec Agent (`/spec FEAT-XXX`)

Transforma un ticket (Linear, Jira, GitHub Issues) en una especificacion atomica y verificable.

**Genera:** `spec.md`, `risks.md`, `tasks.md`, `qa_plan.md`

**Gate:** ACs en formato Given/When/Then, 4 estados de UI definidos, analytics events, riesgos con mitigacion.

### Etapa 2 — Design Translator (`/ui`)

Traduce un diseno (Figma, screenshot) a codigo nativo mapeado al Design System del proyecto.

**Genera:** `ui_contract.md` + componentes Compose/SwiftUI con los 4 estados + previews.

**Gate:** State machine completa, tokens del Design System, checklist de accesibilidad.

### Etapa 3 — Mobile Architect (`/arch`)

Valida y disena la arquitectura del feature siguiendo Clean Architecture.

**Genera:** `arch.md` + `tasks.md` refinado con paths, complejidad y orden de ejecucion.

**Gate:** Regla de dependencia respetada, ViewModels solo dependen de UseCases, sin dependencias nuevas sin aprobacion.

### Etapa 4 — Code Agent (`/api`, `/ui`, `/security`)

Implementa el codigo en 3 vertical slices obligatorios:

| Slice | Contenido | Commit |
|-------|-----------|--------|
| 1 | Happy path + Loading | `feat(xxx): implement happy path` |
| 2 | Empty + Error + Retry | `feat(xxx): add error handling and empty state` |
| 3 | Analytics + Feature Flag + a11y + i18n | `feat(xxx): add analytics, a11y, and i18n` |

**Gate por slice:** Compila sin warnings, lint pasa, tests pasan (cobertura >= 80% en ViewModels), sin strings hardcodeados, `/security` sin FAILs criticos.

### Etapa 5 — PR Guardian (`/pr`)

Prepara el PR con validacion completa del Definition of Done.

**Genera:** `pr.md` con descripcion, screenshots, archivos modificados, breaking changes, y checklist completo (funcionalidad, calidad, arquitectura, seguridad OWASP, accesibilidad, observabilidad, i18n).

## Comandos Disponibles

| Comando | Descripcion |
|---------|-------------|
| `/spec FEAT-XXX` | Genera especificacion atomica desde un ticket |
| `/ui [argumento]` | Traduce diseno a Composable / SwiftUI |
| `/api` | Genera DTO, mapper, repository, tests para endpoints |
| `/arch [ruta]` | Valida dependencias y estructura Clean Architecture |
| `/security [ruta]` | Auditoria de seguridad OWASP MASVS |
| `/pr` | Prepara PR con checklist de Definition of Done |

## Skills

| Skill | Activacion | Funcion |
|-------|-----------|---------|
| `owasp-mobile` | Codigo con datos sensibles, auth, networking | Revision contra controles MASVS (PASS/WARN/FAIL) |
| `api-consume` | Nuevo endpoint o modificacion de networking | Genera data + domain layer completo con tests |
| `arch-check` | Review de PRs o generacion de features | Valida regla de dependencia y patrones |
| `figma-to-ui` | Construccion de UI desde Figma/screenshots | Convierte diseno a codigo nativo con Design System |

## Rules

| Archivo | Cobertura |
|---------|-----------|
| `engineering.md` | Naming, commits convencionales, strings, imports, logging, TODOs |
| `mobile_android.md` | Stack Android, MVI, Compose, Room, testing, seguridad |
| `mobile_ios.md` | Stack iOS, MVVM, SwiftUI, testing, seguridad |
| `security_privacy.md` | OWASP MASVS completo: storage, network, auth, crypto, platform, code |

## Templates

En `docs/features/TEMPLATE/` se encuentran 7 plantillas que se copian automaticamente al crear un nuevo feature:

| Template | Proposito |
|----------|-----------|
| `spec.md` | User Stories, Acceptance Criteria, UI States, Analytics, Feature Flags |
| `risks.md` | Riesgos tecnicos, de negocio y de timeline con mitigaciones |
| `tasks.md` | Tareas organizadas por 3 vertical slices |
| `qa_plan.md` | Happy paths, edge cases, error scenarios, dispositivos target |
| `ui_contract.md` | Componentes, tokens del Design System, state machine, accesibilidad |
| `arch.md` | Diagrama de dependencias, estructura por capa, contratos de datos, ADRs |
| `pr.md` | Descripcion, screenshots, archivos modificados, Definition of Done |

## Seguridad (OWASP MASVS)

La seguridad esta integrada en cada etapa del flujo, no solo al final. Controles principales:

- **Storage:** Datos sensibles en Android Keystore / iOS Keychain. Nunca en SharedPreferences/UserDefaults/logs
- **Network:** TLS 1.3 obligatorio, certificate pinning, sin cleartext traffic
- **Auth:** Tokens seguros, refresh rotation, session timeout, biometria nativa
- **Crypto:** AES-256-GCM, SecureRandom, sin algoritmos deprecados (MD5, SHA1, DES)
- **Platform:** Deep links validados, WebViews con JS deshabilitado por defecto, permisos minimos
- **Code:** ProGuard/R8 en release, sin API keys hardcodeadas, sin stack traces expuestos

## Comandos de Build

```bash
# Build
./gradlew assembleDebug

# Tests
./gradlew testDebugUnitTest

# Lint
./gradlew ktlintCheck
./gradlew ktlintFormat

# Tests de integracion
./gradlew connectedAndroidTest
```

## Convenciones

- **Idioma:** Todo en espanol (respuestas, comentarios, commits, docs)
- **Commits:** Conventional Commits obligatorio (`feat`, `fix`, `refactor`, `test`, `docs`, `chore`)
- **Strings:** Nunca hardcodeados — siempre en `strings.xml`
- **Naming:** Archivos PascalCase, funciones/variables camelCase, constantes UPPER_SNAKE_CASE
- **Cobertura minima:** ViewModels 80%, UseCases 90%, Mappers 100%

## Como Empezar

1. Abre el proyecto en Android Studio
2. Sincroniza Gradle
3. Ejecuta `./gradlew assembleDebug` para verificar que compila
4. Para desarrollar un nuevo feature, ejecuta `/spec FEAT-XXX` con el ticket correspondiente y sigue el flujo de 5 etapas definido en AGENTS.md
