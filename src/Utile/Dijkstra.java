package Utile;

import Modele.Coup;
import Modele.Niveau;
import Structures.Graphe;
import Structures.Position;
import Structures.Sequence;

public class Dijkstra {

	Niveau niveau;
	Graphe accessiblePousseur;
	Coup marque = new Coup();
	Position pousseur;
	public Position positionDevantCaisse;
	Position caisse;

	public Dijkstra(Niveau niveau){
		this.niveau = niveau;
		accessiblePousseur = new Graphe(niveau.lignes(), niveau.colonnes());
	}

	public void VerifCheminVersCaisse(Position position, int dLig, int dCol){
		int newLig = position.Ligne()+dLig;
		int newCol = position.Colonne()+dCol;

		if(niveau.aCaisse(newLig, newCol) && (niveau.estOccupable(newLig+dLig, newCol+dCol) || niveau.aPousseur(newLig+dLig, newCol+dCol))){
			marque.ajouteMarque(position.Ligne(), position.Colonne(), 0x2222AA);
			positionDevantCaisse = position;
			caisse = new Position(newLig, newCol, null);
		}
		Position nextPos = accessiblePousseur.At(newLig, newCol);
		if(niveau.estOccupable(newLig, newCol)){
			if(nextPos == null) {
				nextPos = new Position(newLig, newCol, position, position.Distance() + 1);
				accessiblePousseur.At(newLig, newCol, nextPos);
				PlusCourtCheminVersCaisse(nextPos);
			}
			else if(nextPos.SetDistanceMin(position)){
				VerifCheminVersCaisse(nextPos, 1, 0);
				VerifCheminVersCaisse(nextPos, -1, 0);
				VerifCheminVersCaisse(nextPos, 0, 1);
				VerifCheminVersCaisse(nextPos, 0, -1);
			}
		}
	}

	public void InitPlusCourtCheminVersCaisse(int ligne, int colonne){
		pousseur = new Position(ligne, colonne, null);
		accessiblePousseur.At(ligne, colonne, pousseur);
		PlusCourtCheminVersCaisse(pousseur);
	}
	public void PlusCourtCheminVersCaisse(Position position){
		if (accessiblePousseur.AtPos(position).EstVisite()){
			return;
		}
		accessiblePousseur.AtPos(position).Visitite();
		marque.ajouteMarque(position.Ligne(), position.Colonne(), 0x22AA22);
		VerifCheminVersCaisse(position, 1, 0);
		VerifCheminVersCaisse(position, -1, 0);
		VerifCheminVersCaisse(position, 0, -1);
		VerifCheminVersCaisse(position, 0, 1);
	}

	public Sequence<Coup> DeplacerVersPos(Sequence<Coup> resultat, Position position){
		Position currentPos = position;
		Position ancetre = position.Ancetre();
		while (ancetre != null){
			Coup deplacement = new Coup();

			deplacement.deplacementPousseur(ancetre.Ligne(), ancetre.Colonne(), currentPos.Ligne(), currentPos.Colonne());
			currentPos = ancetre;
			ancetre = currentPos.Ancetre();

			resultat.insereTete(deplacement);
		}
		return resultat;
	}

	public Coup Marque(){
		return marque;
	}
}
