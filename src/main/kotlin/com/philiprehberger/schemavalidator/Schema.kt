package com.philiprehberger.schemavalidator

/** A validation schema for type [T]. */
public class Schema<T> internal constructor(private val rules: List<(T) -> List<ValidationError>>) {
    /** Validate a value against this schema. */
    public fun validate(value: T): ValidationResult<T> {
        val errors = rules.flatMap { it(value) }
        return if (errors.isEmpty()) ValidationResult.Valid(value) else ValidationResult.Invalid(errors)
    }
}

/** Build a validation schema. */
public fun <T> schema(block: SchemaBuilder<T>.() -> Unit): Schema<T> {
    val builder = SchemaBuilder<T>()
    builder.block()
    return Schema(builder.rules)
}

public class SchemaBuilder<T> {
    internal val rules = mutableListOf<(T) -> List<ValidationError>>()

    /** Add a field validation rule using a getter function. */
    public fun <V> field(name: String, getter: (T) -> V, block: FieldRuleBuilder<V>.() -> Unit) {
        val fb = FieldRuleBuilder<V>(name)
        fb.block()
        rules.add { value -> fb.validate(getter(value)) }
    }
}

public class FieldRuleBuilder<V>(private val fieldName: String) {
    private val checks = mutableListOf<(V) -> ValidationError?>()

    internal fun validate(value: V): List<ValidationError> = checks.mapNotNull { it(value) }

    /** Value must not be null or empty string. */
    public fun required() { checks.add { v -> if (v == null || (v is String && v.isBlank())) ValidationError(fieldName, "is required") else null } }
    /** String must be at least [min] characters. */
    public fun minLength(min: Int) { checks.add { v -> if (v is String && v.length < min) ValidationError(fieldName, "must be at least $min characters") else null } }
    /** String must be at most [max] characters. */
    public fun maxLength(max: Int) { checks.add { v -> if (v is String && v.length > max) ValidationError(fieldName, "must be at most $max characters") else null } }
    /** Number must be in [range]. */
    public fun range(range: IntRange) { checks.add { v -> if (v is Int && v !in range) ValidationError(fieldName, "must be in range $range") else null } }
    /** String must be a valid email. */
    public fun email() { checks.add { v -> if (v is String && !v.matches(Regex(".+@.+\\..+"))) ValidationError(fieldName, "must be a valid email") else null } }
    /** String must match [regex]. */
    public fun matches(regex: Regex, message: String = "must match pattern") { checks.add { v -> if (v is String && !v.matches(regex)) ValidationError(fieldName, message) else null } }
    /** Value must be one of [values]. */
    public fun oneOf(vararg values: V) { checks.add { v -> if (v !in values) ValidationError(fieldName, "must be one of ${values.toList()}") else null } }
    /** Custom validation. */
    public fun custom(message: String, predicate: (V) -> Boolean) { checks.add { v -> if (!predicate(v)) ValidationError(fieldName, message) else null } }
}
