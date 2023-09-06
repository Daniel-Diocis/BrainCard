package com.example.braincard

import com.example.braincard.factories.ChangePasswordViewModelFactory

data class RegistrationFormState (
    val usernameError: Int? = null,
    val passwordError: Int? = null,
    val passwordError2: Int? = null,
    val emailError: Int? = null,
    val telefonoError: Int? = null,
    val genereError:Int? = null,
    val isDataValid: Boolean = false
    )