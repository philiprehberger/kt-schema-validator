# schema-validator

[![Tests](https://github.com/philiprehberger/kt-schema-validator/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-schema-validator/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/schema-validator.svg)](https://central.sonatype.com/artifact/com.philiprehberger/schema-validator)
[![Last updated](https://img.shields.io/github/last-commit/philiprehberger/kt-schema-validator)](https://github.com/philiprehberger/kt-schema-validator/commits/main)

Declarative data validation with composable rules and structured errors.

## Installation

### Gradle (Kotlin DSL)

```kotlin
implementation("com.philiprehberger:schema-validator:0.1.3")
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>schema-validator</artifactId>
    <version>0.1.3</version>
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
