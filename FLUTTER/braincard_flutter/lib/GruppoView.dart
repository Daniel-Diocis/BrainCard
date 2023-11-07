import 'dart:convert';
import 'dart:math';

import 'package:braincard_flutter/Fstudio.dart';
import 'package:braincard_flutter/home.dart';
import 'package:flutter/material.dart';
import 'ModCreaFlashcard.dart';
import 'flashcard_database.dart';
import 'model/deck.dart' as DeckX;
import 'model/deck.dart';

class GruppoView extends StatefulWidget {
  final String gruppoId;

  GruppoView({required this.gruppoId});

  @override
  _GruppoViewState createState() => _GruppoViewState();
}

class _GruppoViewState extends State<GruppoView> {
  late FlashcardDatabase _database;
  final TextEditingController _newDeckNameController = TextEditingController();
  String message = ""; // Inizializza

  @override
  void initState() {
    super.initState();
    _database = FlashcardDatabase();
    _database.open().then((_) {
      print("Database aperto con successo");
      setState(() {});
    });
  }

  @override
  Widget build(BuildContext context) {
    double screenWidth = MediaQuery.of(context).size.width; //larghezza schermo
    return Scaffold(
      appBar: AppBar(
        automaticallyImplyLeading: false,
        leading: IconButton(
            icon: Icon(Icons.arrow_back),
            onPressed: () {
              // Codice da eseguire quando si preme la freccia "back"
              Navigator.pop(
                context,
                MaterialPageRoute(
                  builder: (context) => HomeScreen(),
                ),
              );
            }),
        title: Text(
          'Deck',
          style: TextStyle(
            fontSize: 20.0,
            fontWeight: FontWeight.bold,
          ),
        ),
      ),
      body: FutureBuilder<List<Deck>>(
        future: _database.getDecksWithGruppoId(widget.gruppoId),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return Center(
              child: CircularProgressIndicator(),
            );
          } else if (snapshot.hasError) {
            return Center(
              child: Text('Errore nel recupero dei dati'),
            );
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return Center(
              child: Text('Nessun deck presente'),
            );
          } else {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                final deck = snapshot.data![index];
                return Row(
                  
                  mainAxisAlignment: MainAxisAlignment.center,

                  children: [
                    Container(
                        
                        width: 0.20 * screenWidth,
                        child: Center(
                            child: Text(
                          deck.percentualeCompletamento.toString() + "% ",
                          style: TextStyle(
                            fontSize:
                                20, // Imposta la dimensione del carattere desiderata
                            fontWeight: FontWeight
                                .bold, // Imposta il testo in grassetto
                            color: (deck.percentualeCompletamento == 0)
                                ? Colors.red
                                : (deck.percentualeCompletamento == 100)
                                    ? Colors.green
                                    : Colors
                                        .black, // Colore predefinito in caso di altre percentuali
                          ),
                        ))),
                    Container(
                      width: 0.5 * screenWidth,
                      child: ElevatedButton(
                        onPressed: () {
                          Navigator.push(
                            context,
                            MaterialPageRoute(
                                builder: (context) => Fstudio(
                                      deckId: deck.id,
                                    ) 

                                ),
                          );
                        },
                        onLongPress: () async {
                          _editDeckName(deck);

                          // Aggiorna l'interfaccia utente dopo l'eliminazione
                          setState(() {});
                        },
                        child: Text(
                          deck.nome,
                          style: TextStyle(color: Colors.white),
                        ),
                        style: ElevatedButton.styleFrom(
                          textStyle: TextStyle(fontSize: 18.0),
                        ),
                      ),
                    ),
                    IconButton(
                      icon: Icon(Icons.edit),
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) =>
                                ModCreaFlashcardView(deckId: deck.id),
                          ),
                        );
                      },
                    ),
                    IconButton(
                      icon: Icon(Icons.delete),
                      onPressed: () async {
                        await _database.deleteDeck(deck.id);

                        setState(() {});
                      },
                    ),
                  ],
                );
              },
            );
          }
        },
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _showAddDeckDialog,
        child: Icon(Icons.add),
      ),
    );
  }
  

  void _showAddDeckDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Aggiungi un nuovo deck'),
          content: TextField(
            controller: _newDeckNameController,
            decoration: InputDecoration(hintText: 'Nome del deck'),
            maxLength: 18,
          ),
          actions: <Widget>[
            TextButton(
              child: Text('Annulla'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: Text('Salva'),
              onPressed: () {
                String randomId = _generateRandomId();
                String deckName = _newDeckNameController.text;
                Deck newDeck = Deck(
                  id: randomId,
                  nome: deckName,
                  percentualeCompletamento: 0,
                  idGruppo: widget.gruppoId,
                );

                _database.insertDeck(newDeck).then((_) {
                  setState(() {});
                  Navigator.of(context).pop();
                });
              },
            ),
          ],
        );
      },
    );
  }

  void _editDeckName(deck) {
    TextEditingController editingController =
        TextEditingController(text: deck.nome);

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Modifica Nome Deck'),
          content: TextField(
            controller: editingController,
            decoration: InputDecoration(hintText: 'Nuovo nome del deck'),
            maxLength: 18,
          ),
          actions: <Widget>[
            TextButton(
              child: Text('Annulla'),
              onPressed: () {
                Navigator.of(context).pop();
              },
            ),
            TextButton(
              child: Text('Salva'),
              onPressed: () async {
                String newName = editingController.text;
                
                await _database.updateDeckName(DeckX.Deck(
                  id: deck.id,
                  nome: newName,
                  percentualeCompletamento: deck.percentualeCompletamento,
                  idGruppo: deck.idGruppo,
                ));
                Navigator.of(context).pop();
              },
            ),
          ],
        );
      },
    );
  }

  String _generateRandomId() {
    final Random _random = Random.secure();
    var values = List<int>.generate(20, (i) => _random.nextInt(256));
    return base64UrlEncode(values);
  }

  @override
  void dispose() {
    _database.close();
    super.dispose();
  }
}
