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
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ModProfiloViewModel(application: Application) : ViewModel() {

    lateinit private var repository: UtenteRepository
    val registrationFormState = MutableLiveData<RegistrationFormState>()
    private val db = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance().currentUser
    init {

        val utenteDAO= BrainCardDatabase.getDatabase(application).utenteDao()
        repository= UtenteRepository(utenteDAO)

    }

    fun registrationDataChanged2(username: String, telefono: String, genere: String) {
        if (!isUserNameValid(username)) {
            registrationFormState.value = RegistrationFormState(usernameError = R.string.invalid_username)
        }else if(!isTelefonoValid(telefono)){
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

    private fun isTelefonoValid(telefono: String) : Boolean{
        val pattern = Regex("[0-9]+")
        return (pattern.matches(telefono) && telefono.length == 10)
    }
    private fun isGenereValid(genere: String) : Boolean{
        return genere.equals("maschio") || genere.equals("femmina") || genere.equals("altro")
    }
    fun updateUtente(nomeutente:String,nome: String,cognome: String,telefono: String,genere: String){
        val updateData = hashMapOf(
            "nomeutente" to nomeutente,
            "nome" to nome,
            "cognome" to cognome,
            "telefono" to telefono,
            "genere" to genere
        )
        db.collection("Utente").document(auth?.uid.toString()).set(updateData, SetOptions.merge())
    }


}