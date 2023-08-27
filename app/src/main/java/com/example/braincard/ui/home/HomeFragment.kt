package com.example.braincard.ui.home

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.braincard.PopUp
import com.example.braincard.PopUpMessage
import com.example.braincard.R
import com.example.braincard.data.model.Gruppo
import com.example.braincard.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var nomeNuovo="a"
    private lateinit var homeViewModel: HomeViewModel
    lateinit var popUpMessage: PopUpMessage
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    var desiredWidth=0


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val deleteIcon = ContextCompat.getDrawable(requireContext(),
            android.R.drawable.ic_delete
        ) // Sostituisci con l'ID dell'icona
        val DeckIcon = ContextCompat.getDrawable(requireContext(),
            android.R.drawable.ic_input_get
        )
        popUpMessage = PopUpMessage.getInstance()

        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val deckContainer=binding.layoutDecks

        homeViewModel.HomeDeck.observe(viewLifecycleOwner,Observer{decks->
            deckContainer.removeAllViews()
            for (deck in decks){
                val deckButton = ImageButton(requireContext())
                deckButton.tag =deck.id
                deckButton.setOnClickListener {
                    val bundle = bundleOf("deckId" to deck.id)
                    findNavController().navigate(
                        R.id.action_HomeFragment_to_flashcardStudio,
                        bundle
                    )
                }


                deckButton.setImageDrawable(DeckIcon) // Imposta lo sfondo del pulsante come quadrato


                val nomeTextView = TextView(requireContext())
                nomeTextView.gravity = Gravity.CENTER


                nomeTextView.text = deck.nome

                val deckLayout = LinearLayout(requireContext())
                deckLayout.orientation = LinearLayout.VERTICAL
                deckLayout.setPadding(60)
                deckLayout.addView(deckButton)
                deckLayout.addView(nomeTextView)

                deckContainer.addView(deckLayout)
            }

        })



        val gruppiContainer=binding.gruppiContainer
        val btn_genera=binding.floatingActionButton

        homeViewModel.AllGruppo.observe(viewLifecycleOwner, Observer { gruppi ->
            gruppiContainer.removeAllViews()
            for (gruppo in gruppi) {
                Log.e("controllo","Gruppo trovato")
                val layout=LinearLayout(requireContext())
                layout.orientation = LinearLayout.HORIZONTAL


// Calcola la larghezza desiderata per il deckButton (3/5 dello schermo)
                 desiredWidth = (screenWidth * 6) / 7
                val layoutParams = LinearLayout.LayoutParams(
                    desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )

                val deckButton = Button(requireContext())
                deckButton.layoutParams = layoutParams // Imposta i parametri del layout


                val deckElimina=ImageButton (requireContext())
                deckElimina.setImageDrawable(deleteIcon)


                deckButton.text = gruppo.nome // O qualsiasi altra proprietà del mazzo che vuoi visualizzare
                deckButton.tag = gruppo.id
                deckElimina.tag=gruppo

                //eventi
                deckButton.setOnClickListener {
                    val gruppoId= it.tag as String
                    Log.e("gruppo id",gruppoId)
                    val bundle = bundleOf("gruppoid" to gruppoId)

                    val action = R.id.action_navigation_home_to_gruppoFragment

                    findNavController().navigate(action,bundle)
                    // Qui gestisci il click sul bottone del mazzo
                }

                deckElimina.setOnClickListener{
                    homeViewModel.eliminaGruppo(deckElimina.tag as Gruppo)
                }
                layout.addView(deckButton)
                layout.addView(deckElimina)

                gruppiContainer.addView(layout)
            }
        })
        btn_genera.setOnClickListener{
            val pop = PopUp()

            pop.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")

        }
        // Osserva l'attributo message quando si crea un gruppo

        popUpMessage.messageLiveData.observe(viewLifecycleOwner, Observer { newMessage ->
            // Esegui azioni in risposta ai cambiamenti dell'attributo message
            if (popUpMessage.invia){
                homeViewModel.creaGruppo(newMessage)
                popUpMessage.invia=false
            }


        })

        return binding.root
    }


    override fun onDestroyView() {
        super.onDestroyView()

        _binding = null
    }
}