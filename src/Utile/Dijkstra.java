package Utile;

import Global.Configuration;
import Modele.Coup;
import Modele.Niveau;
import Structures.Graphe;
import Structures.Position;
import Structures.Sequence;

public class Dijkstra {

	Niveau niveau;
	Graphe accessiblePousseur;
	Graphe accessibleCaisse;
	Coup marque = new Coup();
	Position pousseur;
	public Position positionDevantCaisse;
	public Position caisse;

	public Position caisseAvant;
	Position newCaisse;
	public Position positionBut;


	public Dijkstra(Niveau niveau){
		this.niveau = niveau;
	}

	public void VerifButAccessible(Position positionCaisse, Position positionPousseur, int dLig, int dCol){
		int newLig = positionCaisse.Ligne()+dLig;
		int newCol = positionCaisse.Colonne()+dCol;

		int befLig = positionCaisse.Ligne()-dLig;
		int befCol = positionCaisse.Colonne()-dCol;

		InitCaisseAccessible(positionPousseur.Ligne(), positionPousseur.Colonne(), caisseAvant.Ligne(), caisseAvant.Colonne(), positionCaisse.Ligne(),positionCaisse.Colonne());

		Position nextPos = accessibleCaisse.At(newLig, newCol);
		Position nextPosPousseur = new Position(befLig, befCol, null);
		if((niveau.aBut(newLig, newCol)) && (accessiblePousseur.At(befLig, befCol) != null) && (accessiblePousseur.At(befLig, befCol).EstVisite())){
			marque.ajouteMarque(newLig, newCol, 0xAAAA22);
			positionBut = new Position(newLig, newCol, positionCaisse);
		}
		else if((niveau.estOccupable(newLig, newCol) || niveau.aPousseur(newLig, newCol)) && (accessiblePousseur.At(befLig, befCol) != null) && (accessiblePousseur.At(befLig, befCol).EstVisite())){
			if(nextPos == null) {
				nextPos = new Position(newLig, newCol, positionCaisse, positionCaisse.Distance() + 1);
				accessibleCaisse.At(newLig, newCol, nextPos);
				ButAccessible(nextPos, nextPosPousseur);
			}
			else if(nextPos.SetDistanceMin(positionCaisse)){
				VerifButAccessible(nextPos, nextPosPousseur, 1, 0);
				VerifButAccessible(nextPos, nextPosPousseur, -1, 0);
				VerifButAccessible(nextPos, nextPosPousseur, 0, 1);
				VerifButAccessible(nextPos, nextPosPousseur, 0, -1);
			}
		}
	}

	public void InitButAccessible(int ligne, int colonne, int lignePousseur, int colonnePousseur){
		caisseAvant = new Position(ligne, colonne, null);
		accessibleCaisse = new Graphe(niveau.lignes(), niveau.colonnes());
		newCaisse = new Position(ligne, colonne, null);
		accessibleCaisse.At(ligne, colonne, newCaisse);
		Position pousseur = new Position(lignePousseur, colonnePousseur, null);
		accessibleCaisse.At(lignePousseur, colonnePousseur, pousseur);
		ButAccessible(newCaisse, pousseur);
	}

	public void ButAccessible(Position positionCaisse, Position positionPousseur){
		if (accessibleCaisse.AtPos(positionCaisse).EstVisite()){
			return;
		}
		accessibleCaisse.AtPos(positionCaisse).Visitite();
		marque.ajouteMarque(positionCaisse.Ligne(), positionCaisse.Colonne(), 0xAAAA22);
		VerifButAccessible(positionCaisse, positionPousseur, 1, 0);
		VerifButAccessible(positionCaisse, positionPousseur, -1, 0);
		VerifButAccessible(positionCaisse, positionPousseur, 0, -1);
		VerifButAccessible(positionCaisse, positionPousseur, 0, 1);
	}

	public void VerifCaisseAccessible(Position position, int dLig, int dCol, int ligneToIgnore, int colonneToIgnore, int ligneToInvent, int colonneToInvent){
		int newLig = position.Ligne()+dLig;
		int newCol = position.Colonne()+dCol;

		Position nextPos = accessiblePousseur.At(newLig, newCol);
		if(((!(newLig == ligneToIgnore && newCol == colonneToIgnore) && niveau.aCaisse(newLig, newCol)) || (newLig == ligneToInvent && newCol == colonneToInvent)) &&
				(niveau.estOccupable(newLig+dLig, newCol+dCol) || niveau.aPousseur(newLig+dLig, newCol+dCol))){
//			marque.ajouteMarque(position.Ligne(), position.Colonne(), 0x2222AA);
			positionDevantCaisse = position;
			caisse = new Position(newLig, newCol, null);
		}
		else if(((newLig == ligneToIgnore && newCol == colonneToIgnore) || niveau.estOccupable(newLig, newCol)) && !(newLig == ligneToInvent && newCol == colonneToInvent)){
			if(nextPos == null) {
				nextPos = new Position(newLig, newCol, position, position.Distance() + 1);
				accessiblePousseur.At(newLig, newCol, nextPos);
				CaisseAccessible(nextPos, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
			}
			else if(nextPos.SetDistanceMin(position)){
				VerifCaisseAccessible(nextPos, 1, 0, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
				VerifCaisseAccessible(nextPos, -1, 0, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
				VerifCaisseAccessible(nextPos, 0, 1, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
				VerifCaisseAccessible(nextPos, 0, -1, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
			}
		}
	}

	public void InitCaisseAccessible(int ligne, int colonne, int ligneToIgnore, int colonneToIgnore, int ligneToInvent, int colonneToInvent){

		accessiblePousseur = new Graphe(niveau.lignes(), niveau.colonnes());
		pousseur = new Position(ligne, colonne, null);
		accessiblePousseur.At(ligne, colonne, pousseur);
		CaisseAccessible(pousseur, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
	}

	public void CaisseAccessible(Position position, int ligneToIgnore, int colonneToIgnore, int ligneToInvent, int colonneToInvent){
		if (accessiblePousseur.AtPos(position).EstVisite()){
			return;
		}
		accessiblePousseur.AtPos(position).Visitite();
//		marque.ajouteMarque(position.Ligne(), position.Colonne(), 0x22AA22);
		VerifCaisseAccessible(position, 1, 0, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
		VerifCaisseAccessible(position, -1, 0, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
		VerifCaisseAccessible(position, 0, -1, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
		VerifCaisseAccessible(position, 0, 1, ligneToIgnore, colonneToIgnore, ligneToInvent, colonneToInvent);
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
	public Sequence<Coup> DeplacerVersButPos(Sequence<Coup> resultat, Position position){
		Position currentPos = position;
		Position ancetre = position.Ancetre();
		Sequence<Coup> resultat_invert = Configuration.nouvelleSequence();
		Coup deplacement;
		while (ancetre != null){
			deplacement = new Coup();

			deplacement.deplacementPousseur(ancetre.Ligne(), ancetre.Colonne(), currentPos.Ligne(), currentPos.Colonne());
			currentPos = ancetre;
			ancetre = currentPos.Ancetre();
//			pousseur = new Position(position.Ligne()+(position.Ligne()-ancetre.Ligne()), position.Colonne()+(position.Colonne()-ancetre.Colonne()), null);
			resultat.insereTete(deplacement);
//			resultat_invert.insereTete(deplacement);
		}
//		int i=0;
//		while(!resultat_invert.estVide()){
//			deplacement = resultat_invert.extraitTete();
//			resultat.insereTete(deplacement);
//			Dijkstra caisseAccesible = new Dijkstra(niveau);
//			System.out.println("Pousseur : " + pousseur);
//			caisseAccesible.InitCaisseAccessible(pousseur.Ligne(), pousseur.Colonne(), currentPos.Ligne(), currentPos.Colonne(), -1, -1);
//			resultat = DeplacerVersPos(resultat, caisseAccesible.accessiblePousseur.At(currentPos.Ligne()-deplacement.dirPousseurL(), currentPos.Colonne()-deplacement.dirPousseurC()));
//			pousseur = new Position(currentPos.Ligne()-deplacement.dirPousseurL(), currentPos.Colonne()-deplacement.dirPousseurC(), null);
//			currentPos = new Position(currentPos.Ligne()+deplacement.dirPousseurL(), currentPos.Colonne()+deplacement.dirPousseurC(), null);
//			System.out.println("PousseurApres : " + pousseur);
//			System.out.println("currentPos : " + currentPos);
////			i++;
//		}

		return resultat;
	}

	public Coup Marque(){
		return marque;
	}
}
