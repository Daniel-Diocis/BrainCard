import 'package:flutter/material.dart';
import 'flashcard_database.dart';
import 'model/card.dart' as CardX;
import 'model/deck.dart';

class DeckStudioView extends StatefulWidget {
  final String deckId;

  DeckStudioView({required this.deckId});

  @override
  _DeckStudioViewState createState() => _DeckStudioViewState();
}

class _DeckStudioViewState extends State<DeckStudioView> {
  late List<CardX.Card> cards;
  int currentIndex = 0;
  late Deck deck;
  late FlashcardDatabase _database;

  @override
  void initState() {
    super.initState();
    _database = FlashcardDatabase(); // Inizializza il database
    _database.open(); // Apre il database
    _getDeckAndCards(widget.deckId);
  }

  void _getDeckAndCards(String deckId) async {
    List<CardX.Card> cards =
        await FlashcardDatabase().getCardsWithDeckId(deckId);
    Deck deck = await FlashcardDatabase().getDeckById(deckId);

    setState(() {
      this.cards = cards;
      this.deck = deck;
    });
  }

  void handleAnswer(bool answer) {
    // Verifica se la risposta è corretta
    bool isCorrect = answer == cards[currentIndex].completata;

    // Aggiorna lo stato del deck
    if (!isCorrect) {
      double increment =
          answer ? (1 / cards.length) * 100 : -((1 / cards.length) * 100);
      deck.percentualeCompletamento =
          (deck.percentualeCompletamento + increment).toInt();

      // Aggiorna la carta come completata
      answer
          ? cards[currentIndex].completata = true
          : cards[currentIndex].completata = false;
      // Aggiorna il database
      _updateCardAndDeck();
    }

    // Passa alla prossima carta
    if (currentIndex < cards.length - 1) {
      setState(() {
        currentIndex++;
      });
    } else {
      var gruppoId = deck.idGruppo;
      // Fine delle carte del deck, torna a GruppoView
      Navigator.of(context).pop(gruppoId); // Passa gruppoId come risultato
    }
  }

  void _updateCardAndDeck() async {
    await FlashcardDatabase().updateCard(cards[currentIndex]);
    await FlashcardDatabase().updateDeck(deck);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Flashcard Studio',
          style: TextStyle(
            fontSize: 20.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          GestureDetector(
            onTap: () {
              // Mostra la risposta
              setState(() {
                cards[currentIndex].completata =
                    !cards[currentIndex].completata;
              });
            },
            child: Container(
              padding: EdgeInsets.all(16.0),
              color: Colors.blue, // Colore della flashcard
              child: Center(
                child: Text(
                  cards[currentIndex].completata
                      ? cards[currentIndex].risposta
                      : cards[currentIndex].domanda,
                  style: TextStyle(fontSize: 24.0, color: Colors.white),
                ),
              ),
            ),
          ),
          Row(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: [
              ElevatedButton(
                onPressed: () {
                  handleAnswer(true);
                },
                child: Icon(Icons.check, color: Colors.white),
                style: ElevatedButton.styleFrom(primary: Colors.green),
              ),
              ElevatedButton(
                onPressed: () {
                  handleAnswer(false);
                },
                child: Icon(Icons.close, color: Colors.white),
                style: ElevatedButton.styleFrom(primary: Colors.red),
              ),
            ],
          ),
        ],
      ),
    );
  }
}
