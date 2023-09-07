class Gruppo {
  final String id;
  final String nome;

  Gruppo({required this.id, required this.nome});

  // Metodo per convertire un oggetto Gruppo in una mappa (Map)
  Map<String, dynamic> toMap() {
    return {
      'id': id,
      'nome': nome,
    };
  }

  // Metodo per creare un oggetto Gruppo da una mappa (Map)
  factory Gruppo.fromMap(Map<String, dynamic> map) {
    return Gruppo(
      id: map['id'],
      nome: map['nome'],
    );
  }
}
