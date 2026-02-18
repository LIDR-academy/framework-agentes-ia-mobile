# Arquitectura — FEAT-XXX: [Título del Feature]

## Diagrama de dependencias

```
presentation/
  XxxScreen ──→ XxxViewModel ──→ GetXxxUseCase ──→ XxxRepository (interface)
                                                          ↑
data/                                                     │
  XxxRepositoryImpl ─────────────────────────────────────┘
    ├── XxxApiService (Retrofit/URLSession)
    ├── XxxDao (Room/CoreData)
    └── XxxMapper (DTO ↔ Domain ↔ Entity)
```

## Estructura de archivos

```
feature-xxx/
  presentation/
    XxxScreen.kt / XxxView.swift
    XxxViewModel.kt / XxxViewModel.swift
    XxxUiState.kt / XxxUiState.swift
  domain/
    model/Xxx.kt / Xxx.swift
    usecase/GetXxxUseCase.kt / GetXxxUseCase.swift
    repository/XxxRepository.kt / XxxRepository.swift (interface/protocol)
  data/
    repository/XxxRepositoryImpl.kt / XxxRepositoryImpl.swift
    remote/
      dto/XxxResponseDto.kt / XxxResponseDTO.swift
      XxxApiService.kt / XxxAPIService.swift
    local/
      XxxDao.kt / XxxDAO.swift (si aplica)
      XxxEntity.kt / XxxEntity.swift (si aplica)
    mapper/XxxMapper.kt / XxxMapper.swift
```

## Contrato de datos

### Android (Kotlin)
```kotlin
sealed interface XxxUiState {
    object Loading : XxxUiState
    data class Content(val data: Xxx) : XxxUiState
    data class Error(val message: String, val retry: () -> Unit) : XxxUiState
    object Empty : XxxUiState
}
```

### iOS (Swift)
```swift
enum XxxUiState {
    case loading
    case content(Xxx)
    case error(String, retry: () -> Void)
    case empty
}
```

## Dependencias

### Existentes (ya en el proyecto)
| Dependencia | Uso |
|-------------|-----|
| [Nombre] | [Para qué] |

### Nuevas (requieren aprobación)
| Dependencia | Justificación |
|-------------|---------------|
| [Nombre] | [Por qué es necesaria] |

## Decisiones técnicas (ADR)

### Decisión 1: [Título]
**Contexto:** [Por qué se necesita decidir]
**Decisión:** [Qué se decidió]
**Alternativas consideradas:** [Qué más se evaluó]
**Consecuencias:** [Impacto positivo/negativo]
