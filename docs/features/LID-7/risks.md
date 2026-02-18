# Riesgos — LID-7: Pantalla de Noticias — Hacker News Top Stories

## Técnicos

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|:------------:|:-------:|------------|
| N+1 requests: obtener 20 items requiere 21 requests (1 lista + 20 detalles), lo que puede ser lento en redes débiles | Media | Alto | Requests en paralelo con `Semaphore(10)` / `TaskGroup(maxConcurrency: 10)`. Mostrar items conforme llegan, no esperar a todos. Cache en memoria para evitar re-fetch |
| API de HN sin rate limit documentado: riesgo de throttling si se hacen demasiados requests concurrentes | Baja | Medio | Limitar concurrencia a máximo 10 requests simultáneos. Implementar backoff exponencial si se detectan errores 429 |
| Campo `url` puede ser null en items tipo "Ask HN" / "Show HN": crash si no se maneja | Media | Alto | Validación explícita en mapper: si `url` es null, usar fallback `https://news.ycombinator.com/item?id={ID}`. Tests unitarios para ambos casos |
| Campo `time` es Unix timestamp en segundos (no milisegundos): cálculo incorrecto de tiempo relativo | Media | Medio | Converter explícito en mapper con multiplicación por 1000 para plataformas que esperan milisegundos. Tests unitarios con timestamps conocidos |
| Respuesta de API puede contener items de tipo no-story (job, poll, comment): UI inesperada | Baja | Bajo | Filtrar por `type == "story"` en el mapper. Ignorar items que no sean stories |
| Pérdida de scroll position al rotar pantalla o volver de WebView | Media | Medio | Usar `rememberLazyListState` (Compose) / `ScrollViewReader` (SwiftUI) para preservar posición. ViewModel retiene estado |
| Cache offline puede mostrar datos muy desactualizados | Baja | Bajo | Mostrar badge "Última actualización: hace X" cuando se muestran datos de cache. Auto-refresh al recuperar conexión |

## De negocio

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|:------------:|:-------:|------------|
| API de Hacker News es pública y no controlada: puede cambiar sin aviso | Baja | Alto | Mappers robustos con campos opcionales. Tests de integración contra la API real como smoke test. Monitorear disponibilidad |
| El contenido de HN puede incluir títulos ofensivos o inapropiados | Baja | Bajo | Fuera de scope para esta demo. En producción real, implementar filtrado de contenido |

## De timeline

| Riesgo | Probabilidad | Impacto | Mitigación |
|--------|:------------:|:-------:|------------|
| Sin dependencias bloqueantes: API pública, Design System disponible, Room/SwiftData configurados | N/A | N/A | No hay bloqueos identificados. Todas las dependencias están disponibles |
| Complejidad de la estrategia de paginación + concurrencia puede extender el timeline | Media | Medio | Implementar primero sin optimizaciones (requests secuenciales), luego optimizar en Slice 1. Vertical slices permiten entregar valor incremental |