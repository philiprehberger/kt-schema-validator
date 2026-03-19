package com.philiprehberger.schemavalidator

import kotlin.test.*

data class User(val name: String, val email: String, val age: Int)

class SchemaTest {
    private val userSchema = schema<User> {
        field("name", User::name) { required(); minLength(2); maxLength(50) }
        field("email", User::email) { required(); email() }
        field("age", User::age) { range(13..120) }
    }

    @Test fun `valid data`() {
        val result = userSchema.validate(User("Alice", "alice@test.com", 30))
        assertTrue(result is ValidationResult.Valid)
    }
    @Test fun `invalid name`() {
        val result = userSchema.validate(User("", "alice@test.com", 30))
        assertTrue(result is ValidationResult.Invalid)
        assertTrue((result as ValidationResult.Invalid).errors.any { it.field == "name" })
    }
    @Test fun `invalid email`() {
        val result = userSchema.validate(User("Alice", "not-email", 30))
        assertTrue(result is ValidationResult.Invalid)
    }
    @Test fun `invalid age`() {
        val result = userSchema.validate(User("Alice", "a@b.com", 5))
        assertTrue(result is ValidationResult.Invalid)
    }
    @Test fun `multiple errors collected`() {
        val result = userSchema.validate(User("", "bad", 5))
        assertTrue(result is ValidationResult.Invalid)
        assertTrue((result as ValidationResult.Invalid).errors.size >= 3)
    }
    @Test fun `custom rule`() {
        val s = schema<String> { field("value", { it }) { custom("must start with A") { it.startsWith("A") } } }
        assertTrue(s.validate("Alice") is ValidationResult.Valid)
        assertTrue(s.validate("Bob") is ValidationResult.Invalid)
    }
}
