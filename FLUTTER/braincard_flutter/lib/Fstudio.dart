import 'package:flip_card/flip_card.dart';
import 'package:flutter/material.dart';
import 'flashcard_database.dart';
import 'model/card.dart' as CardX;
import 'model/deck.dart';
import 'package:fluttertoast/fluttertoast.dart';

class Fstudio extends StatefulWidget {
  final String deckId;

  Fstudio({required this.deckId});
  @override
  State<StatefulWidget> createState() => FstudioState();
}

class FstudioState extends State<Fstudio> {
  late List<CardX.Card> cards;
  late List<Flashcard> flashcards;
  late Deck deck;
  late FlashcardDatabase _database;
  int nCarta = 0;
  CardSide frontCard = CardSide.FRONT;
  late int nCarte;
  bool visibile = false;
  GlobalKey<FlipCardState> cardKey = GlobalKey<FlipCardState>();
  late bool bottoni;
  double _posBottoni = -100;
  double _opacity = 0;

  @override
  void initState() {
    super.initState();
    flashcards = [];

    _database = FlashcardDatabase(); // Inizializza il database
    _database.open().then((_) {
      print("Database aperto con successo");
      
      _database.getCardsWithDeckId(widget.deckId).then((cards) {
        setState(() {
          if (cards.length == 0) {
            showCenterToast("il deck non presenta flashcard.");
            Navigator.pop(context);
          }

          for (var card in cards) {
            var newFlashcard = Flashcard(
              id: card.id,
              domanda: card.domanda,
              risposta: card.risposta,
              isFront: true,
              completata: card.completata,
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
          nCarte = flashcards.length;
          flashcards.sort((a, b) => a.completata.compareTo(b
              .completata)); //ordina da quelle che so meno a quello che so di più
        });
      });
    }); // Apre il database
  }

  //popUp
  void showCenterToast(String message) {
    Fluttertoast.showToast(
      msg: message,
      toastLength: Toast.LENGTH_SHORT, // Durata di 3 secondi
      gravity: ToastGravity.CENTER,
      timeInSecForIosWeb: 4,
      backgroundColor: Color.fromARGB(255, 55, 71, 78),
      textColor: Colors.white,
    );
  }

  void salvaProgressi() async {
    double progresso = 0;
    int max = flashcards.length;
    for (var card in flashcards) {
      if (card.completata == 3) {
        progresso += 1;
      }
    }
    progresso = (progresso * 100);
    progresso = (progresso / max);
    int progressoInt = progresso.toInt();
    
    print(progressoInt.toString() + "%");
    await _database.updateDeckCompletion(widget.deckId, progressoInt).then((_){
      setState(() {
      
    });
    });
    
    
  }

  void caricaCarta() async {
    if (nCarta < nCarte - 1) {
      nCarta++;
    } else {
      nCarta = 0;
      flashcards.sort((a, b) => a.completata.compareTo(b.completata));
      for (var card in flashcards) {
        print(card.domanda + " : " + card.completata.toString());
      }
    }
    if (cardKey.currentState?.isFront == false) {
      await cardKey.currentState?.toggleCard();
      await Future.delayed(const Duration(
          milliseconds:
              200)); // Attendere 500 millisecondi (puoi regolare il valore)
    }

    setState(() {
      nCarta = nCarta;

      _opacity = 0;
      _posBottoni = -100;
    });

    print(nCarta);
  }

  void modCompletata() async {
    await _database.updateCard(CardX.Card(
      id: flashcards[nCarta].id,
      domanda: flashcards[nCarta].domanda,
      risposta: flashcards[nCarta].risposta,
      completata: flashcards[nCarta].completata,
      deckID: widget.deckId,
    ));
  }

  Widget build(BuildContext context) {
    return WillPopScope(
      onWillPop: () async {
        salvaProgressi();
        return true; 
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text("Studio"),
        ),
        body: Stack(
          children: <Widget>[
            Center(
              child: GestureDetector(
                onTap: () {
                  // Codice da eseguire quando si tocca il widget Center
                  setState(() {
                    cardKey.currentState?.toggleCard();
                    visibile = true;
                    _posBottoni = 20;
                    _opacity = 1;
                  });
                  print("gira Carta");
                },
                child: Container(
                  height: 500,
                  width: 300,
                  child: FlipCard(
                    key: cardKey,
                    fill: Fill.fillBack,
                    direction: FlipDirection.HORIZONTAL,
                    flipOnTouch: false,
                    side: frontCard,
                    front: Container(
                      color: Color.fromARGB(255, 235, 240, 242),
                      child: Align(
                        alignment: Alignment.center,
                        child: Transform.scale(
                          scale: 1, // Imposta la scala desiderata

                          child: Text(
                            flashcards[nCarta].domanda,
                            style: TextStyle(
                              fontSize:
                                  20, // Imposta la dimensione del carattere desiderata
                              fontWeight: FontWeight
                                  .bold, // Imposta il testo in grassetto
                              color: Colors.black,
                            ),
                          ),
                        ),
                      ),
                    ),
                    back: Container(
                      color: Color.fromARGB(255, 235, 240, 242),
                      child: Align(
                        alignment: Alignment.center,
                        child: Transform.scale(
                          scale: 1, // Imposta la scala desiderata

                          child: Text(
                            flashcards[nCarta].risposta,
                            style: TextStyle(
                              fontSize:
                                  20, // Imposta la dimensione del carattere desiderata
                              fontWeight: FontWeight
                                  .bold, // Imposta il testo in grassetto
                              color: Colors.black,
                            ),
                          ),
                        ),
                      ),
                    ),
                  ),
                ),
              ),
            ),
            AnimatedPositioned(
              width: MediaQuery.of(context).size.width,
              duration: Duration(milliseconds: 200),
              bottom: _posBottoni,
              child: Align(
                alignment: Alignment.center,

                
                child: AnimatedOpacity(
                  duration: Duration(milliseconds: 500),
                  opacity: _opacity,
                  child:
                      
                      Row(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: <Widget>[
                      Container(
                        width: 80,
                        height: 60,
                        child: ElevatedButton(
                          onPressed: () {
                            if (flashcards[nCarta].completata < 3) {
                              flashcards[nCarta].completata += 1;
                              modCompletata();
                            }
                            print("completamento: " +
                                flashcards[nCarta].completata.toString());
                            salvaProgressi();
                            caricaCarta();
                          },
                          child: Icon(Icons.check, color: Colors.white),
                          style:
                              ElevatedButton.styleFrom(primary: Colors.green),
                        ),
                      ),
                      Container(
                        width: 80,
                        height: 60,
                        child: ElevatedButton(
                          onPressed: () {
                            if (flashcards[nCarta].completata > 0) {
                              flashcards[nCarta].completata -= 1;
                              modCompletata();
                            }
                            print("completamento: " +
                                flashcards[nCarta].completata.toString());
                            salvaProgressi();
                            caricaCarta();
                          },
                          child: Icon(Icons.close, color: Colors.white),
                          style: ElevatedButton.styleFrom(primary: Colors.red),
                        ),
                      ),
                    ],
                  ),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}

class Flashcard {
  final String id;
  String domanda;
  String risposta;
  int completata;
  bool isFront;
  late TextEditingController
      domandaController; // Nuovo controller per la domanda
  late TextEditingController
      rispostaController; // Nuovo controller per la risposta

  Flashcard({
    required this.id,
    required this.domanda,
    required this.risposta,
    required this.isFront,
    required this.completata,
  });

  void toggleSide() {
    isFront = !isFront;
  }
}
