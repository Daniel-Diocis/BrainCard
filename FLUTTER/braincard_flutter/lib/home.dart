import 'dart:convert';

import 'package:flutter/material.dart';
import 'GruppoView.dart';
import 'flashcard_database.dart';
import 'model/gruppo.dart' as GruppoX;
import 'dart:math';

class HomeScreen extends StatefulWidget {
  const HomeScreen({super.key});

  @override
  _HomeScreenState createState() => _HomeScreenState();
}

class _HomeScreenState extends State<HomeScreen> {
  late FlashcardDatabase _database;
  final TextEditingController _newGroupNameController = TextEditingController();

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
    double screenWidth = MediaQuery.of(context).size.width;
    return Scaffold(
      appBar: AppBar(
        title: const Text(
          'BrainCard',
          style: TextStyle(
            fontSize: 20.0,
            fontWeight: FontWeight.bold,
          ),
        ),
        centerTitle: false,
      ),
      body: FutureBuilder<List<GruppoX.Gruppo>>(
        future: _database.getGruppi(),
        builder: (context, snapshot) {
          if (snapshot.connectionState == ConnectionState.waiting) {
            return const Center(
              child: CircularProgressIndicator(),
            );
          } else if (snapshot.hasError) {
            print(snapshot.error);
            return const Center(
              child: Text('Errore nel recupero dei dati'),
            );
          } else if (!snapshot.hasData || snapshot.data!.isEmpty) {
            return const Center(
              child: Text('Nessun gruppo presente'),
            );
          } else {
            return ListView.builder(
              itemCount: snapshot.data!.length,
              itemBuilder: (context, index) {
                final gruppo = snapshot.data![index];
                return Row(
                  mainAxisAlignment: MainAxisAlignment.center,
                  children: [
                    Container(
                      width: 0.7 * screenWidth,
                      child: ElevatedButton(
                        child: Text(
                          gruppo.nome,
                          style: TextStyle(color: Colors.white),
                        ),
                        key: Key(gruppo.id),
                        style: ElevatedButton.styleFrom(
                          textStyle: const TextStyle(fontSize: 18.0),
                          //minimumSize:
                          //Size(MediaQuery.of(context).size.width - 36, 50),
                          //COME SONO I BOTTONI??
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
                    ),
                    IconButton(
                      icon: const Icon(Icons.edit),
                      onPressed: () async {
                        _editGroupName(gruppo);

                        // Aggiorna l'interfaccia utente dopo l'eliminazione
                        setState(() {});
                      },
                    ),
                    IconButton(
                      icon: const Icon(Icons.delete), // Icona del cestino
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
      floatingActionButton: FloatingActionButton(
        onPressed: _showAddGroupDialog,
        child: Icon(Icons.add),
      ),
    );
  }

  void _showAddGroupDialog() {
    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Aggiungi un nuovo gruppo'),
          content: TextField(
            controller: _newGroupNameController,
            decoration: InputDecoration(hintText: 'Nome del gruppo'),
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
                String groupName = _newGroupNameController.text;
                GruppoX.Gruppo newGroup =
                    GruppoX.Gruppo(id: randomId, nome: groupName);

                _database.insertGruppo(newGroup).then((_) {
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

  void _editGroupName(gruppo) {
    TextEditingController editingController =
        TextEditingController(text: gruppo.nome);

    showDialog(
      context: context,
      builder: (BuildContext context) {
        return AlertDialog(
          title: Text('Modifica Nome Gruppo'),
          content: TextField(
            controller: editingController,
            decoration: InputDecoration(hintText: 'Nuovo nome del gruppo'),
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
                // Aggiorna il nome del gruppo nel database
                await _database.updateGroupName(GruppoX.Gruppo(
                  id: gruppo.id,
                  nome: newName,
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
