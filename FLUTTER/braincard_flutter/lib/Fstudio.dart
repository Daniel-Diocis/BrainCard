import 'package:flip_card/flip_card.dart';
import 'package:flutter/material.dart';
import 'flashcard_database.dart';
import 'model/card.dart' as CardX;
import 'model/deck.dart';
import 'dart:math';


class Fstudio extends StatefulWidget{
  final String deckId;

  Fstudio({required this.deckId});
  @override
  State<StatefulWidget> createState() => FstudioState();
}

class FstudioState extends State<Fstudio>{
  late List<CardX.Card> cards;
  late List<Flashcard> flashcards;
  late Deck deck;
  late FlashcardDatabase _database;
  int nCarta=0;
  CardSide frontCard=CardSide.FRONT;
  late int nCarte;
  bool visibile=false;
  GlobalKey<FlipCardState> cardKey = GlobalKey<FlipCardState>();
  late bool bottoni;
  double _posBottoni=-100;
  double _opacity=0;

  @override
  void initState() {
    super.initState();
    flashcards = [];
    
    
    _database = FlashcardDatabase(); // Inizializza il database
    _database.open().then((_) {
      print("Database aperto con successo");

      // Ora carica le flashcard dal database
      _database.getCardsWithDeckId(widget.deckId).then((cards) {
        setState(() {
          print("okokok");
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
          nCarte=flashcards.length;
          print("object 1");
          print(nCarte);
        });
      });
    }); // Apre il database
    
  }
  

  void caricaCarta()async{
    Random random = Random();
    int randomNumber = random.nextInt(nCarte);
    if(cardKey.currentState?.isFront==false){
     await  cardKey.currentState?.toggleCard();
     await Future.delayed(const Duration(milliseconds: 200)); // Attendere 500 millisecondi (puoi regolare il valore)
    }
    
    
    setState(() {
      nCarta=randomNumber;
      _opacity=0;
      _posBottoni=-100;
      
    });
    
    print(nCarta);

  }
   

  

  Widget build(BuildContext context){
   return MaterialApp(
    home: Scaffold(
      appBar: AppBar(
        title: Text('Il tuo titolo'),
      ),
      body: Stack(
        children: <Widget>[
          Center(
          child: GestureDetector(
            onTap: () {
              // Codice da eseguire quando si tocca il widget Center
              setState(() {
                cardKey.currentState?.toggleCard();
                visibile=true;
                _posBottoni=20;
                _opacity=1;
              });
              print("gira Carta");
      
      
    },
    child: SizedBox(
      height: 500,
      width: 300,
      child: FlipCard(
        key: cardKey,
        fill: Fill.fillBack,
        direction: FlipDirection.HORIZONTAL,
        flipOnTouch: false,
        side: frontCard,
        front: Container(
          child: Text(flashcards[nCarta].domanda),
        ),
        back: Container(
          child: Text(flashcards[nCarta].risposta),
              ),
            ),
          ),
        ),
      ),
           AnimatedPositioned(
            width: MediaQuery.of(context).size.width,
            
            duration:Duration(milliseconds: 200),
            bottom: _posBottoni,
            child:Align(
              alignment: Alignment.center,
            
            
            
            
             // Imposta questa opzione su true se desideri che sia inizialmente visibile
            child:AnimatedOpacity(
              duration: Duration(milliseconds: 500),
              opacity: _opacity,
            
              child: 
                 // Spazio inferiore
                
                 Row(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    Container(
                      width: 80,
                      height: 60,
                    child: ElevatedButton(
                      onPressed: () {
                        caricaCarta();
                      },
                      child: Icon(Icons.check, color: Colors.white),
                      style: ElevatedButton.styleFrom(primary: Colors.green),
                      
                    ),
                    ),
                    Container(
                      width: 80,
                      height: 60,
                      child: ElevatedButton(
                      onPressed: () {
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
  });

  void toggleSide() {
    isFront = !isFront;
  }
}