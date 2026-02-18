# UI Contract — LID-7: Pantalla de Noticias — Hacker News Top Stories

## Componentes identificados

### Nuevos (a crear)
| Componente | Tipo | Descripción |
|-----------|------|-------------|
| `NewsScreen` | Screen | Pantalla principal con Scaffold, TopAppBar y manejo de 4 estados |
| `NewsItemCard` | Card | Item individual de noticia con posición, título, score, autor, comentarios, dominio, tiempo |
| `NewsShimmerCard` | Placeholder | Skeleton card con shimmer effect para estado Loading |
| `NewsErrorContent` | Estado | Contenido centrado con icono, mensaje y botón Reintentar |
| `NewsEmptyContent` | Estado | Contenido centrado con mensaje y botón Refrescar |
| `PaginationLoadingItem` | Indicador | CircularProgressIndicator al final de la lista durante paginación |

### Reutilizables (del Design System)
| Componente Material 3 | Uso |
|----------------------|-----|
| `Scaffold` | Estructura base de la pantalla |
| `TopAppBar` (CenterAligned) | Header con título "Hacker News" y botón refresh |
| `Card` | Contenedor de cada noticia |
| `CircularProgressIndicator` | Indicadores de carga |
| `SnackbarHost` | Errores de refresh manteniendo datos previos |
| `PullToRefreshBox` | Pull to refresh nativo de Material 3 |
| `IconButton` | Botón de refresh en toolbar |
| `Button` / `OutlinedButton` | Botones de Reintentar/Refrescar |

## Mapeo a tokens del Design System

### Colores
| Elemento | Token | Valor |
|---------|-------|-------|
| Triángulo score (▲) | `HackerNewsOrange` | `#FF6600` |
| Número de posición | `MaterialTheme.colorScheme.primary` | `HackerNewsOrange` en dark |
| Título de noticia | `MaterialTheme.colorScheme.onSurface` | Default |
| Texto secundario (autor, dominio, tiempo) | `MaterialTheme.colorScheme.onSurfaceVariant` | `#94A3B8` en dark |
| Fondo de pantalla | `MaterialTheme.colorScheme.surface` | `#0F172A` en dark |
| Fondo de card | `MaterialTheme.colorScheme.surfaceVariant` | `#1E293B` en dark |
| Separador entre cards | `MaterialTheme.colorScheme.outlineVariant` | Divider sutil |
| Shimmer base | `MaterialTheme.colorScheme.surfaceVariant` | |
| Shimmer highlight | `MaterialTheme.colorScheme.surface` | |

### Tipografía
| Elemento | Token |
|---------|-------|
| Título en TopAppBar | `titleLarge` (20sp, Bold) |
| Título de noticia | `titleMedium` (16sp, SemiBold) |
| Score + autor (línea 2) | `bodyMedium` (14sp, Normal) |
| Comentarios + dominio + tiempo (línea 3) | `bodySmall` (12sp, Normal) |
| Número de posición | `titleMedium` (16sp, SemiBold) |
| Botón Reintentar/Refrescar | `labelLarge` (16sp, Bold) |

### Spacing
| Elemento | Valor |
|---------|-------|
| Padding interno de card | 16dp |
| Spacing entre líneas dentro de card | 4dp |
| Spacing entre cards (vertical) | 0dp (separador de 1dp) |
| Padding horizontal de la lista | 0dp (cards edge-to-edge con divider) |
| Separador entre items | HorizontalDivider, 1dp, `outlineVariant` |
| Gap entre número de posición y contenido | 12dp |
| Gap entre elementos inline (·) | 4dp (separado por " · ") |

### Iconos
| Elemento | Icono |
|---------|-------|
| Refresh (toolbar) | `Icons.Default.Refresh` |
| Score triangle | Texto "▲" con `HackerNewsOrange` |
| Comentarios | `Icons.AutoMirrored.Filled.Comment` o texto "💬" |
| Error (sin conexión) | `Icons.Default.WifiOff` |
| Empty | `Icons.Default.Info` |

## State Machine

| Estado | Composable | Trigger | Transiciones posibles |
|--------|-----------|---------|----------------------|
| Loading | `NewsShimmerCard` x5 | `onAppear` / primera carga | → Content, → Error, → Empty |
| Content | `LazyColumn` con `NewsItemCard` + pull-to-refresh | Datos cargados exitosamente | → Loading (refresh completo), → Error (refresh falla → snackbar) |
| Error | `NewsErrorContent` centrado | Fallo en carga inicial (red, API) | → Loading (retry) |
| Empty | `NewsEmptyContent` centrado | API retorna `[]` | → Loading (refresh) |

### Sub-estados durante Content
| Sub-estado | Descripción | Visual |
|-----------|-------------|--------|
| Refreshing | Pull to refresh activo | Indicador pull-to-refresh nativo |
| Paginating | Cargando más items | `PaginationLoadingItem` al final de la lista |
| PaginationError | Error al cargar más items | Item de error al final con botón retry |
| AllLoaded | Todas las noticias cargadas | Sin indicador al final |

## Checklist de accesibilidad

- [x] Content descriptions en todos los elementos interactivos
  - Card: "Noticia {posición}: {título}, {score} puntos por {autor}, {comentarios} comentarios"
  - Botón refresh toolbar: "Refrescar noticias"
  - Botón Reintentar: "Reintentar carga de noticias"
  - Botón Refrescar: "Refrescar lista de noticias"
- [x] Touch targets >= 48dp
  - Cards: toda el área es tappeable (height mínimo ~72dp)
  - Botón refresh toolbar: 48dp
  - Botones de acción: 48dp height mínimo
- [x] Color contrast >= 4.5:1
  - `#FF6600` (naranja) sobre `#1E293B` (card) → ratio 3.4:1 (WARN: usar solo para decorativo, no para texto informativo crítico)
  - Texto principal sobre surface → cumple
  - Texto secundario `#94A3B8` sobre `#1E293B` → ratio 4.6:1 (cumple)
- [x] Dynamic Type / scaled fonts: usar `sp` para todos los tamaños de texto
- [x] Orden de navegación lógico: posición → título → metadata → siguiente card

## Consideraciones Android (Material 3)

- `PullToRefreshBox` de Material 3 (reemplaza al deprecated `SwipeRefresh`)
- `LazyColumn` con `rememberLazyListState` para preservar scroll position
- Shimmer con `placeholder-material3` o implementación custom con `Brush.linearGradient` animado
- Edge-to-edge layout ya configurado en `MainActivity`
- Soporte para Dynamic Color (Material You) en Android 12+
