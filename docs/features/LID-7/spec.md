# LID-7: Pantalla de Noticias — Hacker News Top Stories

## Resumen

Pantalla principal de la app que muestra las noticias más populares de Hacker News en tiempo real. Consume la API pública de Hacker News (Firebase) para obtener el listado de top stories y el detalle de cada noticia. Incluye paginación por scroll infinito (20 items por página), pull to refresh, y apertura de artículos en WebView/navegador externo.

Esta pantalla sirve como caso de estudio del flujo completo de desarrollo asistido por agentes de IA, demostrando consumo de API, manejo de estados, paginación y buenas prácticas de arquitectura Clean Architecture + MVI/MVVM.

## User Stories

### US-1: Ver listado de noticias populares
**Como** usuario de la app,
**quiero** ver las noticias más populares de Hacker News,
**para** mantenerme informado de las tendencias tech.

**Acceptance Criteria:**

#### AC-1: Carga inicial del listado
- **Given** que abro la app
- **When** se carga la pantalla principal
- **Then** veo un listado con las 20 noticias más populares

#### AC-2: Información visible por noticia
- **Given** que las noticias están cargadas
- **When** veo cada item del listado
- **Then** veo: título, autor (`by`), puntuación (`score`), número de comentarios (`descendants`), y tiempo relativo ("hace 2h", "hace 1d")

#### AC-3: Dominio visible para noticias con URL externa
- **Given** que una noticia tiene campo `url` con valor no nulo
- **When** la veo en el listado
- **Then** se muestra el dominio del link sin "www." (ej: "github.com", "nytimes.com")

#### AC-4: Dominio por defecto para noticias sin URL
- **Given** que una noticia NO tiene `url` (Ask HN, Show HN)
- **When** la veo en el listado
- **Then** se muestra "news.ycombinator.com" como dominio

### US-2: Abrir una noticia
**Como** usuario de la app,
**quiero** poder abrir una noticia para leer el contenido completo,
**para** leer el artículo original.

**Acceptance Criteria:**

#### AC-5: Abrir noticia con URL externa
- **Given** que toco una noticia con URL externa
- **When** se abre
- **Then** navego a un WebView o al navegador externo con la URL del artículo

#### AC-6: Abrir noticia sin URL (Ask HN)
- **Given** que toco una noticia sin URL (Ask HN, Show HN)
- **When** se abre
- **Then** navego a `https://news.ycombinator.com/item?id={ID}`

### US-3: Pull to refresh
**Como** usuario de la app,
**quiero** poder refrescar la lista tirando hacia abajo,
**para** ver las noticias más recientes.

**Acceptance Criteria:**

#### AC-7: Refresh exitoso
- **Given** que estoy en el listado
- **When** hago pull to refresh
- **Then** se recarga la lista de top stories con datos frescos

#### AC-8: Indicador de refresh
- **Given** que estoy recargando
- **When** el refresh está en progreso
- **Then** veo un indicador de carga (pull-to-refresh spinner)

#### AC-9: Error en refresh con datos previos
- **Given** que falla el refresh (sin conexión o error de API)
- **When** el refresh termina con error
- **Then** veo un mensaje de error (snackbar/toast) con opción de reintentar Y mantengo las noticias anteriores visibles

### US-4: Paginación (scroll infinito)
**Como** usuario de la app,
**quiero** que se carguen más noticias conforme hago scroll,
**para** explorar más contenido sin presionar un botón.

**Acceptance Criteria:**

#### AC-10: Carga automática al llegar al final
- **Given** que veo las primeras 20 noticias
- **When** hago scroll y llego cerca del final de la lista
- **Then** se cargan las siguientes 20 noticias automáticamente

#### AC-11: Indicador de paginación
- **Given** que se están cargando más noticias
- **When** el loading de paginación está en progreso
- **Then** veo un indicador de carga al final de la lista

#### AC-12: Fin del listado
- **Given** que se cargaron todas las noticias disponibles (hasta 500)
- **When** llego al final
- **Then** NO se muestra más indicador de carga

## Estados de UI

| Estado  | Qué se muestra | Trigger |
|---------|---------------|---------|
| Loading | 5 skeleton cards con shimmer effect | Primera carga de la pantalla (antes de recibir cualquier dato) |
| Content | Lista de noticias con: número de posición, título, puntuación (triangulo naranja), autor, comentarios, dominio, tiempo relativo. Pull-to-refresh habilitado. Indicador de paginación al final si hay más items | Top stories IDs + detalles de items cargados exitosamente |
| Error   | Mensaje de error centrado + icono de conexión + botón "Reintentar" | Fallo en la carga inicial de top stories (sin conexión, timeout, error HTTP) |
| Empty   | Mensaje "No hay noticias disponibles" + botón "Refrescar" | API devuelve array vacío de top stories |

## Estrategia de carga

1. GET `/v0/topstories.json` → obtener hasta 500 IDs
2. Tomar los primeros 20 IDs (página 1)
3. Hacer 20 requests en **paralelo** a `/v0/item/{ID}.json` con límite de concurrencia de 10 simultáneos
4. Mostrar resultados conforme llegan (no esperar a que terminen todos)
5. Al hacer scroll al final, tomar los siguientes 20 IDs y repetir pasos 3-4

### Optimizaciones
- Cache en memoria de items ya cargados (evitar re-fetch al scroll up)
- IDs cacheados en Room/SwiftData para soporte offline
- Si no hay red, mostrar cache con badge "Última actualización: hace X"

## Analytics Events

| Evento | Trigger | Propiedades |
|--------|---------|-------------|
| `news_list_viewed` | Pantalla se muestra por primera vez | `story_count: Int` |
| `news_list_refreshed` | Pull to refresh completado | `success: Bool` |
| `news_item_tapped` | Usuario toca una noticia | `story_id: Int`, `position: Int`, `score: Int` |
| `news_list_paginated` | Se cargan más noticias (nueva página) | `page: Int`, `total_loaded: Int` |
| `news_item_opened_external` | Se abre URL en browser/WebView | `story_id: Int`, `domain: String` |

## Feature Flags

| Flag | Default | Descripción |
|------|---------|-------------|
| `feature_hackernews_feed` | `true` | Habilita la pantalla de noticias de Hacker News. Rollout al 100% desde el inicio (es demo). |

## Dependencias

| Dependencia | Estado | Owner | Notas |
|-------------|--------|-------|-------|
| API Hacker News (`/v0/topstories.json`) | Disponible | Pública (Firebase) | Sin autenticación, sin rate limit formal |
| API Hacker News (`/v0/item/{ID}.json`) | Disponible | Pública (Firebase) | Campos opcionales: `url` puede ser null |
| Design System (Cards, Skeleton, Pull-to-refresh) | Disponible | Equipo de diseño | Componentes estándar del proyecto |
| Room (Android) / SwiftData (iOS) | Disponible | Infraestructura | Para cache offline de items |
| Módulo de networking (OkHttp / URLSession) | Disponible | Infraestructura | Usar interceptors comunes del proyecto |

## Notas de diseño

- Cada card tiene padding de 16dp/16pt
- Número de posición (#1, #2, #3) usa color accent del Design System
- Puntuación con triángulo naranja (color: `#FF6600`, el naranja de HN)
- Tiempo relativo calculado en presentación: "hace Xm", "hace Xh", "hace Xd"
- Separadores sutiles entre cards (1dp, color divider del Design System)
- Skeleton shimmer para loading: mínimo 5 placeholders