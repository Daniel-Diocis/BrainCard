import 'package:flutter/material.dart';
import 'GruppoView.dart';
import 'flashcard_database.dart';
import 'model/gruppo.dart'; // Importa il modello Gruppo

class HomeScreen extends StatefulWidget {
  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
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
          'BrainCard',
          style: TextStyle(
            fontSize: 20.0,
            fontWeight: FontWeight.bold,
          ),
        ),
        backgroundColor: Colors.blue, // Cambia il colore dell'appbar
        centerTitle: false, // Sposta il titolo leggermente a sinistra
      ),
      body: FutureBuilder<List<Gruppo>>(
        future: _database.getGruppi(), // Ottieni i gruppi dal database
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
              child: Text('Nessun gruppo presente'),
            );
          } else {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                final gruppo = snapshot.data![index];
                return Row(
                  children: [
                    ElevatedButton(
                      child: Text(gruppo.nome),
                      key: Key(gruppo.id),
                      style: ElevatedButton.styleFrom(
                        primary: Colors.purple, // Cambia il colore del pulsante
                        textStyle: TextStyle(fontSize: 18.0),
                      ),
                      onPressed: () {
                        // Utilizza Navigator.push per navigare a una nuova schermata chiamata "GruppoView"
                        Navigator.push(
                          context,
                          MaterialPageRoute(
                            builder: (context) => GruppoView(
                                gruppoId: gruppo
                                    .id), // Passa l'id del gruppo alla nuova schermata
                          ),
                        );
                      },
                    ),
                    IconButton(
                      icon: Icon(Icons.delete), // Icona del cestino
                      onPressed: () async {
                        // Chiamata alla funzione per eliminare il gruppo dal database
                        await _database.deleteGruppo(gruppo.id);

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
