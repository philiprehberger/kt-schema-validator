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

    @Test fun `crossField validation`() {
        data class DateRange(val start: Int, val end: Int)
        val s = schema<DateRange> {
            field("start", DateRange::start) { range(1..100) }
            field("end", DateRange::end) { range(1..100) }
            crossField("start must be before end") { it.start < it.end }
        }
        assertTrue(s.validate(DateRange(1, 10)) is ValidationResult.Valid)
        val result = s.validate(DateRange(10, 5))
        assertTrue(result is ValidationResult.Invalid)
        assertTrue((result as ValidationResult.Invalid).errors.any { it.message == "start must be before end" })
    }

    @Test fun `conditionalField applies rules only when condition is true`() {
        data class Form(val type: String, val email: String)
        val s = schema<Form> {
            field("type", Form::type) { required() }
            conditionalField("email", Form::email, { it.type == "email" }) {
                required()
                email()
            }
        }
        // When type is "email", email must be valid
        val r1 = s.validate(Form("email", ""))
        assertTrue(r1 is ValidationResult.Invalid)
        // When type is not "email", email is not validated
        val r2 = s.validate(Form("phone", ""))
        assertTrue(r2 is ValidationResult.Valid)
    }

    @Test fun `compose merges two schemas`() {
        val nameSchema = schema<User> {
            field("name", User::name) { required() }
        }
        val emailSchema = schema<User> {
            field("email", User::email) { email() }
        }
        val combined = nameSchema.compose(emailSchema)
        val result = combined.validate(User("", "bad", 30))
        assertTrue(result is ValidationResult.Invalid)
        assertEquals(2, (result as ValidationResult.Invalid).errors.size)
    }

    @Test fun `custom error message on minLength`() {
        val s = schema<User> {
            field("name", User::name) { minLength(5, "Name too short") }
        }
        val result = s.validate(User("Al", "a@b.com", 30))
        assertTrue(result is ValidationResult.Invalid)
        assertEquals("Name too short", (result as ValidationResult.Invalid).errors[0].message)
    }
}
