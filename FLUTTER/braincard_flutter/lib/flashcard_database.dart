import 'package:flutter/widgets.dart';
import 'package:sqflite/sqflite.dart';
import 'package:path/path.dart';

import 'model/card.dart';
import 'model/deck.dart';
import 'model/gruppo.dart';

class FlashcardDatabase {
  late Database _db;

  Future<void> open() async {
    WidgetsFlutterBinding.ensureInitialized();
    final databasesPath = await getDatabasesPath();
    final path = join(databasesPath, 'flashcard.db');
    print('Il percorso della directory dei database è: $databasesPath');
    print('Il percorso completo del database è: $path');

    _db = await openDatabase(path, version: 1,
        onCreate: (Database db, int version) async {
      await db.execute('''
        CREATE TABLE Gruppo (
          id TEXT PRIMARY KEY,
          nome TEXT
        )
      ''');

      await db.execute('''
        CREATE TABLE Deck (
          id TEXT PRIMARY KEY,
          nome TEXT,
          percentualeCompletamento INTEGER,
          idGruppo TEXT,
          FOREIGN KEY (idGruppo) REFERENCES Gruppo(id)
        )
      ''');

      await db.execute('''
        CREATE TABLE Card (
          id TEXT PRIMARY KEY,
          domanda TEXT,
          risposta TEXT,
          completata INTEGER,
          deckID TEXT,
          FOREIGN KEY (deckID) REFERENCES Deck(id)
        )
      ''');
    });
  }

  Future<void> insertGruppo(Gruppo gruppo) async {
    await _db.insert('Gruppo', gruppo.toMap());
  }

  Future<void> insertDeck(Deck deck) async {
    await _db.insert('Deck', deck.toMap());
  }

  Future<Card?> getCardById(String cardId) async {
    List<Map<String, dynamic>> result =
        await _db.query('Card', where: 'id = ?', whereArgs: [cardId]);

    if (result.isNotEmpty) {
      return Card(
        id: result.first['id'],
        domanda: result.first['domanda'],
        risposta: result.first['risposta'],
        completata: result.first['completata'],
        deckID: result.first['deckID'],
      );
    } else {
      return null;
    }
  }

  Future<void> insertCard(Card card) async {
    await _db.insert('Card', card.toMap());
  }

  // Altri metodi per eseguire query, aggiornamenti e eliminazioni

  Future<List<Gruppo>> getGruppi() async {
    final List<Map<String, dynamic>> maps = await _db.query('Gruppo');
    return List.generate(maps.length, (i) {
      return Gruppo.fromMap(maps[i]);
    });
  }

  Future<void> deleteGruppo(String gruppoId) async {
    await _db.delete(
      'Gruppo',
      where: 'id = ?',
      whereArgs: [gruppoId],
    );
  }

  Future<void> deleteDeck(String deckId) async {
    await _db.delete(
      'Deck',
      where: 'id = ?',
      whereArgs: [deckId],
    );
  }

  Future<void> deleteCard(String cardId) async {
    await _db.delete(
      'Card',
      where: 'id=?',
      whereArgs: [cardId],
    );
  }

  Future<List<Deck>> getDecksWithGruppoId(String gruppoId) async {
    final List<Map<String, dynamic>> maps = await _db.query(
      'deck',
      where: 'idGruppo = ?',
      whereArgs: [gruppoId],
    );

    return List.generate(maps.length, (i) {
      return Deck(
        id: maps[i]['id'],
        nome: maps[i]['nome'],
        percentualeCompletamento: maps[i]['percentualeCompletamento'],
        idGruppo: maps[i]['idGruppo'],
      );
    });
  }

  Future<List<Card>> getCardsWithDeckId(String deckId) async {
    final List<Map<String, dynamic>> maps = await _db.query(
      'Card',
      where: 'deckID = ?',
      whereArgs: [deckId],
    );

    return List.generate(maps.length, (i) {
      return Card(
        id: maps[i]['id'],
        domanda: maps[i]['domanda'],
        risposta: maps[i]['risposta'],
        completata: maps[i]['completata'],
        deckID: maps[i]['deckID'],
      );
    });
  }

  Future<void> updateCard(Card card) async {
    await _db.update(
      'Card',
      card.toMap(),
      where: 'id = ?',
      whereArgs: [card.id],
    );
  }

  Future<void> updateDeck(Deck deck) async {
    await _db.update(
      'Deck',
      deck.toMap(),
      where: 'id = ?',
      whereArgs: [deck.id],
    );
  }

  Future<void> updateGroupName(Gruppo gruppo) async {
    print(gruppo.nome);
    await _db.update(
      'Gruppo',
      gruppo.toMap(),
      where: 'id = ?',
      whereArgs: [gruppo.id],
    );
  }

  Future<void> updateDeckName(Deck deck) async {
    print(deck.nome);
    await _db.update(
      'Deck',
      deck.toMap(),
      where: 'id = ?',
      whereArgs: [deck.id],
    );
  }

  Future<Deck> getDeckById(String deckId) async {
    List<Map<String, dynamic>> result =
        await _db.query('Deck', where: 'id = ?', whereArgs: [deckId]);

    if (result.isNotEmpty) {
      return Deck.fromMap(result.first);
    } else {
      throw Exception("Deck non trovato");
    }
  }
  Future<void> updateDeckCompletion(String deckId, int newCompletionPercentage) async {
  await _db.update(
    'Deck',
    {'percentualeCompletamento': newCompletionPercentage},
    where: 'id = ?',
    whereArgs: [deckId],
  );
}

  Future<void> close() async {
    await _db.close();
  }
}
