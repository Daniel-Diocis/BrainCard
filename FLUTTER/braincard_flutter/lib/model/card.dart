class Card {
  final String id;
  final String domanda;
  final String risposta;
  bool completata;
  final String deckID;

  Card({
    required this.id,
    required this.domanda,
    required this.risposta,
    required this.completata,
    required this.deckID,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'domanda': domanda,
      'risposta': risposta,
      'completata': completata ? 1 : 0, // Converte bool in int
      'deckID': deckID,
    };
  }

  factory Card.fromMap(Map<String, dynamic> map) {
    return Card(
      id: map['id'],
      domanda: map['domanda'],
      risposta: map['risposta'],
      completata: map['completata'] == 1, // Converte int in bool
      deckID: map['deckID'],
    );
  }
}
