# UI Contract — FEAT-XXX: [Título del Feature]

## Componentes

### Nuevos
| Componente | Plataforma | Descripción |
|------------|------------|-------------|
| `XxxScreen` | Android | [Descripción] |
| `XxxView` | iOS | [Descripción] |

### Reutilizados del Design System
| Componente | Uso en este feature |
|------------|---------------------|
| [Nombre] | [Cómo se usa] |

## Design System Tokens

### Colores
| Token | Uso |
|-------|-----|
| `colorPrimary` | [Dónde se usa] |

### Tipografía
| Token | Uso |
|-------|-----|
| `titleLarge` | [Dónde se usa] |

### Spacing
| Token | Uso |
|-------|-----|
| `spacingMd` (16dp/pt) | [Dónde se usa] |

## State Machine

| Estado  | Android (Composable)      | iOS (SwiftUI View)   | Trigger           |
|---------|---------------------------|----------------------|--------------------|
| Loading | XxxLoadingScreen()        | XxxLoadingView       | onAppear / init    |
| Content | XxxContentScreen()        | XxxContentView       | datos cargados     |
| Error   | XxxErrorScreen()          | XxxErrorView         | API failure        |
| Empty   | XxxEmptyScreen()          | XxxEmptyView         | data.isEmpty       |

## Accesibilidad

- [ ] Content descriptions (Android) / Accessibility labels (iOS)
- [ ] Dynamic Type / Scaled fonts support
- [ ] Touch targets mínimos: 48dp (Android) / 44pt (iOS)
- [ ] Color contrast ratio ≥ 4.5:1
- [ ] Screen reader navigation order definido

## Consideraciones por plataforma

### Android
- Material 3 components utilizados: [lista]
- Compose Previews por estado

### iOS
- HIG compliance: [notas]
- SwiftUI Previews por estado