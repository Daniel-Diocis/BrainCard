package com.example.braincard


import android.animation.ObjectAnimator
import android.graphics.Color
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
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.braincard.data.model.Card
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.databinding.FragmentFlashcardStudioBinding
import com.example.braincard.factories.FlashcardStudioViewModelFactory
import com.github.jinatonic.confetti.CommonConfetti

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates


class FlashcardStudio : Fragment() {



    private lateinit var viewModel: FlashcardStudioViewModel
    private lateinit var binding: FragmentFlashcardStudioBinding
    private var initialX = 0F
    private var initialTranslationX = 0F
    private val flashcardDataList = mutableListOf<Card>()
    var dom = ""
    var risp = ""
    lateinit var deckId : String
    private var index : Int = 0
    private var MAX_INDEX : Int = 0
    private var perc : Int = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        deckId = arguments?.getString("deckId").toString()
        viewModel = ViewModelProvider(
            this, FlashcardStudioViewModelFactory(requireActivity().application, deckId.toString())).get(FlashcardStudioViewModel::class.java)

        binding = FragmentFlashcardStudioBinding.inflate(inflater, container, false)


        binding.flashcard.setOnClickListener {
            toggleFlashcardVisibility()
        }
        binding.flashcardBack.setOnClickListener {
            toggleFlashcardVisibility()
        }
        binding.CorrettoImageView.setOnClickListener {
            CorrectNextCard(deckId.toString())
        }
        binding.SbagliatoImageView.setOnClickListener {
            SbagliatoNextCard()
        }


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


    }


        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            viewModel.AllCard.observe(viewLifecycleOwner) { cards ->
                MAX_INDEX = cards.size
                Log.e(" CONTROLLO ALL CARD ", viewModel.AllCard.value.toString())
                if (!cards.isNullOrEmpty()) {
                    viewModel.loadCardByCode(viewModel.AllCard.value!![index].id)
                }
                else {
                    onEmptyAllCard()
                }
            }
            viewModel.percentualeDeck.observe(viewLifecycleOwner) { percentuale ->
                    perc = percentuale
            }
            viewModel.cardLiveData.observe(viewLifecycleOwner, { card ->
                binding.textDomanda.setText(card.domanda)
                binding.textRisposta.setText(card.risposta)
            })






            view.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        initialTranslationX = binding.flashcard.translationX
                        initialX = event.x
                    }

                    MotionEvent.ACTION_UP -> {
                        val finalX = event.x
                        val deltaX = finalX - initialX
                        if (deltaX < -SWIPE_THRESHOLD) {
                            // Swipe da destra a sinistra
                            val endTranslationX = initialTranslationX - binding.flashcard.width
                            binding.flashcard.animate()
                                .translationX(endTranslationX)
                                .setDuration(300) // Durata dell'animazione in millisecondi
                                .withEndAction {
                                    // Alla fine dell'animazione, chiamare la funzione SbagliatoNextCard
                                    SbagliatoNextCard()
                                    // Ripristinare la posizione della carta per la prossima animazione
                                    binding.flashcard.translationX = initialTranslationX
                                }
                                .start()
                        }
                    }
                }
                true
            }
        }
            companion object {
                private const val SWIPE_THRESHOLD = 50 // Imposta il valore appropriato per il tuo caso
            }

    fun onEmptyAllCard(){
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Nessuna Carta")
            setMessage("Il deck non contiene alcuna carta.")
            setPositiveButton("OK"){_, _ ->
                findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment)
            }
            setCancelable(false)
            create()
            show()
        }
    }
    private fun CorrectNextCard(deckId : String) {
        val size = viewModel.getSizeOfDeck()
        var bool = false
        if (!viewModel.cardLiveData.value!!.completata) {
            if (perc == 0) viewModel.updatePercentualeCompletamento(deckId, ((1.0 / size) * 100).toInt())
            else {
                var newPercentage = perc + ((1.0 / size) * 100).toInt()
                if (newPercentage == 99 || newPercentage>100) newPercentage=100
                viewModel.updatePercentualeCompletamento(deckId, newPercentage)
                if(newPercentage==100){
                    if(binding.CoriandolitextView.visibility == View.GONE) Log.e("VISIBILITA'","GONE")
                    else Log.e("VISIBILITA'","ORCO DIO")
                    binding.CoriandolitextView.visibility = View.VISIBLE;
                    if(binding.CoriandolitextView.visibility == View.GONE) Log.e("VISIBILITA'","GONE2")
                    else Log.e("VISIBILITA'","ORCO DIO2")

                    CommonConfetti.rainingConfetti(binding.ConstrLayout, intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED))
                        .infinite();
                    bool = true
                    Handler().postDelayed({
                        findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment)
                    }, 3000)
                }
            }
            viewModel.updateCompletataCard()
        }
        if(index+1<MAX_INDEX)
        {index = index+1
        viewModel.loadCardByCode(viewModel.AllCard.value!![index].id)}
        else {
            if(!bool) findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment)
        }
    }
    private fun SbagliatoNextCard(){
        if(index+1<MAX_INDEX)
        {index = index+1
            viewModel.loadCardByCode(viewModel.AllCard.value!![index].id)}
        else {
             findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment)
        }
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