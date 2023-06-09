package Utile;

import Global.Configuration;
import Modele.Coup;
import Modele.Niveau;
import Structures.Graphe;
import Structures.Position;
import Structures.Sequence;

public class Dijkstra {

	public Niveau niveau;
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
			marque.ajouteMarque(newLig, newCol, 0x11CCCC);
			positionBut = new Position(newLig, newCol, positionCaisse);
		}
		else if((niveau.estOccupable(newLig, newCol) || niveau.aPousseur(newLig, newCol)) && (accessiblePousseur.At(befLig, befCol) != null && accessiblePousseur.At(befLig, befCol).EstVisite())){
			if(nextPos == null) {
				nextPos = new Position(newLig, newCol, positionCaisse, positionCaisse.Distance() + 1);
//				nextPos.chemin = DeplacerVersPos(Configuration.nouvelleSequence(), accessiblePousseur.At(befLig, befCol));

				accessibleCaisse.At(newLig, newCol, nextPos);
				ButAccessible(nextPos, nextPosPousseur);
			}
			else if(nextPos.SetDistanceMin(positionCaisse)){
//				nextPos.chemin = DeplacerVersPos(Configuration.nouvelleSequence(), accessiblePousseur.At(befLig, befCol));
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
		newCaisse.SetDistance(0);
		accessibleCaisse.At(ligne, colonne, newCaisse);
		pousseur = new Position(lignePousseur, colonnePousseur, null);
//		accessibleCaisse.At(lignePousseur, colonnePousseur, pousseur);
		ButAccessible(newCaisse, pousseur);
	}

	public void ButAccessible(Position positionCaisse, Position positionPousseur){
		if (accessibleCaisse.AtPos(positionCaisse).EstVisite()){
			return;
		}
		accessibleCaisse.AtPos(positionCaisse).Visitite();
		marque.ajouteMarque(positionCaisse.Ligne(), positionCaisse.Colonne(), 0xCCCC22);
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
		else if(((newLig == ligneToIgnore && newCol == colonneToIgnore) || niveau.estOccupable(newLig, newCol) || niveau.aPousseur(newLig, newCol)) && !(newLig == ligneToInvent && newCol == colonneToInvent)){
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
		pousseur.SetDistance(0);
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
		Position ancien;
		Coup marque = new Coup();

		while (ancetre != null){
			deplacement = new Coup();

			deplacement.deplacementPousseur(ancetre.Ligne(), ancetre.Colonne(), currentPos.Ligne(), currentPos.Colonne());
			marque.ajouteMarque(ancetre.Ligne(), ancetre.Colonne(), 0xCC8811);
			currentPos = ancetre;
			ancetre = currentPos.Ancetre();
//			resultat.insereTete(deplacement);
			resultat_invert.insereTete(deplacement);
		}

		pousseur = new Position(niveau.lignePousseur(), niveau.colonnePousseur(), null);
		System.out.println(pousseur);
		System.out.println(currentPos);

		Coup deplacement2 = new Coup();
		while(!resultat_invert.estVide()){
			deplacement = resultat_invert.extraitTete();
			ancien = pousseur;
			pousseur = new Position(currentPos.Ligne()-deplacement.dirPousseurL(), currentPos.Colonne()-deplacement.dirPousseurC(), null);
//			System.out.println("Ancien" + ancien);
//			System.out.println("Pousseur" + pousseur);
//			System.out.println("Caisse" + currentPos);
			deplacement2 = new Coup();
			deplacement2.deplacementPousseur(ancien.Ligne(), ancien.Colonne(), pousseur.Ligne(), pousseur.Colonne());
			resultat.insereQueue(deplacement2);
			resultat.insereQueue(deplacement);
			currentPos = new Position(currentPos.Ligne()+deplacement.dirPousseurL(), currentPos.Colonne()+deplacement.dirPousseurC(), null);
		}
		resultat.insereTete(marque);
		return resultat;
	}

	public Coup Marque(){
		return marque;
	}
}
