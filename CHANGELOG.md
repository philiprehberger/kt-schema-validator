# Changelog
## 0.1.2 (2026-03-20)- Standardize README: fix title, badges, version sync, remove Requirements section

## [0.1.1] - 2026-03-18

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## [0.1.0] - 2026-03-18

### Added

- `schema<T> {}` DSL for defining validation schemas

- Built-in rules: required, minLength, maxLength, range, email, matches, oneOf

- Custom validation with `custom(message) { predicate }`

- Structured errors with field path and message

- `ValidationResult` sealed type (Valid/Invalid)
