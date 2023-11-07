import 'dart:convert';
import 'dart:math';

import 'package:flutter/material.dart';
import 'GruppoView.dart';
import 'flashcard_database.dart';
import 'model/card.dart' as CardX;

class ModCreaFlashcardView extends StatefulWidget {
  final String deckId;

  ModCreaFlashcardView({required this.deckId});

  @override
  _ModCreaFlashcardViewState createState() => _ModCreaFlashcardViewState();
}

class _ModCreaFlashcardViewState extends State<ModCreaFlashcardView> {
  late PageController _pageController;
  late List<Flashcard> flashcards;
  late TextEditingController _domandaController;
  late TextEditingController _rispostaController;
  late FlashcardDatabase _database;
  late String gruppoId;

  @override
  void initState() {
    super.initState();
    _domandaController = TextEditingController();
    _rispostaController = TextEditingController();

    flashcards = [];

    _pageController = PageController();

    _database = FlashcardDatabase();
    _database.open().then((_) {
      print("Database aperto con successo");

      
      _database.getCardsWithDeckId(widget.deckId).then((cards) {
        setState(() {
          for (var card in cards) {
            var newFlashcard = Flashcard(
              id: card.id,
              domanda: card.domanda,
              risposta: card.risposta,
              isFront: true,
            );
            newFlashcard.domandaController =
                TextEditingController(text: card.domanda);
            newFlashcard.rispostaController =
                TextEditingController(text: card.risposta);
            flashcards.add(newFlashcard);
            print(card.toString());
            print(card.domanda);
            print(card.risposta);
          }
        });
      });
      _database.getDeckById(widget.deckId).then((deck) {
        setState(() {
          gruppoId = deck.idGruppo;
        });
      });
    });
  }

  void _addNewFlashcard() {
    setState(() {
      var newFlashcard = Flashcard(
        id: _generateRandomId(),
        domanda: '',
        risposta: '',
        isFront: true,
      );
      flashcards.add(newFlashcard);
      
      newFlashcard.domandaController = TextEditingController();
      newFlashcard.rispostaController = TextEditingController();
      _pageController.animateToPage(
        flashcards.length - 1, // Indice della nuova flashcard
        duration: Duration(milliseconds: 500),
        curve: Curves.easeOut,
      );
    });
  }

  void _salvaFlashcard() async {
    for (var flashcard in flashcards) {
      flashcard.domanda = flashcard.domandaController.text;
      flashcard.risposta = flashcard.rispostaController.text;

      // Verifica se la carta con questo ID esiste già nel database
      var existingCard = await _database.getCardById(flashcard.id);

      if (existingCard != null) {
        // La carta esiste già, quindi esegui un aggiornamento invece di un'aggiunta
        await _database.updateCard(CardX.Card(
          id: flashcard.id,
          domanda: flashcard.domanda,
          risposta: flashcard.risposta,
          completata: 0,
          deckID: widget.deckId,
        ));
      } else {
        // La carta non esiste, esegui un'aggiunta normale
        await _database.insertCard(CardX.Card(
          id: flashcard.id,
          domanda: flashcard.domanda,
          risposta: flashcard.risposta,
          completata: 0,
          deckID: widget.deckId,
        ));
      }
    }
    // Torna alla schermata precedente
    Navigator.pushReplacement(
      context,
      MaterialPageRoute(
        builder: (context) => GruppoView(gruppoId: gruppoId),
      ),
    ).then((_) {
      setState(() {});
    });
  }

  void _deleteFlashcard() async {
    var cardId = flashcards[_pageController.page!.round()].id;
    _database.deleteCard(cardId);
    for (var flashcard in flashcards) {
      if (flashcard.id == cardId) {
        // se la carta era nuova e non salvata viene solo tolta dalla visualizzazione
        setState(() {
          flashcards.remove(flashcard);
        });
      }
    }
  }

  String _generateRandomId() {
    final Random _random = Random.secure();
    var values = List<int>.generate(20, (i) => _random.nextInt(256));
    return base64UrlEncode(values);
  }

  @override
  void dispose() {
    _pageController.dispose();
    _database.close();
    _domandaController.dispose();
    _rispostaController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
         automaticallyImplyLeading: false,
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              
              Navigator.pushReplacement(
                context,
                MaterialPageRoute(
                  builder: (context) => GruppoView(
                    gruppoId: gruppoId,
                  ),
                ),
              );
            }),
        title: Text(
          'Modifica/Aggiungi Flashcard',
          style: TextStyle(
            fontSize: 20.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: PageView(
        controller: _pageController,
        children: flashcards.map<Widget>((flashcard) {
          return Center(
            child: GestureDetector(
              onHorizontalDragEnd: (DragEndDetails details) {
                if (details.primaryVelocity! > 0) {
                  if (_pageController.page != null &&
                      _pageController.page!.round() > 0) {
                    _pageController.previousPage(
                      duration: Duration(milliseconds: 500),
                      curve: Curves.easeOut,
                    );
                  }
                } else if (details.primaryVelocity! < 0) {
                  if (_pageController.page != null &&
                      _pageController.page!.round() < flashcards.length - 1) {
                    _pageController.nextPage(
                      duration: Duration(milliseconds: 500),
                      curve: Curves.easeOut,
                    );
                  }
                }
              },
              child: AnimatedSwitcher(
                duration: Duration(milliseconds: 500),
                child: Container(
                  height: MediaQuery.of(context).size.height - 60,
                  width: MediaQuery.of(context).size.width * 0.8,
                  padding: EdgeInsets.all(16.0),
                  color: Colors.grey[200],
                  child: Align(
                    alignment: Alignment.topCenter,
                    child: TextField(
                      controller: flashcard.isFront
                          ? flashcard.domandaController
                          : flashcard.rispostaController,
                      maxLines: 30,
                      maxLength: 600,
                      decoration: InputDecoration(
                        hintText: (flashcard.isFront
                            ? 'Domanda'
                            : 'Risposta'), // Testo suggerito
                      ),
                    ),
                  ),
                ),
              ),
            ),
          );
        }).toList(),
      ),
      floatingActionButton: Column(
        mainAxisAlignment: MainAxisAlignment.end,
        verticalDirection: VerticalDirection.down,
        children: [
          FloatingActionButton(
            onPressed: () {
              setState(() {
                flashcards[_pageController.page!.round()].toggleSide();
              });
            },
            child: Icon(Icons.refresh),
          ),
          SizedBox(height: 12),
          FloatingActionButton(
            onPressed: () {
              _addNewFlashcard();
            },
            child: Icon(Icons.add),
          ),
          SizedBox(height: 12),
          FloatingActionButton(
            onPressed: () {
              _salvaFlashcard();
            },
            child: Icon(Icons.save),
          ),
          SizedBox(height: 12),
          FloatingActionButton(
            onPressed: () {
              _deleteFlashcard();
            },
            child: Icon(Icons.delete),
          ),
          SizedBox(height: 22),
        ],
      ),
    );
  }
}

class Flashcard {
  final String id;
  String domanda;
  String risposta;
  bool isFront;
  int completata;
  late TextEditingController
      domandaController; // Nuovo controller per la domanda
  late TextEditingController
      rispostaController; // Nuovo controller per la risposta

  Flashcard({
    required this.id,
    required this.domanda,
    required this.risposta,
    required this.isFront,
    this.completata = 0,
  });

  void toggleSide() {
    isFront = !isFront;
  }
}
