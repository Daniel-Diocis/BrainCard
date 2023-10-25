package com.example.braincard.ui.notifications

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.navigation.fragment.findNavController
import com.example.braincard.R
import com.example.braincard.databinding.FragmentNotificationsBinding
import com.example.braincard.factories.NotificationsViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var notificationsViewModel : NotificationsViewModel
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.e("DENTRO A", "CREAte")
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        notificationsViewModel= ViewModelProvider(requireActivity(), NotificationsViewModelFactory("","",
            "","","","")).get(NotificationsViewModel::class.java)
        val root: View = binding.root
        if (auth.currentUser != null) {
            db.collection("Utente").document(auth.currentUser!!.uid).get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.e("Succesful","")
                        val lista = listOf<String>(
                                task.result.getString("displayName").toString(),
                                task.result.getString("nome").toString(),
                                task.result.getString("cognome").toString(),
                                task.result.getString("email").toString(),
                                task.result.getString("telefono").toString(),
                                task.result.getString("genere").toString()
                        )
                        notificationsViewModel.updateData(lista)

                    }
                }
        }
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.e("TEST",(binding.textGenere.text=="").toString()+" "+binding.textGenere.text)
            if (binding.textGenere.text=="") showLoadingScreen()
            binding.buttonLogout.setOnClickListener{
                auth.signOut()
                findNavController().navigate(R.id.navigation_home)
            }
            binding.buttonModificaProfilo.setOnClickListener{
                val bundle = bundleOf("nomeutente" to binding.textNomeUtente.text,
                    "nome" to binding.textNome.text,
                    "cognome" to binding.textCognome.text,
                    "email" to binding.textEmail.text,
                    "telefono" to binding.textTelefono.text,
                    "genere" to binding.textGenere.text)
                findNavController().navigate(R.id.action_navigation_notifications_to_modProfilo, bundle)
            }
        notificationsViewModel.genere.observe(viewLifecycleOwner, Observer {
            setup()
            if (binding.textGenere.text!="") hideLoadingScreen()

        })
        }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun setup(){
        binding.textNomeUtente.text = notificationsViewModel.displayName.value.toString()
        binding.textNome.text = notificationsViewModel.nome.value.toString()
        binding.textCognome.text = notificationsViewModel.cognome.value.toString()
        binding.textEmail.text = notificationsViewModel.email.value.toString()
        binding.textTelefono.text = notificationsViewModel.telefono.value.toString()
        binding.textGenere.text = notificationsViewModel.genere.value.toString()

    }
    fun showLoadingScreen() {
        Log.e("Progress","")
        binding.progressBar.visibility=View.VISIBLE
        binding.buttonLogout.visibility=View.INVISIBLE
        binding.buttonModificaProfilo.visibility=View.INVISIBLE
    }

    // Funzione per nascondere la schermata di caricamento
    fun hideLoadingScreen() {
        Log.e("Progresso","")
        binding.progressBar.visibility=View.GONE
        binding.buttonLogout.visibility=View.VISIBLE
        binding.buttonModificaProfilo.visibility=View.VISIBLE

    }
}