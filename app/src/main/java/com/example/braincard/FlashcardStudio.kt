package com.example.braincard


import android.animation.ObjectAnimator
import android.graphics.Color
import android.opengl.Visibility
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.graphics.component1
import androidx.core.os.bundleOf
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.navigation.fragment.findNavController
import com.example.braincard.data.model.Card
import com.example.braincard.databinding.FragmentFlashcardStudioBinding
import com.example.braincard.factories.FlashcardStudioViewModelFactory
import com.github.jinatonic.confetti.CommonConfetti
import com.github.jinatonic.confetti.ConfettiManager


class FlashcardStudio : Fragment() {



    private lateinit var viewModel: FlashcardStudioViewModel
    private lateinit var binding: FragmentFlashcardStudioBinding
    private var initialX = 0F
    private var initialTranslationX = 0F
    lateinit var deckId : String
    lateinit var shuffledCards : MutableList<Card>
    lateinit var gruppoId : String
    private var index : Int = 0
    private var MAX_INDEX : Int = 0
    private var perc : Int = 0
    private var bool = true
    private var hasBeenCompleted = true
    private var clickable = true




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View {
        deckId = arguments?.getString("deckId").toString()
        viewModel = ViewModelProvider(this, FlashcardStudioViewModelFactory(requireActivity().application, deckId.toString())).get(FlashcardStudioViewModel::class.java)

        binding = FragmentFlashcardStudioBinding.inflate(inflater, container, false)


        binding.flashcard.setOnClickListener {
                if (clickable) toggleFlashcardVisibility()
            }
            binding.flashcardBack.setOnClickListener {
                if (clickable) toggleFlashcardVisibility()
            }
            binding.CorrettoImageView.setOnClickListener {
                if (clickable) CorrectNextCard(deckId.toString())
            }
            binding.SbagliatoImageView.setOnClickListener {
                if (clickable) SbagliatoNextCard()
            }



        return binding.root
    }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)


            viewModel.AllCard.observe(viewLifecycleOwner) { cards ->
                MAX_INDEX = cards.size
                if(bool) {
                    Log.e("SHUFFLING","")
                    shuffledCards=shuffleCards(cards)
                bool=false}
                if (!cards.isNullOrEmpty()) {
                    viewModel.loadCardByCode(shuffledCards[index].id)
                }
                else {
                    onEmptyAllCard()
                }
            }
            viewModel.percentualeDeck.observe(viewLifecycleOwner) { percentuale ->
                    perc = percentuale
            }
            viewModel.cardLiveData.observe(viewLifecycleOwner) { card ->
                binding.textDomanda.setText(card.domanda)
                binding.textRisposta.setText(card.risposta)
                if(binding.flashcardBack.visibility==View.VISIBLE) toggleFlashcardVisibility()


            }
            viewModel.gruppoId.observe(viewLifecycleOwner){
                id-> gruppoId = id

            }






            view.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        if(binding.flashcard.visibility==View.VISIBLE) initialTranslationX = binding.flashcard.translationX
                        else initialTranslationX = binding.flashcardBack.translationX
                        initialX = event.x
                    }

                    MotionEvent.ACTION_UP -> {
                        val finalX = event.x
                        val deltaX = finalX - initialX
                        if (deltaX < -SWIPE_THRESHOLD) {
                            // Swipe da destra a sinistra
                            val endTranslationX = initialTranslationX - binding.flashcard.width
                            if(binding.flashcard.visibility== View.VISIBLE){binding.flashcard.animate()
                                .translationX(endTranslationX)
                                .setDuration(300) // Durata dell'animazione in millisecondi
                                .withEndAction {
                                    // Alla fine dell'animazione, chiama la funzione SbagliatoNextCard
                                    SbagliatoNextCard()
                                    // Ripristinare la posizione della carta per la prossima animazione
                                    binding.flashcard.translationX = initialTranslationX
                                }
                                .start()}
                            else {binding.flashcardBack.animate()
                                .translationX(endTranslationX)
                                .setDuration(300) // Durata dell'animazione in millisecondi
                                .withEndAction {
                                    // Alla fine dell'animazione, chiamare la funzione SbagliatoNextCard
                                    SbagliatoNextCard()
                                    // Ripristinare la posizione della carta per la prossima animazione
                                    binding.flashcardBack.translationX = initialTranslationX


                                }
                                .start()}
                        }
                    }
                }
                true
            }
        }
            companion object {
                private const val SWIPE_THRESHOLD = 50
            }

    fun shuffleCards(card : MutableList<Card>): MutableList<Card> {
        val cards = card
        val falseCards = cards.filter { !it.completata }.shuffled()
        val trueCards = cards.filter { it.completata }.shuffled()
        val shuffledSortedCards = falseCards + trueCards
        return shuffledSortedCards.toMutableList()
    }

    fun onEmptyAllCard(){
        val bundle = bundleOf("gruppoid" to gruppoId)
        val alertDialogBuilder = AlertDialog.Builder(requireContext())
        alertDialogBuilder.apply {
            setTitle("Nessuna Carta")
            setMessage("Il deck non contiene alcuna carta.")
            setPositiveButton("OK"){_, _ ->
                findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment,bundle)
            }
            setCancelable(false)
            create()
            show()
        }
    }


    private fun CorrectNextCard(deckId : String) {
        val size = viewModel.getSizeOfDeck()
        var bool = false
        val bundle = bundleOf("gruppoid" to gruppoId)
        if(perc==100 && index+1==MAX_INDEX && !hasBeenCompleted){
            Log.e("1","")
            binding.CoriandolitextView.visibility = View.VISIBLE;
            CommonConfetti.rainingConfetti(binding.ConstrLayout, intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED))
                .infinite();

            clickable=false // dice che non si può cliccare
            bool = true
            val navController = findNavController()
            val action = R.id.action_flashcardStudio_to_gruppoFragment
            Handler().postDelayed({
                if(navController.currentDestination?.id != R.id.flashcardStudio)
                { }
                else navController.navigate(action, bundle)
            }, 3000)
        }
        if (!viewModel.cardLiveData.value!!.completata) {
            Log.e("2","")
            var newPercentage = 0
            if(hasBeenCompleted) hasBeenCompleted=false
            newPercentage = perc + ((1.0 / size) * 100).toInt()
            if (newPercentage == 99 || newPercentage>100) newPercentage=100
            viewModel.updatePercentualeCompletamento(deckId, newPercentage)
            if(newPercentage==100 && index+1==MAX_INDEX){
                    binding.CoriandolitextView.visibility = View.VISIBLE;
                    CommonConfetti.rainingConfetti(binding.ConstrLayout, intArrayOf(Color.BLUE, Color.CYAN, Color.GREEN, Color.RED))
                        .infinite()
                    
                    clickable=false
                    bool = true
                    val navController = findNavController()
                    val action = R.id.action_flashcardStudio_to_gruppoFragment
                    Handler().postDelayed({
                        if(navController.currentDestination?.id != R.id.flashcardStudio)
                        { }
                        else navController.navigate(action,bundle)
                    }, 3000)
                }

            viewModel.updateCompletataCard()
        }
        if(index+1<MAX_INDEX)
        {index = index+1
        viewModel.loadCardByCode(shuffledCards[index].id)}
        else {
            if(!bool) {
                Log.e("PD", "")
                findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment, bundle)}
        }
    }
    private fun SbagliatoNextCard(){
        val bundle = bundleOf("gruppoid" to gruppoId)
        if (viewModel.cardLiveData.value!!.completata)
        {
        val size = viewModel.getSizeOfDeck()
        var newPercentage = perc - ((1.0 / size) * 100).toInt()
        if(newPercentage == 1 || newPercentage == -1) newPercentage=0
        viewModel.updatePercentualeCompletamento(deckId, newPercentage)
        viewModel.updateSbagliataCard()}
        if(index+1<MAX_INDEX)
        {index = index+1
            viewModel.loadCardByCode(shuffledCards[index].id)}
        else {
             findNavController().navigate(R.id.action_flashcardStudio_to_gruppoFragment, bundle)
        }
    }

    private fun toggleFlashcardVisibility() {
        val rotation = if (binding.flashcardBack.visibility == View.VISIBLE) {

            0f} else 180f
        val rotation2 = if (binding.flashcardBack.visibility == View.VISIBLE) {

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


        }, 320) // Ritardo di 300ms
        binding.textDomanda.visibility = if (rotation == 0f) View.VISIBLE else View.GONE
        binding.textRisposta.visibility = if (rotation == 180f) View.VISIBLE else View.GONE

    }


}