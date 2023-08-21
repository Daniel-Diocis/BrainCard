package com.example.braincard


import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.example.braincard.data.model.Card
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.databinding.FragmentFlashcardStudioBinding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class FlashcardStudio : Fragment() {

    companion object {
        fun newInstance() = FlashcardStudio()
    }

    private lateinit var viewModel: FlashcardStudioViewModel
    private lateinit var binding: FragmentFlashcardStudioBinding
    private var currentFlashcardPosition = 0
    private val flashcardDataList = mutableListOf<Card>()
    var dom = ""
    var risp = ""


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProvider(
            this,).get(FlashcardStudioViewModel::class.java)
        binding = FragmentFlashcardStudioBinding.inflate(inflater, container, false)
        binding.flashcard.setOnClickListener {
            toggleFlashcardVisibility()
        }
        binding.flashcardBack.setOnClickListener {
            toggleFlashcardVisibility()
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        val codiceDeck = "MIYqkjpt4WmSvxBBPpra"
        // TODO : Modo per passare codice deck
        viewLifecycleOwner.lifecycleScope.launch() {
            val flashcardsLiveData : MutableLiveData<MutableList<Card>>
           withContext(Dispatchers.IO) {flashcardsLiveData = viewModel.getFlashcardsByCodiceDeck(codiceDeck)}

            withContext(Dispatchers.Main) {
                flashcardsLiveData.observe(viewLifecycleOwner) { flashcards: List<Card> ->
                    // Pulisci la lista esistente
                    flashcardDataList.clear()

                    // Popola la lista con i dati delle flashcard ottenute dal database
                    flashcards.forEach { flashcard ->
                        flashcardDataList.add(
                            Card(
                                flashcard.id,
                                flashcard.domanda,
                                flashcard.risposta,
                                false,
                                flashcard.deckID
                            )
                        )
                    }

                    // Ora puoi aggiornare la UI con le flashcard caricate
                    updateFlashcardContent()
                }
            }


// TODO: Carica la carta con un codice univoco
            val cardCode = "DVX1UqFxKevoDiMN9Dp3" // Sostituisci con il tuo codice
// TODO : implementare funzione che definisce codice
            withContext(Dispatchers.IO){ viewModel.loadCardByCode(cardCode)}

// Osserva il LiveData per i dettagli della carta e aggiorna l'interfaccia utente
            viewModel.cardLiveData.observe(viewLifecycleOwner, Observer { card: Card ->
                // Aggiorna l'interfaccia utente con i dati della carta
                binding.textDomanda.setText(card.domanda)
                binding.textRisposta.setText(card.risposta)
                //binding.numbersTextView.setText(card.numero)
            })
        }
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val gestureDetector = GestureDetector(requireContext(), object : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(
                p0: MotionEvent,
                p1: MotionEvent,
                velocityX: Float,
                velocityY: Float
            ): Boolean {
                val dx = p1.x ?: (0F - p0?.x!!)
                if (dx > 0 && currentFlashcardPosition < flashcardDataList.size - 1) {
                    // Swipe verso sinistra
                    currentFlashcardPosition++
                    updateFlashcardContent()
                }
                return true
            }
        })

        view.setOnTouchListener { _, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }


    private fun updateFlashcardContent() {
        val flashcardData = flashcardDataList[currentFlashcardPosition]
        binding.textDomanda.text = flashcardData.domanda
        binding.textRisposta.text = flashcardData.risposta

    }

    private fun toggleFlashcardVisibility() {

        if (binding.flashcardBack.visibility == View.GONE) dom=binding.textDomanda.text.toString()
        else risp=binding.textRisposta.text.toString()
        val rotation = if (binding.flashcardBack.visibility == View.VISIBLE) {
            Log.e("Vis", "true")
            0f} else 180f
        val rotation2 = if (binding.flashcardBack.visibility == View.VISIBLE) {
            Log.e("Vis2", "false")
            180f} else 0f
        val anim = ObjectAnimator.ofFloat(binding.flashcard, "rotationY", rotation)
        val anim2 = ObjectAnimator.ofFloat(binding.flashcardBack, "rotationY", rotation2)
        anim.duration = 300
        anim2.duration = 300
        anim.start()
        anim2.start()

        val handler = Handler()
        handler.postDelayed({
            binding.flashcard.visibility = if (rotation == 0f) View.VISIBLE else View.GONE
            binding.flashcardBack.visibility = if (rotation == 180f) View.VISIBLE else View.GONE

            if (dom != "") binding.textDomanda.setText(dom)
            if (risp != "") binding.textRisposta.setText(risp)
        }, 320) // Ritardo di 300ms
        binding.textDomanda.visibility = if (rotation == 0f) View.VISIBLE else View.GONE
        binding.textRisposta.visibility = if (rotation == 180f) View.VISIBLE else View.GONE

    }



}