import 'dart:convert';
import 'dart:math';

import 'package:flutter/material.dart';
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

  @override
  void initState() {
    super.initState();
    _database = FlashcardDatabase();
    _database.open().then((_) {
      print("Database aperto con successo");
      setState(() {});
    });
    _domandaController = TextEditingController();
    _rispostaController = TextEditingController();

    flashcards = [];
    _addNewFlashcard(); // Aggiungi una flashcard iniziale

    _pageController = PageController();
  }

  void _addNewFlashcard() {
    setState(() {
      flashcards.add(Flashcard(
        id: _generateRandomId(),
        domanda: '',
        risposta: '',
        isFront: true,
      ));
    });
  }

  void _salvaFlashcard() async {
    for (var flashcard in flashcards) {
      await _database.insertCard(CardX.Card(
        id: flashcard.id,
        domanda: flashcard.domanda,
        risposta: flashcard.risposta,
        completata: false,
        deckID: widget.deckId,
      ));
    }

    // Torna alla schermata precedente
    Navigator.of(context).pop();
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
                  child: Center(
                    child: Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: [
                        Text(
                          flashcard.isFront
                              ? flashcard.domanda
                              : flashcard.risposta,
                          style: TextStyle(fontSize: 24.0),
                        ),
                        SizedBox(height: 16),
                        Container(
                          width: MediaQuery.of(context).size.width * 0.6,
                          child: TextField(
                            controller: flashcard.isFront
                                ? _domandaController
                                : _rispostaController,
                            decoration: InputDecoration(
                              labelText:
                                  flashcard.isFront ? 'Domanda' : 'Risposta',
                              border: OutlineInputBorder(),
                            ),
                          ),
                        ),
                      ],
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
        children: [
          FloatingActionButton(
            onPressed: () {
              setState(() {
                flashcards[_pageController.page!.round()].toggleSide();
              });
            },
            child: Icon(Icons.refresh),
          ),
          SizedBox(height: 16),
          FloatingActionButton(
            onPressed: () {
              _addNewFlashcard();
            },
            child: Icon(Icons.add),
          ),
          SizedBox(height: 16),
          FloatingActionButton(
            onPressed: () {
              _salvaFlashcard();
            },
            child: Icon(Icons.save),
          ),
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

  Flashcard({
    required this.id,
    required this.domanda,
    required this.risposta,
    required this.isFront,
  });

  void toggleSide() {
    isFront = !isFront;
  }
}
