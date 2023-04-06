package Modele;
/*
 * Sokoban - Encore une nouvelle version (à but pédagogique) du célèbre jeu
 * Copyright (C) 2018 Guillaume Huard
 *
 * Ce programme est libre, vous pouvez le redistribuer et/ou le
 * modifier selon les termes de la Licence Publique Générale GNU publiée par la
 * Free Software Foundation (version 2 ou bien toute autre version ultérieure
 * choisie par vous).
 *
 * Ce programme est distribué car potentiellement utile, mais SANS
 * AUCUNE GARANTIE, ni explicite ni implicite, y compris les garanties de
 * commercialisation ou d'adaptation dans un but spécifique. Reportez-vous à la
 * Licence Publique Générale GNU pour plus de détails.
 *
 * Vous devez avoir reçu une copie de la Licence Publique Générale
 * GNU en même temps que ce programme ; si ce n'est pas le cas, écrivez à la Free
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307,
 * États-Unis.
 *
 * Contact:
 *          Guillaume.Huard@imag.fr
 *          Laboratoire LIG
 *          700 avenue centrale
 *          Domaine universitaire
 *          38401 Saint Martin d'Hères
 */

import Global.Configuration;
import Structures.Position;
import Structures.Sequence;
import Utile.Dijkstra;

class IAAssistance extends IA {

	Position pousseur;

	public IAAssistance() {
	}

	public Coup SupprimerMarques(){
		Coup coup = new Coup();
		for (int l = 0; l < niveau.lignes(); l++) {
			for (int c = 0; c < niveau.colonnes(); c++) {
//				int marque = niveau.marque(l, c);
				coup.ajouteMarque(l, c, 0);
			}
		}
		return coup;
	}

	@Override
	public Sequence<Coup> joue() {
		Sequence<Coup> resultat = Configuration.nouvelleSequence();

		Dijkstra caisseAccesible = new Dijkstra(niveau);


		caisseAccesible.InitCaisseAccessible(niveau.lignePousseur(), niveau.colonnePousseur(), -1, -1, -1, -1);
		resultat.insereTete(caisseAccesible.Marque());

		Dijkstra butAccessible = new Dijkstra(niveau);
		butAccessible.InitButAccessible(caisseAccesible.caisse.Ligne(), caisseAccesible.caisse.Colonne(), niveau.lignePousseur(), niveau.colonnePousseur());


		if (butAccessible.positionBut != null){
			resultat = butAccessible.DeplacerVersButPos(resultat, butAccessible.positionBut);
		}else{
			System.out.println("Pas de solution...");
		}
		resultat.insereTete(butAccessible.Marque());

//		niveau = butAccessible.niveau;

//		resultat.insereQueue(SupprimerMarques());
		return resultat;
	}

}
