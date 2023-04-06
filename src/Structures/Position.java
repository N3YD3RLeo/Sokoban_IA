package Structures;
import Global.Configuration;
import Modele.Coup;
import Structures.Sequence;

public class Position {

	int ligne, colonne;
	int distance = 0;
	boolean est_visite = false;
	Position ancetre;

	public Position (int ligne, int colonne, Position ancetre){
		this.ligne = ligne;
		this.colonne = colonne;
		this.ancetre = ancetre;
	}

	public Position (int ligne, int colonne, Position ancetre, int distance){
		this.ligne = ligne;
		this.colonne = colonne;
		this.ancetre = ancetre;
		this.distance = distance;
	}

	public int Distance(){
		return distance;
	}

	public boolean SetDistanceMin(Position newAncetre){
		if (distance == 0){
			distance = newAncetre.distance+1;
			ancetre = newAncetre;
			return true;
		}
		if (distance > newAncetre.distance+1){
			distance = newAncetre.distance+1;
			ancetre = newAncetre;
			return true;
		}
		return false;
	}
	public boolean EstVisite(){
		return est_visite;
	}

	public void Visitite(){
		est_visite = true;
	}

	public int Ligne(){
		return ligne;
	}

	public Position Ancetre(){
		return ancetre;
	}

	public int Colonne(){
		return colonne;
	}

	@Override
	public String toString() {
		return "(" + Ligne() + ", " + Colonne() + ")";
	}
}
