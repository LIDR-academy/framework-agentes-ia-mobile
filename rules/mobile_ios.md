# Reglas Específicas — iOS

## Stack
- Swift 5.9+, SwiftUI, Combine
- URLSession + async/await (networking)
- SwiftData o CoreData (local DB)
- Swift Package Manager (dependencias)
- Keychain wrapper para almacenamiento seguro

## Arquitectura
- Clean Architecture + MVVM
- ViewModels exponen un solo estado (enum con associated values)
- Toda pantalla: loading, content, error, empty
- UseCases con `func execute()` o `func callAsFunction()`
- Repositories inyectados como protocol

## SwiftUI
- ViewModifier extraction para estilos reutilizables
- Previews para cada estado de UI
- No colores hex directamente — usar tokens del Design System
- No strings hardcodeadas — usar Localizable.strings / String Catalogs

## Testing
- XCTest, Swift Testing, ViewInspector (para SwiftUI)
- Naming: `func test_shouldReturnX_whenY()`
- Cobertura: ViewModels 80%, UseCases 90%, Mappers 100%

## Build
- Build: `xcodebuild build -scheme App -destination 'platform=iOS Simulator,name=iPhone 16'`
- Tests: `xcodebuild test -scheme App -destination 'platform=iOS Simulator,name=iPhone 16'`
- Lint: `swiftlint` / `swiftlint --fix`

## Seguridad
- ATS (App Transport Security) habilitado — no cleartext
- Datos sensibles en iOS Keychain
- API keys vía xcconfig (no hardcoded)
- Universal Links para deep links

## Gotchas
- SwiftData solo disponible a partir de iOS 17 — verificar min deployment target
- NUNCA modificar archivos .pbxproj con Claude Code — crear archivos y agregarlos manualmente en Xcode