package com.example.braincard

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.braincard.factories.GruppoViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.eazegraph.lib.charts.PieChart
import org.eazegraph.lib.models.PieModel


class GruppoFragment : Fragment() {

    var count : Int = 0



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?

    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_gruppo, container, false)
        var gruppoId = arguments?.getString("gruppoid")
        gruppoId=gruppoId.toString()
        //creo viewmodel
        val factory = GruppoViewModelFactory(requireActivity().application, gruppoId)
        val gruppoViewModel = ViewModelProvider(this, factory).get(GruppoViewModel::class.java)




        val pieChart = rootView.findViewById<PieChart>(R.id.piechart)

        val progressBar = rootView.findViewById<TextView>(R.id.progress)
        val percentComplete ="50%" // Cambia questo valore in base alla percentuale di completamento desiderata
        progressBar.text = percentComplete

        val deckContainer=rootView.findViewById<LinearLayout>(R.id.gruppiContainer)
        val btn_gen =rootView.findViewById<Button>(R.id.btn)
        btn_gen.setOnClickListener{
            gruppoViewModel.creaDeck()
        }

        gruppoViewModel.AllDeck.observe(viewLifecycleOwner, Observer { decks ->
            deckContainer.removeAllViews()
            count = 0
            var perc=0
            for (deck in decks) {
                Log.e(deck.nome,deck.idGruppo)
                val deckButton = Button(requireContext())
                deckButton.text = deck.nome
                deckButton.id = count
                deckButton.setOnClickListener {
                            val bundle = bundleOf("deckId" to deck.id)
                            findNavController().navigate(
                                R.id.action_gruppoFragment_to_flashcardStudio,
                                bundle
                            )
                        }
                count++
                perc+=deck.percentualeCompletamento
                deckContainer.addView(deckButton)
                }
            if(decks.size > 0){
                perc=perc/decks.size

            }
            else perc=0

            var percString = perc.toString() + "%"
            progressBar.text =percString
            var pieModel : PieModel = PieModel("Progresso", perc.toFloat(), Color.BLUE)
            pieChart.addPieSlice(pieModel)
            var complementaryPieModel = PieModel("Mancante", (100-perc).toFloat(), Color.WHITE)
            pieChart.addPieSlice(complementaryPieModel)
            pieChart.startAnimation()

        })
        // Inflate the layout for this fragment
        return rootView
    }

}