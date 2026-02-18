---
description: Genera código para consumir un endpoint REST de forma segura con DTOs, mappers, repository y tests. Formato de uso - /api METHOD /path {body esperado}
---

Actúa como el **Code Agent** definido en AGENTS.md (§3.4), usando el skill de api-consume.

Genera el código completo para consumir el siguiente endpoint:

$ARGUMENTS

## Proceso

### 1. Parsea el endpoint
Extrae: método HTTP, path, parámetros, body esperado, response esperado.
Si falta información del response, infiere una estructura razonable y documéntala como asumida.

### 2. Genera Data Layer (por plataforma)

#### Android (Retrofit + OkHttp)

**DTO de Response:**
```kotlin
// data/remote/dto/XxxResponseDto.kt
data class XxxResponseDto(
    @SerializedName("field_name") val fieldName: String,
    // ... mapear todos los campos del response
)
```

**API Service:**
```kotlin
// data/remote/XxxApiService.kt
interface XxxApiService {
    @GET("/path")  // o @POST, @PUT, etc.
    suspend fun getXxx(): Response<XxxResponseDto>
}
```

**Mapper:**
```kotlin
// data/mapper/XxxMapper.kt
fun XxxResponseDto.toDomain(): XxxModel {
    return XxxModel(
        // mapeo explícito campo por campo
    )
}
```

**Repository Implementation:**
```kotlin
// data/repository/XxxRepositoryImpl.kt
class XxxRepositoryImpl @Inject constructor(
    private val apiService: XxxApiService
) : XxxRepository {
    override suspend fun getXxx(): Result<XxxModel> {
        return try {
            val response = apiService.getXxx()
            if (response.isSuccessful) {
                Result.success(response.body()!!.toDomain())
            } else {
                Result.failure(mapHttpError(response.code()))
            }
        } catch (e: IOException) {
            Result.failure(NetworkException(e))
        }
    }
}
```

#### iOS (URLSession + async/await)

**DTO de Response:**
```swift
// data/remote/dto/XxxResponseDTO.swift
struct XxxResponseDTO: Codable {
    let fieldName: String

    enum CodingKeys: String, CodingKey {
        case fieldName = "field_name"
    }
}
```

**API Client:**
```swift
// data/remote/XxxAPIClient.swift
protocol XxxAPIClient {
    func getXxx() async throws -> XxxResponseDTO
}
```

**Mapper:**
```swift
// data/mapper/XxxMapper.swift
extension XxxResponseDTO {
    func toDomain() -> XxxModel {
        XxxModel(
            // mapeo explícito
        )
    }
}
```

**Repository Implementation:**
```swift
// data/repository/XxxRepositoryImpl.swift
final class XxxRepositoryImpl: XxxRepository {
    private let apiClient: XxxAPIClient

    func getXxx() async -> Result<XxxModel, AppError> {
        do {
            let dto = try await apiClient.getXxx()
            return .success(dto.toDomain())
        } catch let error as URLError {
            return .failure(.network(error))
        } catch {
            return .failure(.unknown(error))
        }
    }
}
```

### 3. Genera Domain Layer

**Model:**
```
domain/model/XxxModel.kt / XxxModel.swift
```
Entidad pura sin anotaciones de framework.

**Repository Interface:**
```
domain/repository/XxxRepository.kt / XxxRepository.swift
```

**UseCase (si aplica):**
```
domain/usecase/GetXxxUseCase.kt / GetXxxUseCase.swift
```

### 4. Manejo de errores (OBLIGATORIO)

Implementar estos casos:
- HTTP 401 → refresh token → retry → logout si falla
- HTTP 403 → error de permisos, mostrar en UI
- HTTP 404 → recurso no encontrado
- HTTP 429 → exponential backoff con retry
- HTTP 500+ → error de servidor con retry
- IOException/URLError → verificar conectividad, retry

### 5. Genera Tests

**Tests del Mapper** — Cobertura 100%. Verificar cada campo.
**Tests del Repository** — Mock del API service/client. Test por código HTTP.
**Tests del UseCase** — Mock del Repository. Happy path + error.

### 6. Ejecuta validación de seguridad

Al terminar, ejecuta mentalmente el checklist de `/owasp-review` sobre el código generado:
- ¿Tokens en Keystore/Keychain? (no en memoria plana)
- ¿TLS configurado?
- ¿Logging no expone datos sensibles?
- ¿Certificate pinning presente?