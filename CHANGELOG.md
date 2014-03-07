# Changelog

## [0.1.0] - 2026-03-18

### Added

- `schema<T> {}` DSL for defining validation schemas

- Built-in rules: required, minLength, maxLength, range, email, matches, oneOf

- Custom validation with `custom(message) { predicate }`

- Structured errors with field path and message

- `ValidationResult` sealed type (Valid/Invalid)
