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
import androidx.appcompat.app.AlertDialog
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
        )
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
                deckButton.maxWidth=80
                deckButton.minimumWidth=80
                deckButton.setOnClickListener {
                    val bundle = bundleOf("deckId" to deck.id)
                    findNavController().navigate(
                        R.id.action_HomeFragment_to_flashcardStudio,
                        bundle
                    )
                }


                deckButton.setImageDrawable(DeckIcon)


                val nomeTextView = TextView(requireContext())
                nomeTextView.gravity = Gravity.CENTER
                nomeTextView.width=80


                nomeTextView.text = deck.nome

                val deckLayout = LinearLayout(requireContext())
                deckLayout.orientation = LinearLayout.VERTICAL
                val paddingInDp = 20
                val density = resources.displayMetrics.density
                val paddingInPx = (paddingInDp * density).toInt() // Converte dp in px

                deckLayout.setPadding(paddingInPx, 0, paddingInPx, 0)
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
                deckButton.height=154


                val deckElimina=ImageButton (requireContext())
                deckElimina.setImageDrawable(deleteIcon)


                deckButton.text = gruppo.nome
                deckButton.tag = gruppo.id
                deckElimina.tag=gruppo

                //eventi
                deckButton.setOnClickListener {
                    val gruppoId= it.tag as String
                    Log.e("gruppo id",gruppoId)
                    val bundle = bundleOf("gruppoid" to gruppoId)

                    val action = R.id.action_navigation_home_to_gruppoFragment

                    findNavController().navigate(action,bundle)

                }

                deckElimina.setOnClickListener{
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Conferma Eliminazione")
                    builder.setMessage("Sei sicuro di voler eliminare questo gruppo?")

                    builder.setPositiveButton("Sì") { dialog, which ->
                        homeViewModel.eliminaGruppo(gruppo)

                        dialog.dismiss()
                    }

                    builder.setNegativeButton("No") { dialog, which ->
                        dialog.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()

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


        popUpMessage.messageLiveData.observe(viewLifecycleOwner, Observer { newMessage ->

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