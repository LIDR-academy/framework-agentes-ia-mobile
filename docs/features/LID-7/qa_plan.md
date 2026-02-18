# QA Plan — LID-7: Pantalla de Noticias — Hacker News Top Stories

## Happy Paths

| # | Escenario | Pasos | Resultado esperado |
|---|-----------|-------|---------------------|
| HP-1 | Carga inicial exitosa | 1. Abrir la app con conexión a internet 2. Esperar carga | Se muestran 20 noticias con título, autor, score, comentarios, dominio y tiempo relativo |
| HP-2 | Información completa por item | 1. Verificar cualquier noticia en el listado | Muestra: número de posición, título, triángulo naranja con score, "by {autor}", icono comentarios con cantidad, dominio sin "www.", tiempo relativo |
| HP-3 | Abrir noticia con URL externa | 1. Tocar una noticia que tiene URL (ej: github.com) | Se abre WebView o navegador externo con la URL del artículo |
| HP-4 | Abrir noticia tipo Ask HN (sin URL) | 1. Tocar una noticia "Ask HN" o "Show HN" sin URL | Se abre `https://news.ycombinator.com/item?id={ID}` |
| HP-5 | Pull to refresh | 1. Estando en el listado, hacer pull to refresh 2. Esperar recarga | Se muestra indicador de refresh, la lista se actualiza con datos frescos |
| HP-6 | Scroll infinito - primera página adicional | 1. Hacer scroll hasta el final de las 20 noticias | Se muestra indicador de carga al final, se cargan 20 noticias más automáticamente |
| HP-7 | Scroll infinito - múltiples páginas | 1. Hacer scroll para cargar 3+ páginas | Cada página carga 20 noticias adicionales correctamente, sin duplicados |
| HP-8 | Skeleton loading en primera carga | 1. Forzar cierre de app 2. Abrir la app | Se muestran 5 skeleton cards con shimmer antes de que carguen los datos |

## Edge Cases

| # | Escenario | Pasos | Resultado esperado |
|---|-----------|-------|---------------------|
| EC-1 | Noticia con score 0 | 1. Encontrar o simular noticia con score 0 | Se muestra "0 pts" correctamente, sin crash |
| EC-2 | Noticia sin comentarios (descendants = 0 o null) | 1. Verificar noticia sin comentarios | Se muestra "0" comentarios, no "null" |
| EC-3 | Título muy largo (> 2 líneas) | 1. Verificar noticia con título extenso | El título se trunca con ellipsis o se muestra en múltiples líneas sin romper el layout |
| EC-4 | Dominio con subdominios largos | 1. Verificar noticia con URL tipo `blog.some-very-long-domain.co.uk` | Se muestra el host completo sin "www.", layout no se rompe |
| EC-5 | Tiempo relativo: noticia recién publicada (< 1 min) | 1. Verificar noticia publicada hace segundos | Se muestra "hace 1m" o "ahora" |
| EC-6 | Tiempo relativo: noticia antigua (> 30 días) | 1. Verificar noticia muy antigua | Se muestra "hace Xd" correctamente |
| EC-7 | Scroll rápido (fling) durante carga | 1. Hacer scroll muy rápido mientras se paginan items | No hay crash, items se renderizan correctamente al detenerse |
| EC-8 | Rotación de pantalla durante carga | 1. Iniciar carga 2. Rotar el dispositivo | Estado se preserva, no se reinicia la carga desde cero |
| EC-9 | Volver de WebView al listado | 1. Abrir noticia 2. Presionar back | Se vuelve al listado en la misma posición de scroll |
| EC-10 | Fin del listado (todas las noticias cargadas) | 1. Hacer scroll hasta cargar las ~500 noticias | Se muestra fin de lista, no se intenta cargar más |
| EC-11 | Pull to refresh mientras se está paginando | 1. Iniciar scroll infinito 2. Inmediatamente hacer pull to refresh | Se cancela la paginación, se refresca toda la lista desde cero |
| EC-12 | Item de API retorna tipo no-story (job, poll) | 1. Simular respuesta con type != "story" | El item se filtra y no se muestra en la lista |

## Error Scenarios

| # | Escenario | Pasos | Resultado esperado |
|---|-----------|-------|---------------------|
| ER-1 | Sin conexión en carga inicial | 1. Desactivar red 2. Abrir la app | Estado Error: icono de conexión + mensaje de error + botón "Reintentar" |
| ER-2 | Sin conexión en carga inicial con cache disponible | 1. Cargar noticias exitosamente 2. Cerrar app 3. Desactivar red 4. Abrir app | Se muestran noticias del cache con badge "Última actualización: hace X" |
| ER-3 | Sin conexión durante pull to refresh | 1. Tener noticias cargadas 2. Desactivar red 3. Hacer pull to refresh | Snackbar/toast con error + noticias anteriores se mantienen visibles |
| ER-4 | Sin conexión durante paginación | 1. Tener 20 noticias cargadas 2. Desactivar red 3. Hacer scroll al final | Indicador de error al final de la lista con botón "Reintentar" |
| ER-5 | API timeout en carga inicial | 1. Simular timeout de red 2. Abrir la app | Estado Error con botón "Reintentar" |
| ER-6 | API devuelve 500 en topstories | 1. Simular error 500 | Estado Error con botón "Reintentar" |
| ER-7 | API devuelve 500 en item detail | 1. Simular error 500 en algunos items | Los items que fallan se omiten, los exitosos se muestran |
| ER-8 | API devuelve array vacío en topstories | 1. Simular respuesta vacía `[]` | Estado Empty: "No hay noticias disponibles" + botón "Refrescar" |
| ER-9 | API devuelve JSON malformado | 1. Simular respuesta con JSON inválido | Estado Error con botón "Reintentar", sin crash |
| ER-10 | Reintentar después de error | 1. Provocar error (ER-1) 2. Restaurar conexión 3. Tocar "Reintentar" | Se carga exitosamente, transición de Error → Loading → Content |
| ER-11 | Error parcial en carga de items (algunos OK, algunos fallan) | 1. Simular que 5 de 20 items fallan | Se muestran los 15 exitosos, los fallidos se omiten sin romper la numeración |

## Dispositivos Target

| Dispositivo | OS | Prioridad | Notas |
|-------------|-----|:---------:|-------|
| Pixel 7 | Android 14 | Alta | Referencia Android moderna |
| Samsung Galaxy S23 | Android 13 | Alta | Fabricante más popular |
| Pixel 4a | Android 12 | Media | Pantalla más pequeña |
| Samsung Galaxy A14 | Android 13 | Media | Gama baja, verificar rendimiento de shimmer y paginación |
| iPhone 15 | iOS 17 | Alta | Referencia iOS moderna |
| iPhone 13 | iOS 16 | Alta | Generación anterior popular |
| iPhone SE 3 | iOS 16 | Media | Pantalla pequeña, verificar layout de cards |
| iPad Air | iPadOS 17 | Baja | Verificar layout en tablet (si aplica) |

## Accesibilidad

| # | Verificación | Resultado esperado |
|---|-------------|---------------------|
| A11Y-1 | VoiceOver (iOS) / TalkBack (Android) navega la lista | Cada card se lee completa: posición, título, score, autor, comentarios, dominio, tiempo |
| A11Y-2 | Dynamic Type / Font scaling al máximo | Layout no se rompe, texto es legible, cards se expanden verticalmente |
| A11Y-3 | Touch targets de cards | Área tappeable >= 48dp (Android) / 44pt (iOS) |
| A11Y-4 | Contraste de colores | Texto sobre fondo cumple ratio >= 4.5:1 (incluyendo texto naranja del score) |
| A11Y-5 | Screen reader en estado Loading | Anuncia "Cargando noticias" |
| A11Y-6 | Screen reader en estado Error | Anuncia mensaje de error y botón Reintentar |
| A11Y-7 | Screen reader en estado Empty | Anuncia mensaje vacío y botón Refrescar |
| A11Y-8 | Botón Reintentar/Refrescar accesible por teclado | Focusable y activable sin touch |

## Analytics

| # | Verificación | Resultado esperado |
|---|-------------|---------------------|
| AN-1 | Evento `news_list_viewed` | Se dispara al mostrar la pantalla, con `story_count` correcto |
| AN-2 | Evento `news_list_refreshed` | Se dispara al completar pull to refresh, con `success: true/false` |
| AN-3 | Evento `news_item_tapped` | Se dispara al tocar noticia, con `story_id`, `position`, `score` correctos |
| AN-4 | Evento `news_list_paginated` | Se dispara al cargar nueva página, con `page` y `total_loaded` correctos |
| AN-5 | Evento `news_item_opened_external` | Se dispara al abrir URL, con `story_id` y `domain` correctos |
| AN-6 | No duplicación de eventos | Eventos no se disparan doble al rotar pantalla o recomposición |