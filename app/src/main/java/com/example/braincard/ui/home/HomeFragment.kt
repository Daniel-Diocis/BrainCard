package com.example.braincard.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.braincard.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        val deckContainer:LinearLayout =binding.deckContainer
        homeViewModel.AllDeck.observe(viewLifecycleOwner, Observer { decks ->
            deckContainer.removeAllViews()
            for (deck in decks) {
                Log.e("controllo","ok")

                val deckButton = Button(requireContext())
                deckButton.text = "gino" // O qualsiasi altra proprietà del mazzo che vuoi visualizzare
                deckButton.setOnClickListener {
                    // Qui gestisci il click sul bottone del mazzo
                }
                deckContainer.addView(deckButton)
            }
        })





        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}