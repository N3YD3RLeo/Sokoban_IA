package Structures;

public class Graphe {

	Position[][] graphe;

	public Graphe (int nbLigne, int nbColonne){
		graphe = new Position[nbLigne][nbColonne];
	}

	public Position At(int ligne, int colonne){
		return graphe[ligne][colonne];
	}

	public void At(int ligne, int colonne, Position newPos){
		graphe[ligne][colonne] = newPos;
	}

	public Position AtPos(Position position){
		return graphe[position.Ligne()][position.Colonne()];
	}

	public Position AtFromPos(Position position, int dLig, int dCol){
		return At(position.Ligne()+dLig, position.Colonne()+dCol);
	}
}
