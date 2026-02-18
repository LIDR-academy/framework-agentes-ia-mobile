---
description: Valida que el código generado o modificado respeta Clean Architecture y los patrones definidos en CLAUDE.md. Usar al revisar PRs o después de generar features.
---

Actúa como el **Mobile Architect** definido en AGENTS.md (§3.3) en modo review.

Valida la arquitectura del código en: $ARGUMENTS

Si no se especifica ruta, analiza los archivos modificados:
```bash
git diff --name-only HEAD~1
```

## Validaciones obligatorias

### 1. Regla de dependencia
La regla fundamental de Clean Architecture: `presentation → domain ← data`

Busca y reporta violaciones:
- [ ] domain/ NO importa nada de presentation/ ni data/
- [ ] presentation/ NO importa directamente de data/
- [ ] data/ solo implementa interfaces definidas en domain/

Escanea los imports de cada archivo modificado y verifica que no cruzan los boundaries.

### 2. Estructura por feature
Cada feature debe seguir esta estructura:

```
feature-xxx/
  presentation/
    XxxScreen.kt / XxxView.swift          ← UI
    XxxViewModel.kt / XxxViewModel.swift  ← Lógica de presentación
    XxxUiState.kt / XxxUiState.swift      ← Estados
  domain/
    model/                                ← Entidades puras
    usecase/                              ← Casos de uso
    repository/                           ← Interfaces
  data/
    repository/                           ← Implementaciones
    remote/                               ← DTOs + API
    local/                                ← DAOs + DB entities
    mapper/                               ← Transformaciones
```

Reportar archivos que no encajan en esta estructura.

### 3. Contratos de datos
- [ ] ViewModels exponen UN solo UiState (sealed class / enum)
- [ ] UiState tiene los 4 estados: Loading, Content, Error, Empty
- [ ] Content tiene los datos necesarios para la UI (no models de domain directamente si requieren transformación)
- [ ] Error incluye mecanismo de retry

### 4. Dependencias
- [ ] ViewModels SOLO dependen de UseCases (no de Repositories directamente)
- [ ] UseCases reciben Repository como interface inyectada (no implementación concreta)
- [ ] DTOs no se exponen fuera de data/ (verificar que ningún Composable/View usa DTOs)
- [ ] No hay acceso directo a red o BD desde presentation/

### 5. UseCases
- [ ] Cada UseCase tiene UNA sola responsabilidad
- [ ] Usa operator fun invoke() para invocación limpia
- [ ] No contiene lógica de UI ni de framework
- [ ] Recibe dependencias por constructor injection

### 6. Nuevas dependencias
- [ ] No se agregaron dependencias nuevas sin justificación
- [ ] Si hay dependencias nuevas, verificar que están documentadas en arch.md
- [ ] Verificar que no duplican funcionalidad existente

## Formato de reporte

```
## Reporte de Arquitectura

### Estado general: PASS / FAIL

### Violaciones encontradas
| # | Tipo | Archivo | Detalle | Severidad |
|---|------|---------|---------|-----------|
| 1 | Dependencia | LoginViewModel.kt:5 | Importa directamente de data.remote | ALTA |

### Recomendaciones
- [Lista de mejoras sugeridas]

### Archivos revisados
- [Lista de archivos analizados]
```

Severidades:
- **ALTA** — Violación de regla de dependencia o estructura. Bloquea merge.
- **MEDIA** — Patrón no óptimo pero funcional. Sugiere fix.
- **BAJA** — Mejora de estilo o convención. Informativo.