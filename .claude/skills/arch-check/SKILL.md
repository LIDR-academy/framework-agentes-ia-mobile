# Skill: Validación de Clean Architecture

## Cuándo se activa
Review de PRs, generación de features, o al ejecutar `/arch`.

## Qué hace
Valida que el código respeta la regla de dependencia de Clean Architecture y los patrones obligatorios del proyecto.

## Reglas que valida

### Dependencias entre capas
```
presentation → domain ← data
```
- [ ] domain/ NO importa de presentation/ ni data/
- [ ] presentation/ NO importa directamente de data/
- [ ] DTOs NO se exponen fuera de data/

### ViewModels
- [ ] Exponen un solo UiState (sealed class / enum)
- [ ] Solo dependen de UseCases (no de repositories directamente)
- [ ] No contienen lógica de negocio (delegada a UseCases)

### UseCases
- [ ] Una sola responsabilidad (operator invoke / execute)
- [ ] Reciben Repository interface (no implementación concreta)
- [ ] No dependen de otros UseCases (salvo composición explícita en arch.md)

### Repositories
- [ ] Interface definida en domain/
- [ ] Implementación en data/
- [ ] Inyectados como interface vía DI (Hilt / manual)

### Mappers
- [ ] Explícitos entre capas: DTO ↔ Domain Model ↔ Entity
- [ ] Tests al 100% de cobertura
- [ ] No usan lógica de negocio (solo transformación de datos)

### Dependencias externas
- [ ] No hay dependencias nuevas sin justificación en arch.md
- [ ] No hay imports de framework en domain/ (excepto coroutines/combine)

## Output esperado

```
## Resultado de Validación Arquitectónica

### VIOLACIONES (bloquean merge)
- [VIOLACIÓN] domain/ importa de data/ — archivo:línea

### ADVERTENCIAS
- [ADVERTENCIA] ViewModel accede a Repository directamente — archivo:línea

### OK
- [OK] Regla de dependencia respetada
- [OK] UiState correctamente definido
```
