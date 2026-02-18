# Skill: Traducción de Diseño a Código Nativo

## Cuándo se activa
Construcción de UI desde Figma (MCP o screenshots) o cuando se ejecuta `/ui`.

## Qué hace
Convierte un diseño visual en código nativo (Jetpack Compose / SwiftUI) mapeado al Design System del proyecto, generando el ui_contract.md y los componentes necesarios.

## Inputs
- Diseño: Figma MCP / screenshot pegado / URL de Figma
- spec.md del feature (para conocer los estados de UI)
- Design System tokens del proyecto

## Output generado

### ui_contract.md
- Componentes identificados (nuevos vs reutilizables del Design System)
- Mapeo a tokens: colores, tipografía, spacing, elevación
- State machine completa con los 4 estados obligatorios:

| Estado  | Android (Composable)   | iOS (SwiftUI View) | Trigger         |
|---------|------------------------|---------------------|-----------------|
| Loading | XxxLoadingScreen()     | XxxLoadingView      | onAppear / init |
| Content | XxxContentScreen()     | XxxContentView      | datos cargados  |
| Error   | XxxErrorScreen()       | XxxErrorView        | API failure     |
| Empty   | XxxEmptyScreen()       | XxxEmptyView        | data.isEmpty    |

### Código generado

**Android (Kotlin + Compose):**
- `XxxScreen.kt` — Composable principal con state handling
- Composables internos por estado
- Preview functions para cada estado

**iOS (Swift + SwiftUI):**
- `XxxView.swift` — View principal con state handling
- Subviews por estado
- Preview providers para cada estado

## Checklist de accesibilidad (obligatorio)
- [ ] Content descriptions (Android) / Accessibility labels (iOS)
- [ ] Dynamic Type / Scaled fonts support
- [ ] Touch targets mínimos: 48dp (Android) / 44pt (iOS)
- [ ] Color contrast ratio ≥ 4.5:1
- [ ] Screen reader navigation order definido

## Reglas
- Usar SOLO tokens del Design System (no valores hardcodeados)
- Strings en resources (strings.xml / Localizable.strings)
- Modifier order: layout → visual → interaction (Android)
- ViewModifier extraction para estilos reutilizables (iOS)
- No usar colores hex directamente en el código