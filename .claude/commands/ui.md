---
description: Traduce un diseño (screenshot pegado o MCP Figma) a código nativo (Jetpack Compose / SwiftUI). Usar cuando se construya UI desde diseños.
---

Actúa como el **Design Translator** definido en AGENTS.md (§3.2).

Tu tarea es traducir el diseño proporcionado a código nativo siguiendo el Design System del proyecto.

$ARGUMENTS

## Proceso

1. **Analiza el diseño** — Identifica todos los componentes visibles, su jerarquía, spacing, colores y tipografía
2. **Mapea a Design System** — Para cada elemento visual, busca si existe un componente reutilizable en el proyecto. Si no existe, créalo siguiendo los patrones existentes
3. **Identifica los 4 estados** — Loading (skeleton/shimmer), Content (lo que se ve en el diseño), Error (con botón de retry), Empty (con CTA)
4. **Genera código por plataforma**

## Android (Jetpack Compose)

- Usar componentes de Material 3
- Spacing con Arrangement y Padding usando valores del Design System (dp)
- Colores de MaterialTheme.colorScheme
- Typography de MaterialTheme.typography
- Generar @Preview para CADA estado (Loading, Content, Error, Empty)
- Content descriptions para accesibilidad en todos los elementos interactivos
- Modifier.semantics para screen readers

## iOS (SwiftUI)

- Componentes nativos de SwiftUI
- Compliance con Human Interface Guidelines
- Dynamic Type support obligatorio (no tamaños fijos de fuente)
- Generar #Preview para CADA estado
- Accessibility labels y traits en todos los elementos interactivos
- VoiceOver navigation order verificado

## Output esperado

Para cada plataforma, genera:

1. **Screen principal** — El Composable / View con los 4 estados
2. **UiState** — Sealed class (Kotlin) / Enum (Swift) con los estados
3. **Componentes extraídos** — Si hay elementos que se repiten, extraerlos como componentes reutilizables
4. **Previews** — Una preview por cada estado

## Checklist de accesibilidad (obligatorio)

- [ ] Content descriptions / accessibility labels presentes
- [ ] Touch targets ≥ 48dp (Android) / 44pt (iOS)
- [ ] Color contrast ≥ 4.5:1
- [ ] Dynamic Type / scaled fonts support
- [ ] Orden de navegación lógico para screen readers

## Nota

Si no se proporciona diseño (ni imagen pegada ni MCP Figma activo), pide al usuario que pegue un screenshot con Ctrl+V o que proporcione la URL de Figma.