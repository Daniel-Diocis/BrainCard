package com.example.braincard

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.braincard.database.UtenteDAO
import com.example.braincard.database.UtenteRepository
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.braincard.databinding.FragmentRegistrationBinding
import com.example.braincard.factories.FlashcardStudioViewModelFactory
import com.example.braincard.factories.RegistrationViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegistrationFragment : Fragment() {

    companion object {
        fun newInstance() = RegistrationFragment()
    }

    private lateinit var viewModel: RegistrationViewModel
    private lateinit var binding: FragmentRegistrationBinding
    private var utenteId: String = generateRandomString(20)
    private lateinit var nomeUtente: String
    private lateinit var password: String
    private lateinit var nome: String
    private lateinit var cognome: String
    private lateinit var email: String
    private lateinit var telefono: String
    private lateinit var genere: String
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this, RegistrationViewModelFactory(requireActivity().application)
        ).get(RegistrationViewModel::class.java)
        binding = FragmentRegistrationBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.registrationFormState.observe(viewLifecycleOwner,
            Observer { registrationFormState ->
                if (registrationFormState == null) {
                    return@Observer
                }
               binding.buttonSalvaUtente.isEnabled = registrationFormState.isDataValid
                registrationFormState.usernameError?.let {
                    binding.textNomeUtente.error = getString(it)
                }
                registrationFormState.passwordError?.let {
                    binding.editTextPassword.error = getString(it)
                }
                registrationFormState.emailError?.let {
                    binding.textEmail.error = getString(it)
                }
                registrationFormState.telefonoError?.let {
                    binding.textTelefono.error = getString(it)
                }
                registrationFormState.genereError?.let {
                    binding.textGenere.error = getString(it)
                }
            })

        binding.buttonSalvaUtente.setOnClickListener {
            nomeUtente = binding.textNomeUtente.text.toString()
            password = binding.editTextPassword.text.toString()
            nome = binding.textNome.text.toString()
            cognome = binding.textCognome.text.toString()
            email = binding.textEmail.text.toString()
            telefono = binding.textTelefono.text.toString()
            genere = binding.textGenere.text.toString()

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
                password).addOnCompleteListener {
                if (it.isSuccessful) {
                    utenteId = it.result.user!!.uid
                    it.result.user?.let { it1 -> db.collection("Utente").document(it1.uid).set(
                        hashMapOf("displayName" to nomeUtente,
                            "nome" to nome,
                            "cognome" to cognome,
                            "email" to email,
                            "telefono" to telefono,
                            "genere" to genere)
                    ) }
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.registraUtente(
                            utenteId,
                            nomeUtente,
                            password,
                            nome,
                            cognome,
                            email,
                            telefono,
                            genere
                        )
                    }
                    val bundle = bundleOf("displayName" to nomeUtente, "nome" to nome, "cognome" to cognome, "email" to email, "telefono" to telefono, "genere" to genere )
                    findNavController().navigate(
                        R.id.action_registrationFragment_to_navigation_notifications,
                        bundle
                    )
                } else {
                    Toast.makeText(context, "Account non creato online", Toast.LENGTH_SHORT).show()
                }
            }
        }
        val afterTextChangedListener = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // ignore
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // ignore
            }

            override fun afterTextChanged(s: Editable) {
                viewModel.registrationDataChanged(
                    binding.textNomeUtente.text.toString(),
                    binding.editTextPassword.text.toString(),
                    binding.textTelefono.text.toString(),
                    binding.textGenere.text.toString(),
                    binding.textEmail.text.toString()
                )
            }
        }
        binding.textNomeUtente.addTextChangedListener(afterTextChangedListener)
        binding.editTextPassword.addTextChangedListener(afterTextChangedListener)
        binding.textTelefono.addTextChangedListener(afterTextChangedListener)
        binding.textGenere.addTextChangedListener(afterTextChangedListener)
        binding.textEmail.addTextChangedListener(afterTextChangedListener)
binding.loginLink.setOnClickListener{
    findNavController().navigate(R.id.action_registrationFragment_to_loginFragment)
}
        }
    }

    fun generateRandomString(length: Int): String {
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..length)
            .map { charset.random() }
            .joinToString("")
    }

