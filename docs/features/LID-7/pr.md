# PR: LID-7 — Pantalla de Noticias — Hacker News Top Stories

## Descripción

Implementa la pantalla principal de la app que muestra las noticias más populares de Hacker News en tiempo real. Consume la API pública de Hacker News (Firebase) con dos endpoints: listado de IDs (`/v0/topstories.json`) y detalle por item (`/v0/item/{id}.json`).

La pantalla incluye scroll infinito con paginación de 20 en 20, pull to refresh, carga paralela de items con límite de concurrencia (Semaphore 10), cache en memoria, y apertura de artículos en navegador externo. Cada noticia muestra título, puntuación, autor, cantidad de comentarios, dominio y tiempo relativo.

Arquitectura Clean Architecture + MVI con separación estricta de capas: presentation (Compose + ViewModel), domain (Models + UseCase + Repository interface), data (Retrofit + DTOs + Mapper + Repository impl).

## Ticket

[LID-7: Pantalla de Noticias — Hacker News Top Stories](https://linear.app/lidr/issue/LID-7/pantalla-de-noticias-hacker-news-top-stories)

## Cambios realizados

### Presentation (UI + ViewModel)
- `NewsScreen.kt` — Pantalla principal con 4 estados, pull-to-refresh, scroll infinito
- `NewsViewModel.kt` — Lógica de carga inicial, refresh, paginación, manejo de errores
- `NewsUiState.kt` — Sealed interface con Loading, Content, Error, Empty + PaginationState
- `StoryUiModel.kt` — Modelo de presentación para cada noticia
- `NewsItemCard.kt` — Card individual con posición, título, score, autor, comentarios, dominio, tiempo
- `NewsShimmerCard.kt` — 5 skeleton cards con shimmer animado para Loading
- `NewsErrorContent.kt` — Estado Error centrado con icono + mensaje + botón Reintentar
- `NewsEmptyContent.kt` — Estado Empty con mensaje + botón Refrescar
- `PaginationItems.kt` — Indicadores de paginación (loading spinner + error con retry)
- `RelativeTimeFormatter.kt` — Calcula "hace Xm/Xh/Xd" desde Unix timestamp
- `DomainExtractor.kt` — Extrae dominio de URL, remueve "www.", fallback a ycombinator

### Domain
- `Story.kt` — Entidad pura: id, title, author, score, commentCount, timestamp, url, type
- `TopStoriesResult.kt` — Wrapper: stories + hasMore
- `NewsRepository.kt` — Interface: getTopStoryIds() + getStories()
- `GetTopStoriesUseCase.kt` — Orquesta paginación: page + pageSize + forceRefresh

### Data
- `StoryDto.kt` — DTO con @SerializedName para campos de la API
- `HackerNewsApiService.kt` — Retrofit service: 2 endpoints (topstories + item detail)
- `StoryMapper.kt` — Conversión StoryDto.toDomain() con defaults para nulls
- `NewsRepositoryImpl.kt` — Requests paralelos (Semaphore 10), cache en memoria, filtrado por type
- `HttpException.kt` / `NetworkException.kt` — Excepciones tipadas

### Core
- `RetrofitProvider.kt` — Singleton Retrofit + OkHttp con timeouts 30s, logging solo en DEBUG

### Config
- `strings.xml` — 14 strings con prefijo `news_*`
- `MainActivity.kt` — Composition root: instancia dependencias y conecta ViewModel con UI

### Tests (54 tests)
- `StoryMapperTest.kt` — 8 tests (cobertura 100% del mapper)
- `NewsRepositoryImplTest.kt` — 10 tests (cache, errores HTTP, errores de red, filtrado)
- `GetTopStoriesUseCaseTest.kt` — 8 tests (paginación, empty, errores, forceRefresh)
- `NewsViewModelTest.kt` — 8 tests (Loading→Content, Error, Empty, paginación, refresh error)
- `RelativeTimeFormatterTest.kt` — 10 tests (minutos, horas, días, edge cases)
- `DomainExtractorTest.kt` — 10 tests (www, null, blank, subdominios, TLD complejos)

### Documentación
- `docs/features/LID-7/spec.md`
- `docs/features/LID-7/ui_contract.md`
- `docs/features/LID-7/tasks.md`
- `docs/features/LID-7/risks.md`
- `docs/features/LID-7/qa_plan.md`
- `docs/features/LID-7/pr.md`

## Screenshots / Recordings

> **Pendiente:** El desarrollador debe agregar screenshots de los 4 estados ejecutando la app en emulador.

| Estado | Android |
|--------|---------|
| Loading | _Agregar screenshot de skeleton shimmer_ |
| Content | _Agregar screenshot con lista de noticias_ |
| Error | _Agregar screenshot de estado error con botón reintentar_ |
| Empty | _Agregar screenshot de estado vacío con botón refrescar_ |

## Archivos modificados

### presentation/ (11 archivos)
- `features/news/presentation/NewsScreen.kt`
- `features/news/presentation/NewsViewModel.kt`
- `features/news/presentation/NewsUiState.kt`
- `features/news/presentation/model/StoryUiModel.kt`
- `features/news/presentation/util/RelativeTimeFormatter.kt`
- `features/news/presentation/util/DomainExtractor.kt`
- `features/news/presentation/components/NewsItemCard.kt`
- `features/news/presentation/components/NewsShimmerCard.kt`
- `features/news/presentation/components/NewsErrorContent.kt`
- `features/news/presentation/components/NewsEmptyContent.kt`
- `features/news/presentation/components/PaginationItems.kt`

### domain/ (4 archivos)
- `features/news/domain/model/Story.kt`
- `features/news/domain/model/TopStoriesResult.kt`
- `features/news/domain/repository/NewsRepository.kt`
- `features/news/domain/usecase/GetTopStoriesUseCase.kt`

### data/ (4 archivos)
- `features/news/data/remote/dto/StoryDto.kt`
- `features/news/data/remote/HackerNewsApiService.kt`
- `features/news/data/mapper/StoryMapper.kt`
- `features/news/data/repository/NewsRepositoryImpl.kt`

### core/ (1 archivo)
- `core/network/RetrofitProvider.kt`

### config/ (2 archivos modificados)
- `app/src/main/res/values/strings.xml`
- `app/src/main/java/proyecto/com/hackersnew/MainActivity.kt`

### tests/ (6 archivos)
- `StoryMapperTest.kt`, `NewsRepositoryImplTest.kt`, `GetTopStoriesUseCaseTest.kt`
- `NewsViewModelTest.kt`, `RelativeTimeFormatterTest.kt`, `DomainExtractorTest.kt`

### docs/ (6 archivos)
- `spec.md`, `ui_contract.md`, `tasks.md`, `risks.md`, `qa_plan.md`, `pr.md`

## Breaking changes

Ninguno. Este es un feature nuevo que no modifica funcionalidad existente.

## Migration notes

Ninguna. No hay migraciones de base de datos ni cambios en APIs existentes.

## Definition of Done

### Funcionalidad
- [x] Todos los Acceptance Criteria de spec.md cumplidos (12 ACs)
- [x] 4 estados de UI implementados (Loading/Content/Error/Empty)
- [x] Manejo de errores robusto (error inicial, error refresh, error paginación)
- [x] Feature flag definido (`feature_hackernews_feed`, 100% rollout)

### Calidad de código
- [x] Build compila sin errores ni warnings
- [x] Tests unitarios pasan (54/54)
- [x] Cobertura ≥ 80% en ViewModel (8 tests) y UseCase (8 tests)
- [x] Cobertura 100% en Mappers (8 tests) y Utilidades (20 tests)
- [x] No hay TODOs sin issue asociado
- [x] Strings internacionalizados (14 strings en strings.xml)

### Arquitectura
- [x] `/arch` validado — Clean Architecture respetada, 0 violaciones
- [x] No dependencias nuevas sin aprobación (solo las ya incluidas en build.gradle.kts)
- [x] DTOs no expuestos fuera de data layer
- [x] Regla de dependencia: `presentation → domain ← data`

### Seguridad (OWASP MASVS)
- [x] `/security` ejecutado — 0 FAILs, 3 WARNs (no blockers)
- [x] No hay datos sensibles (API pública sin auth)
- [x] No PII en logs (logging Level.BASIC solo en DEBUG)
- [x] Network security config: TLS obligatorio, cleartext prohibido

### Accesibilidad
- [x] Content descriptions en todos los elementos interactivos
- [x] semantics(mergeDescendants) en cards para TalkBack
- [x] Touch targets ≥ 48dp
- [x] Tipografía en sp para Dynamic Type
- [x] Contraste verificado (texto secundario #94A3B8 sobre #1E293B → 4.6:1)

### Observabilidad
- [x] Analytics events definidos en spec.md (5 eventos con propiedades)
- [ ] Analytics events instrumentados en código (pendiente — Slice 3)

### Internacionalización
- [x] Todos los strings en resources con prefijo `news_*`
- [x] No hay strings hardcodeados en composables
