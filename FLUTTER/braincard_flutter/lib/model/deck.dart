class Deck {
  final String id;
  final String nome;
  int percentualeCompletamento;
  final String idGruppo;

  Deck({
    required this.id,
    required this.nome,
    required this.percentualeCompletamento,
    required this.idGruppo,
  });

  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'nome': nome,
      'percentualeCompletamento': percentualeCompletamento,
      'idGruppo': idGruppo,
    };
  }

  factory Deck.fromMap(Map<String, dynamic> map) {
    return Deck(
      id: map['id'],
      nome: map['nome'],
      percentualeCompletamento: map['percentualeCompletamento'],
      idGruppo: map['idGruppo'],
    );
  }
}
