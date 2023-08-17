package com.example.braincard

import AppDatabase
import android.animation.ObjectAnimator
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.GestureDetector
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.braincard.data.model.Card
import com.example.braincard.databinding.FragmentFlashcardStudioBinding


class FlashcardStudio : Fragment() {

    companion object {
        fun newInstance() = FlashcardStudio()
    }

    private lateinit var viewModel: FlashcardStudioViewModel
    private lateinit var binding: FragmentFlashcardStudioBinding
    private var currentFlashcardPosition = 0
    private val flashcardDataList = mutableListOf<Card>()
    var dom=""
    var risp=""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val cardDao=AppDatabase.cardDao()
        binding = FragmentFlashcardStudioBinding.inflate(inflater, container, false)
        binding.flashcard.setOnClickListener {
            toggleFlashcardVisibility()
        }
        binding.flashcardBack.setOnClickListener {
            toggleFlashcardVisibility()
        }
        val codiceDeck = "il_tuo_codice_deck"
        // TODO : Modo per passare codice deck
        viewModel.getFlashcardsByCodiceDeck(codiceDeck).observe(viewLifecycleOwner) { flashcards : List<Card> ->

            // Pulisci la lista esistente
            flashcardDataList.clear()

            // Popola la lista con i dati delle flashcard ottenute dal database
            for (flashcard in flashcards) {
                flashcardDataList.add(Card(flashcard.domanda, flashcard.risposta))
            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FlashcardStudioViewModel::class.java)
        // TODO: Use the ViewModel
        // Carica la carta con un codice univoco
        val cardCode = "CODICE_UNIVOCO" // Sostituisci con il tuo codice
        // TODO : implementare funzione che definisce codice
        viewModel.loadCardByCode(cardCode)
        // Osserva il LiveData per i dettagli della carta e aggiorna l'interfaccia utente
        viewModel.cardLiveData.observe(viewLifecycleOwner, Observer { card : Card ->
            // Aggiorna l'interfaccia utente con i dati della carta
            binding.textDomanda.setText(card.domanda)
            binding.textRisposta.setText(card.risposta)
            binding.numbersTextView.setText(card.numero)
        })
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

        val rotation = if (binding.flashcardBack.visibility == View.VISIBLE) 0f else 180f
        val anim = ObjectAnimator.ofFloat(binding.flashcard, "rotationY", rotation)
        anim.duration = 300
        anim.start()

        binding.flashcard.visibility = if (rotation == 0f) View.VISIBLE else View.GONE
        binding.flashcardBack.visibility = if (rotation == 180f) View.VISIBLE else View.GONE


    }



}