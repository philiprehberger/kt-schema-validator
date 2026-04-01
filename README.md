# schema-validator

[![Tests](https://github.com/philiprehberger/kt-schema-validator/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-schema-validator/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/schema-validator.svg)](https://central.sonatype.com/artifact/com.philiprehberger/schema-validator)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-schema-validator)](https://github.com/philiprehberger/kt-schema-validator/commits/main)

Declarative data validation with composable rules and structured errors.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:schema-validator:0.2.0")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>schema-validator</artifactId>
    <version>0.2.0</version>
</dependency>
```

## Usage

```kotlin
import com.philiprehberger.schemavalidator.*

data class User(val name: String, val email: String, val age: Int)

val userSchema = schema<User> {
    field("name", User::name) { required(); minLength(2) }
    field("email", User::email) { required(); email() }
    field("age", User::age) { range(13..120) }
}

when (val result = userSchema.validate(user)) {
    is ValidationResult.Valid -> process(result.value)
    is ValidationResult.Invalid -> result.errors.forEach { println("${it.field}: ${it.message}") }
}
```

### Cross-Field Validation

```kotlin
data class DateRange(val start: Int, val end: Int)

val dateSchema = schema<DateRange> {
    field("start", DateRange::start) { range(1..365) }
    field("end", DateRange::end) { range(1..365) }
    crossField("start must be before end") { it.start < it.end }
}
```

### Conditional Rules

```kotlin
data class ContactForm(val type: String, val email: String)

val formSchema = schema<ContactForm> {
    field("type", ContactForm::type) { required() }
    conditionalField("email", ContactForm::email, { it.type == "email" }) {
        required()
        email()
    }
}
```

### Schema Composition

```kotlin
val baseSchema = schema<User> { field("name", User::name) { required() } }
val extendedSchema = schema<User> { field("email", User::email) { email() } }
val fullSchema = baseSchema.compose(extendedSchema)
```

## API

| Function / Class | Description |
|------------------|-------------|
| `schema<T> { }` | Build a validation schema |
| `field(name, getter) { }` | Add field validation rules |
| `required()` | Field must not be null/blank |
| `minLength(n)` / `maxLength(n)` | String length constraints |
| `range(intRange)` | Number range constraint |
| `email()` | Basic email format check |
| `matches(regex)` | Regex pattern match |
| `custom(message) { predicate }` | Custom validation rule |
| `crossField(message) { }` | Cross-field validation |
| `conditionalField(name, getter, condition) { }` | Conditional field validation |
| `Schema.compose(other)` | Combine two schemas |
| `minLength(n, message)` | Min length with custom error |
| `maxLength(n, message)` | Max length with custom error |
| `ValidationResult.Valid` / `Invalid` | Sealed result type |

## Development

```bash
./gradlew test
./gradlew build
```

## Support

If you find this project useful:

⭐ [Star the repo](https://github.com/philiprehberger/kt-schema-validator)

🐛 [Report issues](https://github.com/philiprehberger/kt-schema-validator/issues?q=is%3Aissue+is%3Aopen+label%3Abug)

💡 [Suggest features](https://github.com/philiprehberger/kt-schema-validator/issues?q=is%3Aissue+is%3Aopen+label%3Aenhancement)

❤️ [Sponsor development](https://github.com/sponsors/philiprehberger)

🌐 [All Open Source Projects](https://philiprehberger.com/open-source-packages)

💻 [GitHub Profile](https://github.com/philiprehberger)

🔗 [LinkedIn Profile](https://www.linkedin.com/in/philiprehberger)

## License

[MIT](LICENSE)
