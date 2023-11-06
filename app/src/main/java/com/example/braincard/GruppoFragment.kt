package com.example.braincard

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.braincard.factories.GruppoViewModelFactory
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel
import androidx.core.content.ContextCompat
import com.example.braincard.data.model.Deck
import com.google.android.material.floatingactionbutton.FloatingActionButton


class GruppoFragment : Fragment() {

    var count : Int = 0
    lateinit var popUpMessage: PopUpMessage
    lateinit var gruppoViewModel : GruppoViewModel
    val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    var desiredWidth=0




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {
        val deleteIcon = ContextCompat.getDrawable(requireContext(),
            android.R.drawable.ic_delete
        )
        val modIcon = ContextCompat.getDrawable(requireContext(),
            android.R.drawable.ic_menu_edit
        )
        popUpMessage = PopUpMessage.getInstance()
        val rootView = inflater.inflate(R.layout.fragment_gruppo, container, false)
        var gruppoId = arguments?.getString("gruppoid")
        gruppoId=gruppoId.toString()
        //creo viewmodel
        val factory = GruppoViewModelFactory(requireActivity().application, gruppoId)
        gruppoViewModel = ViewModelProvider(this, factory).get(GruppoViewModel::class.java)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner){
            findNavController().navigate(R.id.action_gruppoFragment_to_navigation_home)
        }

        val pieChart = rootView.findViewById<PieChart>(R.id.piechart)

        val progressBar = rootView.findViewById<TextView>(R.id.progress)
        val percentComplete ="50%" // Cambia questo valore in base alla percentuale di completamento desiderata
        progressBar.text = percentComplete

        val deckContainer=rootView.findViewById<LinearLayout>(R.id.gruppiContainer)
        val btn_gen =rootView.findViewById<FloatingActionButton>(R.id.floatingActionButtonDeck)
        btn_gen.setOnClickListener{

        }

        gruppoViewModel.AllDeck.observe(viewLifecycleOwner, Observer { decks ->
            deckContainer.removeAllViews()
            count = 0
            var perc=0
            for (deck in decks) {

                val layout=LinearLayout(requireContext())
                layout.orientation = LinearLayout.HORIZONTAL
                desiredWidth = (screenWidth * 3) / 4
                val layoutParams = LinearLayout.LayoutParams(
                    desiredWidth,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                val deckButton = Button(requireContext())
                deckButton.layoutParams = layoutParams // Imposta i parametri del layout

                val deckElimina=ImageButton (requireContext())
                deckElimina.setImageDrawable(deleteIcon)
                val deckMod=ImageButton (requireContext())
                deckMod.setImageDrawable(modIcon)

                deckButton.text = deck.nome
                deckButton.id = count
                deckButton.setOnClickListener {
                            val bundle = bundleOf("deckId" to deck.id)
                            findNavController().navigate(
                                R.id.action_gruppoFragment_to_flashcardStudio,
                                bundle
                            )
                        }
                deckElimina.setOnClickListener{
                    dialogEliminaDeck(deck)
                }
                deckMod.setOnClickListener{
                    val bundle = bundleOf("deckId" to deck.id)
                    findNavController().navigate(
                        R.id.action_gruppoFragment_to_modCreaCard,
                        bundle
                    )

                }
                count++
                perc+=deck.percentualeCompletamento
                layout.addView(deckButton)
                layout.addView(deckMod)
                layout.addView(deckElimina)
                deckContainer.addView(layout)
                }
            //SETUP DEL DIAGRAMMA A TORTA
            if(decks.size > 0){
                perc=perc/decks.size

            }
            else perc=0

            val percString = perc.toString() + "%"
            progressBar.text =percString
            val pieModel : PieModel = PieModel("Progresso", perc.toFloat(), Color.BLUE)
            pieChart.addPieSlice(pieModel)
            val complementaryPieModel = PieModel("Mancante", (100-perc).toFloat(), Color.WHITE)
            pieChart.addPieSlice(complementaryPieModel)
            pieChart.startAnimation()

        })
        // Inflate the layout for this fragment
        btn_gen.setOnClickListener{
            val pop = PopUp()
            pop.vista="gruppo"
            pop.show((activity as AppCompatActivity).supportFragmentManager, "showPopUp")
        }

        // Osserva l'attributo message quando si crea un gruppo
        popUpMessage.messageDeckLiveData.observe(viewLifecycleOwner, Observer { newMessage ->
            // Esegui azioni in risposta ai cambiamenti dell'attributo message
            if (popUpMessage.invia){
                gruppoViewModel.creaDeck(newMessage)
                popUpMessage.invia=false
            }


        })
        return rootView
    }
fun dialogEliminaDeck(deck : Deck){
    val builder = AlertDialog.Builder(requireContext())
    builder.setTitle("Conferma Eliminazione")
    builder.setMessage("Sei sicuro di voler eliminare questo deck?")

    builder.setPositiveButton("Sì") { dialog, which ->
        // Operazione di eliminazione qui
        gruppoViewModel.deleteDeck(deck)
        dialog.dismiss()
    }

    builder.setNegativeButton("No") { dialog, which ->
        dialog.dismiss()
    }

    val dialog = builder.create()
    dialog.show()
}

}