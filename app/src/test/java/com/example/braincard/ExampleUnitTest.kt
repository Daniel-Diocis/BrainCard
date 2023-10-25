package com.example.braincard

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.braincard.data.model.Card
import com.example.braincard.data.model.Deck
import com.example.braincard.data.model.Gruppo
import com.example.braincard.database.BrainCardDatabase
import com.example.braincard.database.CardDAO
import com.example.braincard.database.CardRepository
import com.example.braincard.database.DeckDAO
import com.example.braincard.database.DeckRepository
import com.example.braincard.database.GruppoDAO
import com.example.braincard.database.GruppoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.junit.runner.manipulation.Ordering.Context
import org.mockito.Mockito.mock

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LocalUnitTest {

    lateinit var gruppoRepository: GruppoRepository
    lateinit var cardRepository: CardRepository
    lateinit var deckRepository: DeckRepository
    lateinit var viewModel: FlashcardStudioViewModel
    lateinit var db: BrainCardDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        db = Room.inMemoryDatabaseBuilder(
                context, BrainCardDatabase::class.java
            ).build()

            //val applicationMock = mock(Application::class.java)
            val cardDaoMock = db.cardDao()
            val deckDaoMock = db.deckDao()
            val gruppoDaoMock = db.gruppoDao()
            //val context = mock(Context::class.java)

            cardRepository = CardRepository(cardDaoMock)
            deckRepository = DeckRepository(deckDaoMock)
            gruppoRepository = GruppoRepository(gruppoDaoMock)
            viewModel =
                FlashcardStudioViewModel(ApplicationProvider.getApplicationContext(), "deckId")
        }

    @Test
    fun testCreateGroupWithDeckAndCard() = runBlockingTest {
            // Crea un Gruppo
            val gruppo = Gruppo(id = "gruppoId", nome = "Nome Gruppo")
            gruppoRepository.insertGruppo(gruppo)

            // Crea un Deck associato a quel Gruppo
            val deck = Deck(
                id = "deckId",
                nome = "Nome Deck",
                idGruppo = "gruppoId",
                percentualeCompletamento = 0
            )
            deckRepository.insertDeck(deck)

            // Crea una Card associata a quel Deck
            val card = Card(
                id = "cardId",
                domanda = "",
                risposta = "",
                completata = false,
                deckID = "deckId"
            )
            cardRepository.insertCard(card)

            // Verifica se il Gruppo, Deck e Card sono stati creati correttamente
            val observedGruppo = gruppoRepository.getGruppoById("gruppoId")
            assertNotNull(observedGruppo)

            val observedDeck1 = deckRepository.getDeckById("deckId")
            assertNotNull(observedDeck1)

            val observedCard = cardRepository.getCardById("cardId")
            assertNotNull(observedCard)

            viewModel.updatePercentualeCompletamento("deckId", 75)

            // Verifica se la percentuale di completamento è stata aggiornata correttamente nel repository
            val observedDeck2 = deckRepository.getDeckById("deckId")
            assertEquals(75, observedDeck2.percentualeCompletamento)
        }


    }

