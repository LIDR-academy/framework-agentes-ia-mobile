# Tasks — LID-7: Pantalla de Noticias — Hacker News Top Stories

## Slice 1: Happy path + loading

| # | Tarea | Archivos | Complejidad | Done |
|---|-------|----------|:-----------:|:----:|
| 1.1 | Crear modelos de dominio (`Story`) con campos: id, title, by, score, descendants, time, url, type | `feature-news/domain/model/Story.kt` | S | [ ] |
| 1.2 | Crear interface del repository en domain | `feature-news/domain/repository/NewsRepository.kt` | S | [ ] |
| 1.3 | Crear UseCases: `GetTopStoriesUseCase` (obtiene IDs + detalles paginados) | `feature-news/domain/usecase/GetTopStoriesUseCase.kt` | M | [ ] |
| 1.4 | Crear DTOs para la API (`TopStoriesResponse`, `StoryDto`) | `feature-news/data/remote/dto/StoryDto.kt` | S | [ ] |
| 1.5 | Crear API service con Retrofit (2 endpoints: topstories + item detail) | `feature-news/data/remote/HackerNewsApiService.kt` | S | [ ] |
| 1.6 | Crear mappers: `StoryDto` → `Story` | `feature-news/data/mapper/StoryMapper.kt` | S | [ ] |
| 1.7 | Implementar `NewsRepositoryImpl` con requests en paralelo (Semaphore 10) y cache en memoria | `feature-news/data/repository/NewsRepositoryImpl.kt` | L | [ ] |
| 1.8 | Crear `NewsUiState` sealed interface (Loading, Content, Error, Empty) | `feature-news/presentation/NewsUiState.kt` | S | [ ] |
| 1.9 | Crear `NewsViewModel` con carga inicial + paginación + pull to refresh | `feature-news/presentation/NewsViewModel.kt` | L | [ ] |
| 1.10 | Crear UI: `NewsScreen` con lista, skeleton loading (5 cards shimmer), y contenido | `feature-news/presentation/NewsScreen.kt` | L | [ ] |
| 1.11 | Crear `NewsItemCard` composable (título, score, autor, comentarios, dominio, tiempo relativo) | `feature-news/presentation/components/NewsItemCard.kt` | M | [ ] |
| 1.12 | Crear utilidad de tiempo relativo (`RelativeTimeFormatter`) | `feature-news/presentation/util/RelativeTimeFormatter.kt` | S | [ ] |
| 1.13 | Crear utilidad de extracción de dominio (`DomainExtractor`) | `feature-news/presentation/util/DomainExtractor.kt` | S | [ ] |
| 1.14 | Configurar módulo Hilt (DI): proveer ApiService, Repository, UseCases | `feature-news/di/NewsModule.kt` | M | [ ] |
| 1.15 | Tests unitarios: `StoryMapper`, `RelativeTimeFormatter`, `DomainExtractor` | `feature-news/test/...` | M | [ ] |
| 1.16 | Tests unitarios: `GetTopStoriesUseCase` | `feature-news/test/domain/...` | M | [ ] |
| 1.17 | Tests unitarios: `NewsViewModel` (estados Loading → Content, paginación) | `feature-news/test/presentation/...` | L | [ ] |

**Commit:** `feat(news): implementar happy path con listado y loading`

---

## Slice 2: Empty + error + retry

| # | Tarea | Archivos | Complejidad | Done |
|---|-------|----------|:-----------:|:----:|
| 2.1 | Implementar estado Empty en `NewsScreen` (mensaje + botón Refrescar) | `feature-news/presentation/NewsScreen.kt` | S | [ ] |
| 2.2 | Implementar estado Error en `NewsScreen` (icono + mensaje + botón Reintentar) | `feature-news/presentation/NewsScreen.kt` | S | [ ] |
| 2.3 | Manejar error de refresh con snackbar (mantener datos previos visibles) | `feature-news/presentation/NewsScreen.kt`, `NewsViewModel.kt` | M | [ ] |
| 2.4 | Manejar error de paginación (mostrar item de error al final de la lista con retry) | `feature-news/presentation/NewsScreen.kt`, `NewsViewModel.kt` | M | [ ] |
| 2.5 | Implementar manejo de errores HTTP en repository (timeout, sin conexión, errores de servidor) | `feature-news/data/repository/NewsRepositoryImpl.kt` | M | [ ] |
| 2.6 | Tests unitarios: `NewsViewModel` (estados Error, Empty, retry, error de paginación) | `feature-news/test/presentation/...` | M | [ ] |
| 2.7 | Tests unitarios: manejo de errores en repository | `feature-news/test/data/...` | M | [ ] |

**Commit:** `feat(news): agregar manejo de errores y estado vacío`

---

## Slice 3: Analytics + feature flag + a11y + i18n

| # | Tarea | Archivos | Complejidad | Done |
|---|-------|----------|:-----------:|:----:|
| 3.1 | Instrumentar eventos de analytics: `news_list_viewed`, `news_list_refreshed`, `news_item_tapped`, `news_list_paginated`, `news_item_opened_external` | `feature-news/presentation/NewsViewModel.kt` | M | [ ] |
| 3.2 | Wrappear pantalla con feature flag `feature_hackernews_feed` | `feature-news/presentation/NewsScreen.kt` | S | [ ] |
| 3.3 | Agregar content descriptions a todos los elementos interactivos y cards | `feature-news/presentation/NewsScreen.kt`, `NewsItemCard.kt` | M | [ ] |
| 3.4 | Extraer todos los strings a `strings.xml` con convención `news_*` | `res/values/strings.xml`, componentes UI | M | [ ] |
| 3.5 | Verificar touch targets mínimos (48dp) en todos los elementos interactivos | `feature-news/presentation/components/...` | S | [ ] |
| 3.6 | Verificar soporte de Dynamic Type / font scaling | `feature-news/presentation/...` | S | [ ] |
| 3.7 | Tests: verificar que analytics events se disparan correctamente | `feature-news/test/presentation/...` | M | [ ] |

**Commit:** `feat(news): agregar analytics, accesibilidad e i18n`
