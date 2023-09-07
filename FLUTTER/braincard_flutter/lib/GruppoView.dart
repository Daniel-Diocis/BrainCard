import 'package:flutter/material.dart';
import 'flashcard_database.dart';
import 'model/deck.dart';
import 'DeckStudioView.dart';

class GruppoView extends StatefulWidget {
  final String gruppoId;

  GruppoView({required this.gruppoId});

  @override
  _GruppoViewState createState() => _GruppoViewState();
}

class _GruppoViewState extends State<GruppoView> {
  late FlashcardDatabase _database;

  @override
  void initState() {
    super.initState();
    _database = FlashcardDatabase(); // Inizializza il database
    _database.open(); // Apre il database
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(
          'Deck', // Titolo "Deck" nella app bar
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
                  children: [
                    ElevatedButton(
                      onPressed: () {
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => DeckStudioView(
                                deckId: deck
                                    .id), // Passa l'id del gruppo alla nuova schermata
                          ),
                        );
                      },
                      child: Text(deck.nome),
                      style: ElevatedButton.styleFrom(
                        primary: Colors.purple,
                        textStyle: TextStyle(fontSize: 18.0),
                      ),
                    ),
                    IconButton(
                      icon: Icon(Icons.edit), // Icona di matita
                      onPressed: () {
                        // Gestisci l'azione di modifica qui
                      },
                    ),
                    IconButton(
                      icon: Icon(Icons.delete), // Icona del cestino
                      onPressed: () async {
                        await _database.deleteDeck(deck.id);

                        // Aggiorna l'interfaccia utente dopo l'eliminazione
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
    );
  }

  @override
  void dispose() {
    _database.close(); // Chiudi il database quando la schermata viene distrutta
    super.dispose();
  }
}
