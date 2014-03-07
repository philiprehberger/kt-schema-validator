package com.philiprehberger.schemavalidator

/** A validation error for a specific field. */
public data class ValidationError(public val field: String, public val message: String)
