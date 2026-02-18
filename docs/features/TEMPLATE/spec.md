# Spec — FEAT-XXX: [Título del Feature]

## Resumen
[1-2 párrafos describiendo el feature]

## User Stories

### US-1: [Título]
**Como** [rol],
**quiero** [acción],
**para** [beneficio].

**Acceptance Criteria:**

**Given** [precondición]
**When** [acción del usuario]
**Then** [resultado esperado]

## Estados de UI

| Estado  | Descripción                | Trigger           |
|---------|----------------------------|--------------------|
| Loading | [Qué se muestra]          | [Cuándo se activa] |
| Content | [Qué se muestra]          | [Cuándo se activa] |
| Error   | [Qué se muestra + retry]  | [Cuándo se activa] |
| Empty   | [Qué se muestra + CTA]    | [Cuándo se activa] |

## Analytics Events

| Evento              | Trigger            | Propiedades           |
|---------------------|--------------------|-----------------------|
| `screen_xxx_viewed` | Pantalla visible   | `source`, `user_type` |
| `xxx_action_tapped` | Tap en CTA         | `item_id`             |

## Feature Flags

| Flag            | Default | Descripción           |
|-----------------|---------|------------------------|
| `ff_xxx_enabled` | false   | [Qué habilita]        |

## Dependencias

| Dependencia       | Estado      | Notas            |
|-------------------|-------------|-------------------|
| API /v1/xxx       | Disponible  |                   |
| Design en Figma   | En progreso | ETA: [fecha]      |
