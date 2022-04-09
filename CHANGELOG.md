# Changelog

## 0.2.0 (2026-03-31)

- Add `crossField()` for validating relationships between fields
- Add `conditionalField()` for rules that only apply when a condition is met
- Add `Schema.compose()` for merging two schemas into one
- Add custom error message overloads for `minLength()` and `maxLength()`

## 0.1.4 (2026-03-31)

- Standardize README to 3-badge format with emoji Support section
- Update CI checkout action to v5 for Node.js 24 compatibility
- Add GitHub issue templates, dependabot config, and PR template

## 0.1.3 (2026-03-22)

- Fix README compliance (badge label, installation format), standardize CHANGELOG

## 0.1.2 (2026-03-20)

- Standardize README: fix title, badges, version sync, remove Requirements section

## 0.1.1 (2026-03-18)

- Upgrade to Kotlin 2.0.21 and Gradle 8.12
- Enable explicitApi() for stricter public API surface
- Add issueManagement to POM metadata

## 0.1.0 (2026-03-18)

- `schema<T> {}` DSL for defining validation schemas
- Built-in rules: required, minLength, maxLength, range, email, matches, oneOf
- Custom validation with `custom(message) { predicate }`
- Structured errors with field path and message
- `ValidationResult` sealed type (Valid/Invalid)
