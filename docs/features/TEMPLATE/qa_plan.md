# QA Plan — FEAT-XXX: [Título del Feature]

## Happy Paths

| # | Escenario | Pasos | Resultado esperado |
|---|-----------|-------|---------------------|
| HP-1 | [Descripción] | 1. [Paso] 2. [Paso] | [Resultado] |

## Edge Cases

| # | Escenario | Pasos | Resultado esperado |
|---|-----------|-------|---------------------|
| EC-1 | [Descripción] | 1. [Paso] 2. [Paso] | [Resultado] |

## Error Scenarios

| # | Escenario | Pasos | Resultado esperado |
|---|-----------|-------|---------------------|
| ER-1 | Sin conexión | 1. Desactivar red 2. [Acción] | Error state con retry |
| ER-2 | API timeout | 1. Simular timeout | Error state con retry |
| ER-3 | API 401 | 1. Token expirado 2. [Acción] | Refresh token → retry |
| ER-4 | API 500 | 1. Server error | Error state con retry |

## Dispositivos Target

| Dispositivo | OS | Prioridad |
|-------------|-----|:---------:|
| Pixel 7 | Android 14 | Alta |
| Samsung Galaxy S23 | Android 13 | Alta |
| iPhone 15 | iOS 17 | Alta |
| iPhone SE 3 | iOS 16 | Media |

## Accesibilidad

- [ ] VoiceOver (iOS) / TalkBack (Android) navega correctamente
- [ ] Dynamic Type / font scaling funciona
- [ ] Touch targets ≥ 48dp / 44pt
- [ ] Color contrast ≥ 4.5:1