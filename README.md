# schema-validator

[![CI](https://github.com/philiprehberger/kt-schema-validator/actions/workflows/publish.yml/badge.svg)](https://github.com/philiprehberger/kt-schema-validator/actions/workflows/publish.yml)
[![Maven Central](https://img.shields.io/maven-central/v/com.philiprehberger/schema-validator)](https://central.sonatype.com/artifact/com.philiprehberger/schema-validator)
[![License](https://img.shields.io/github/license/philiprehberger/kt-schema-validator)](LICENSE)

Declarative data validation with composable rules and structured errors.

## Installation

### Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("com.philiprehberger:schema-validator:0.1.2")
}
```

### Maven

```xml
<dependency>
    <groupId>com.philiprehberger</groupId>
    <artifactId>schema-validator</artifactId>
    <version>0.1.2</version>
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

## License

MIT
