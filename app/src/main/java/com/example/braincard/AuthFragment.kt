package com.example.braincard

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import androidx.fragment.app.FragmentManager
import java.security.Provider

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AuthFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AuthFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // Dichiarazione della variabile registratiButton come variabile di istanza
    private lateinit var registratiButton: Button
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_auth, container, false)

        registratiButton = rootView.findViewById(R.id.registratiButton)
        emailEditText = rootView.findViewById(R.id.emailEditText)
        passwordEditText = rootView.findViewById(R.id.passwordEditText)

        //Setup
        setup()

        return rootView


    }

    private fun setup() {
        registratiButton.setOnClickListener() {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty() ) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(emailEditText.text.toString(),
                    passwordEditText.toString()).addOnCompleteListener {

                        if (it.isSuccessful) {
                            showLog(it.result?.user?.email ?:"", ProviderType.BASIC)
                        } else {
                            showAlert()
                        }
                }
            }

            // Esegui la transazione di fragment qui
            val registrationFragment = RegistrationFragment.newInstance()
            val transaction = requireFragmentManager().beginTransaction()
            transaction.replace(R.id.fragment_container, registrationFragment)
            transaction.addToBackStack(null) // Opzionale: aggiungi la transazione allo stack indietro
            transaction.commit()
        }

}

    private fun showAlert() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Errore")
        builder.setMessage("Si è verificato un errore all'autenticazione")
        builder.setPositiveButton("Accetta", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showLog(email: String, provider: ProviderType) {
        val logIntent = Intent(requireContext(), LogFragment::class.java).apply {
            // Aggiungi eventuali dati extra all'intent, se necessario
            putExtra("email", email)
            putExtra("provider", provider.toString())
        }
        startActivity(logIntent)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AuthFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AuthFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}