package com.example.braincard

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.statement.UiThreadStatement.runOnUiThread
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.GruppoRepository
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class UpdatePercentualeInstrumentedTest {

    lateinit var gruppoRepository: GruppoRepository
    lateinit var cardRepository: CardRepository
    lateinit var deckRepository: DeckRepository
    lateinit var viewModel: FlashcardStudioViewModel
    lateinit var db: BrainCardDatabase

    @Before
    fun setUp() = runTest {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BrainCardDatabase::class.java
        ).build()

        val cardDaoMock = db.cardDao()
        val deckDaoMock = db.deckDao()
        val gruppoDaoMock = db.gruppoDao()

        cardRepository = CardRepository(cardDaoMock)
        deckRepository = DeckRepository(deckDaoMock)
        gruppoRepository = GruppoRepository(gruppoDaoMock)
        val gruppo = Gruppo(id = "AAAAAAAAAAAAAAAAAAAA", nome = "Nome Gruppo")
        gruppoRepository.insertGruppo(gruppo)


        val deck = Deck(
            id = "AAAAAAAAAAAAAAAAAAAB",
            nome = "Nome Deck",
            idGruppo = "AAAAAAAAAAAAAAAAAAAA",
            percentualeCompletamento = 0
        )
        deckRepository.insertDeck(deck)
        viewModel =
            FlashcardStudioViewModel(ApplicationProvider.getApplicationContext(), "AAAAAAAAAAAAAAAAAAAB")


        val card = Card(
            id = "AAAAAAAAAAAAAAAAAAAC",
            domanda = "",
            risposta = "",
            completata = false,
            deckID = "AAAAAAAAAAAAAAAAAAAB"
        )
        cardRepository.insertCard(card)

    }

    @Test
    fun testCreateGroupWithDeckAndCard() {
        runBlocking {

        val observedGruppo = gruppoRepository.getGruppoById("AAAAAAAAAAAAAAAAAAAA")
        assertNotNull(observedGruppo)

        val observedDeck1 = deckRepository.getDeckById("AAAAAAAAAAAAAAAAAAAB")
        assertNotNull(observedDeck1)

        val observedCard = cardRepository.getCardById("AAAAAAAAAAAAAAAAAAAC")
        assertNotNull(observedCard)}


        viewModel.updateCompletataCard()
        viewModel.updatePercentualeCompletamento("AAAAAAAAAAAAAAAAAAAB",75)
        viewModel.loadPercentualeDeck("AAAAAAAAAAAAAAAAAAAB")
        runOnUiThread{
        viewModel.cardLiveData.observeForever{card-> assertEquals(true, card.completata) }
        viewModel.percentualeDeck.observeForever { percentuale-> assertEquals(75, percentuale) }}

    }

    @After
    fun tearDown(){
        db.close()
    }

}

