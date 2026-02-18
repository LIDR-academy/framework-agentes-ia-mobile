# Reglas Específicas — Android

## Stack
- Kotlin 2.x, Jetpack Compose (Material 3), Hilt (DI)
- Retrofit + OkHttp (networking), Room (local DB)
- Coroutines + Flow (async), Coil (imágenes)
- Gradle con version catalogs (`gradle/libs.versions.toml`)

## Arquitectura
- Clean Architecture + MVI
- ViewModels exponen un solo `UiState` (sealed interface)
- Toda pantalla: Loading, Content, Error, Empty
- UseCases con `operator fun invoke()`
- Repositories inyectados como interface vía Hilt

## Compose
- Modifier order: layout → visual → interaction
- Previews para cada estado de UI
- No colores hex directamente — usar tokens del tema
- No strings hardcodeadas — usar `stringResource(R.string.xxx)`

## Testing
- JUnit 5, Mockk, Turbine (para Flows), Compose UI Testing
- Naming: `` fun `should return X when Y`() ``
- Cobertura: ViewModels 80%, UseCases 90%, Mappers 100%

## Room
- Migrations manuales (no auto-migration)
- Siempre crear migration file al cambiar esquema

## Build
- Lint: `./gradlew ktlintCheck` / `./gradlew ktlintFormat`
- Tests: `./gradlew testDebugUnitTest`
- Build: `./gradlew assembleDebug`
- Integración: `./gradlew connectedAndroidTest`

## Seguridad
- ProGuard/R8 habilitado en release
- android:exported=false por defecto
- network_security_config.xml: no cleartext, TLS 1.3
- API keys vía BuildConfig (no hardcoded)
- Datos sensibles en Android Keystore

## Gotchas
- El módulo `legacy-auth` usa XML Views — no migrar sin ticket explícito
- El endpoint /v1/users requiere header `X-App-Version`