# Skill: Consumo Seguro de APIs

## Cuándo se activa
Nuevo endpoint o modificación de capa de networking.

## Qué hace
Genera la implementación completa para consumir un endpoint REST siguiendo Clean Architecture y las convenciones del proyecto.

## Inputs
- Método HTTP (GET, POST, PUT, DELETE, PATCH)
- Path del endpoint (ej: `/v1/users/{id}/profile`)
- Body de request (si aplica)
- Response body esperado

## Output generado (por plataforma)

### Android (Kotlin)
```
data/
  remote/
    dto/XxxRequestDto.kt       → Request DTO con @SerializedName
    dto/XxxResponseDto.kt      → Response DTO con @SerializedName
    XxxApiService.kt           → Interface Retrofit con @Headers
  mapper/
    XxxMapper.kt               → DTO ↔ Domain Model
  repository/
    XxxRepositoryImpl.kt       → Implementación con error handling
domain/
  model/Xxx.kt                 → Modelo de dominio (sin anotaciones)
  repository/XxxRepository.kt  → Interface del repository
  usecase/GetXxxUseCase.kt     → UseCase con operator invoke
```

### iOS (Swift)
```
Data/
  Remote/
    DTO/XxxRequestDTO.swift    → Codable request
    DTO/XxxResponseDTO.swift   → Codable response
    XxxAPIService.swift        → URLSession async/await
  Mapper/
    XxxMapper.swift            → DTO ↔ Domain Model
  Repository/
    XxxRepositoryImpl.swift    → Implementación con error handling
Domain/
  Model/Xxx.swift              → Modelo de dominio
  Repository/XxxRepository.swift → Protocol del repository
  UseCase/GetXxxUseCase.swift  → UseCase
```

## Reglas
- Headers obligatorios: `X-App-Version`, `Authorization` (si autenticado)
- Error handling por código HTTP: 401→refresh token, 429→backoff, 5xx→retry con exponential backoff
- DTOs NUNCA se exponen fuera de data/
- Mappers con tests al 100% de cobertura
- Timeout configurable (default 30s)
