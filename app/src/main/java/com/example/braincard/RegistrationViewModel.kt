package com.example.braincard

import android.app.Application
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.braincard.data.model.Utente
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.UtenteRepository
import com.example.braincard.ui.login.LoginFormState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationViewModel(application: Application) : ViewModel() {

    lateinit private var repository: UtenteRepository
    val registrationFormState = MutableLiveData<RegistrationFormState>()
    private val db = FirebaseFirestore.getInstance()
    init {

        val utenteDAO= BrainCardDatabase.getDatabase(application).utenteDao()
        repository= UtenteRepository(utenteDAO)

    }
    suspend fun registraUtente(utenteId: String, nomeUtente: String, password: String, nome: String, cognome: String, email: String, telefono: String, genere: String) {
        val nuovoUtente = Utente(
            utenteId, nomeUtente, password, nome, cognome, email, telefono, genere
        )
        // Passa l'oggetto nuovoUtente al repository per l'inserimento nel database
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertUtente(nuovoUtente)


        }
    }
    fun registrationDataChanged(username: String, password: String, telefono: String, genere: String,email: String) {
        if (!isUserNameValid(username)) {
            registrationFormState.value = RegistrationFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            registrationFormState.value = RegistrationFormState(passwordError = R.string.invalid_password)
        }else if(!isEmailValid(email)){
            registrationFormState.value = RegistrationFormState(emailError = R.string.invalid_email)}
        else if(!isTelefonoValid(telefono)){
            registrationFormState.value = RegistrationFormState(telefonoError = R.string.invalid_telefono)
        }else if(!isGenereValid(genere)){
            registrationFormState.value = RegistrationFormState(genereError  = R.string.invalid_genere)
        }
        else {
            registrationFormState.value = RegistrationFormState(isDataValid = true)
        }
    }
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains("@")) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isTelefonoValid(telefono: String) : Boolean{
        val pattern = Regex("[0-9]+")
        return (pattern.matches(telefono) && telefono.length == 10)
    }
    private fun isGenereValid(genere: String) : Boolean{
        return genere.equals("maschio") || genere.equals("femmina") || genere.equals("altro")
    }
    private fun isEmailValid(email: String) : Boolean {
        val pattern = Regex("^\\S+@\\S+\\.\\S+\$")  // Un'espressione regolare per il formato dell'email
        return pattern.matches(email)
    }


}