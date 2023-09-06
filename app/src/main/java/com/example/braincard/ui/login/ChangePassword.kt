package com.example.braincard.ui.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.braincard.R
import com.example.braincard.databinding.FragmentChangepasswordBinding
import com.example.braincard.factories.ChangePasswordViewModelFactory
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class ChangePassword : Fragment() {
    lateinit var binding : FragmentChangepasswordBinding
    lateinit var viewModel : ChangePasswordViewModel
    val auth = FirebaseAuth.getInstance().currentUser
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentChangepasswordBinding.inflate(inflater,container,false)
        viewModel=ViewModelProvider(this,ChangePasswordViewModelFactory(requireActivity().application)).get(ChangePasswordViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.registrationFormState.observe(viewLifecycleOwner,
            Observer { registrationFormState ->
                if (registrationFormState == null) {
                    return@Observer
                }
                binding.cambio.isEnabled = registrationFormState.isDataValid
                registrationFormState.passwordError2?.let {
                    binding.Npassword.error = getString(it)
                }
                registrationFormState.passwordError?.let {
                    binding.Vpassword.error = getString(it)
                }
                })
        binding.cambio.setOnClickListener {
            val credential = EmailAuthProvider.getCredential(
                auth?.email.toString(),
                binding.Vpassword.text.toString()
            )
            auth?.reauthenticate(credential)
                ?.addOnCompleteListener { reauthTask ->
                    if (reauthTask.isSuccessful) {
                        auth.updatePassword(binding.Npassword.text.toString())
                            .addOnCompleteListener { updatePasswordTask ->
                                if (updatePasswordTask.isSuccessful) {
                                    Toast.makeText(
                                        requireActivity().application,
                                        "Password aggiornata",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        requireActivity().application,
                                        "Password non aggiornata",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                    } else {
                        Toast.makeText(
                            requireActivity().application,
                            "Errore di autenticazione",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    findNavController().navigate(R.id.navigation_notifications)
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
                viewModel.registrationDataChanged3(
                    binding.Vpassword.text.toString(),
                    binding.Npassword.text.toString()
                )
            }
        }
        binding.Vpassword.addTextChangedListener(afterTextChangedListener)
        binding.Npassword.addTextChangedListener(afterTextChangedListener)



    }
}