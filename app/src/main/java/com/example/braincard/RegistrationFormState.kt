package com.example.braincard

data class RegistrationFormState (
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val emailError: Int? = null,
    val telefonoError: Int? = null,
    val genereError:Int? = null,
    val isDataValid: Boolean = false
    )