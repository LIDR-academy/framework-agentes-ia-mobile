---
description: Genera una spec atómica desde un ticket. Produce spec.md, risks.md, tasks.md y qa_plan.md en docs/features/. Usar al iniciar cualquier feature nueva.
---

Actúa como el **Spec Agent** definido en AGENTS.md (§3.1).

Tu tarea es transformar el siguiente ticket en una especificación atómica y verificable:

$ARGUMENTS

## Proceso

1. **Analiza el ticket** — Extrae el objetivo, el contexto de negocio y las restricciones
2. **Define User Stories** — Formato: "Como [rol], quiero [acción], para [beneficio]"
3. **Escribe Acceptance Criteria** — Formato Given/When/Then para cada User Story
4. **Mapea los 4 estados de UI** — Loading, Content, Error, Empty. Para cada uno describe qué se muestra y qué lo dispara
5. **Identifica dependencias** — APIs necesarias, features prerequisito, equipos bloqueantes
6. **Define eventos de analytics** — Nombre del evento, cuándo se dispara, propiedades
7. **Identifica riesgos** — Técnicos, de negocio, de timeline. Cada uno con mitigación propuesta
8. **Crea el QA plan** — Happy paths, edge cases, error scenarios, dispositivos target

## Output

Crea la carpeta `docs/features/FEAT-XXX/` (usa el ID del ticket) y genera estos archivos:

### spec.md
```markdown
# FEAT-XXX: [Título]

## Resumen
[1-2 párrafos describiendo el feature]

## User Stories
- Como [rol], quiero [acción], para [beneficio]

## Acceptance Criteria
### AC-1: [Nombre]
- Given [contexto]
- When [acción]
- Then [resultado esperado]

## Estados de UI
| Estado | Qué se muestra | Trigger |
|--------|---------------|---------|
| Loading | [descripción] | [trigger] |
| Content | [descripción] | [trigger] |
| Error | [descripción] | [trigger] |
| Empty | [descripción] | [trigger] |

## Analytics
| Evento | Trigger | Propiedades |
|--------|---------|-------------|
| [nombre] | [cuándo] | [props] |

## Feature Flags
- [nombre_flag]: [descripción y rollout plan]

## Dependencias
| Dependencia | Estado | Owner |
|-------------|--------|-------|
| [API/Feature] | Disponible/Bloqueado | [equipo] |
```

### risks.md
Lista de riesgos con severidad (Alta/Media/Baja), impacto y mitigación.

### tasks.md
Desglose en vertical slices:
- Slice 1: Happy path + loading
- Slice 2: Empty + error + retry
- Slice 3: Analytics + feature flag + a11y + i18n

### qa_plan.md
Plan de QA con casos de prueba por categoría.

## Validación antes de terminar

Verifica que se cumplen TODOS estos gates:
- [ ] Todos los AC son testables (Given/When/Then completos)
- [ ] Los 4 estados de UI están definidos con triggers claros
- [ ] Analytics events tienen nombres consistentes
- [ ] Dependencias tienen estado y owner
- [ ] Riesgos tienen mitigación
- [ ] QA plan cubre happy path + edge cases + errors
- [ ] Tasks están organizados por vertical slice

Si algún gate no se cumple, complétalo antes de entregar.
