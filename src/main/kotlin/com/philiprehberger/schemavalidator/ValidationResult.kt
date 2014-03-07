package com.philiprehberger.schemavalidator

/** Result of schema validation. */
public sealed class ValidationResult<out T> {
    public data class Valid<T>(public val value: T) : ValidationResult<T>()
    public data class Invalid(public val errors: List<ValidationError>) : ValidationResult<Nothing>()
}
