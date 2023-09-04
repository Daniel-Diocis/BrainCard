package com.example.braincard.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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
        val root: View = binding.root
        if (auth.currentUser != null){
            val user = db.collection("Utente").document(auth.currentUser!!.uid).get().addOnCompleteListener{task->
                if (task.isSuccessful ) {
                    Log.e("QUIAOAO","2")
                    notificationsViewModel = ViewModelProvider(requireActivity(), NotificationsViewModelFactory(
                        task.result.getString("displayName").toString(),
                        task.result.getString("nome").toString(),
                        task.result.getString("cognome").toString(),
                        task.result.getString("email").toString(),
                        task.result.getString("telefono").toString(),
                        task.result.getString("genere").toString()
                    )).get(NotificationsViewModel::class.java)
                    setup()
                }
            }
            binding.buttonLogout.setOnClickListener{
                auth.signOut()
                findNavController().navigate(R.id.navigation_home)
            }
        }
        return root
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
}