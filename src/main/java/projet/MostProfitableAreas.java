package projet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MostProfitableAreas extends AbstractQueryProcessor 
{
	
	long currentTime;
	int globalIndex;
	final long MINUTES_FENETRES_GAIN = 15 * 60 * 1000;
	LinkedList<DebsRecord> fenetre15min;
	LinkedList<DebsRecord> fenetre30min;
	String classement;
	long delayStart;
	
	// case + profit de la case
	HashMap<String, Profitability> profitArea;
	// medaillon du taxi + derniere case de dropoff
	HashMap<String, String> lastPositionOfTaxi;
	
	ArrayList<Profitability> array10most;
	

	public MostProfitableAreas(QueryProcessorMeasure measure) 
	{
		super(measure);
		fenetre15min = new LinkedList<DebsRecord>();
		fenetre30min = new LinkedList<DebsRecord>();
		profitArea  = new HashMap<String, Profitability>();
		lastPositionOfTaxi = new HashMap<String, String>();
		array10most = new ArrayList<Profitability>();
		globalIndex = 0;
	}

	@Override
	protected String process(DebsRecord record) {
		currentTime = record.getDropoff_datetime();
		
		// informations concernant le trajet
		String caseDepart   = convertToCell(record.getPickup_latitude(), record.getPickup_longitude());
		String caseArrivee  = convertToCell(record.getDropoff_latitude(), record.getDropoff_longitude());		
		float fare = record.getFare_amount();
		float tip  = record.getTip_amount();
		String medaillon = record.getMedallion();
		
		// si la case n'est pas dans la grille, aucun calcul n'est effectue
		if (caseDepart.equals(""))
		{
			return "";
		}
		
		
		// Ajout ou mise à jour du profit de la case dans la hashmap
		// on ne doit prendre en compte que les trajets sans erreurs 
		// (donc fare et tip doivent etre positifs)
		if (fare >= 0 && tip >= 0)
		{
			// Ajout de la course dans la fenetre des 15 min
			fenetre15min.add(record);
			
			// mise a jour du gain si la case a deja ete rencontree
			if (profitArea.containsKey(caseDepart))
			{
				// le profit de la case a augmente. Sa position dans le tableau des 10 cases
				// les plus profitables peut changer. On l'enleve du tableau, puis on le remet 
				// apres avoir modifie le gain
				array10most.remove(profitArea.get(caseDepart));
				profitArea.get(caseDepart).earnings.ajouteGain(fare + tip);
				profitArea.get(caseDepart).setFreshestElement(currentTime);
				array10most.add(profitArea.get(caseDepart));		
			}
			
			// sinon, on cree la case et sa profitabilite
			else
			{
				Profitability tmp = new Profitability(globalIndex, currentTime, caseDepart);
				tmp.earnings.ajouteGain(fare + tip);
				profitArea.put(caseDepart, tmp);
				array10most.add(profitArea.get(caseDepart));
			}		
		}
		
		// On supprime les gains des courses qui datent de plus de 15 min
		removeEarnings15min(currentTime - MINUTES_FENETRES_GAIN);
		
		// La course doit etre placee dans la fenetre des 30 min
		fenetre30min.add(record);
		
		// On regarde si ce taxi etait vide. On regarde sa derniere position, 
		// et on verfie s'il appartient a la liste des taxis vides de cette position.
		// Dans ce cas, on l'enleve de la liste des taxis vides de la case
		if (lastPositionOfTaxi.containsKey(medaillon))
		{
			// le nombre de taxi a diminue, le profit de cette case peut augmenter 
			// et changer sa position dans le tableau des 10 meilleurs area
			String dernierePosition = lastPositionOfTaxi.get(medaillon);
			array10most.remove(profitArea.get(dernierePosition));
			
			profitArea.get(dernierePosition).supprimeTaxiVide(medaillon, currentTime - 2 * MINUTES_FENETRES_GAIN);
			array10most.add(profitArea.get(dernierePosition));
		}
		
		// On met a jour la derniere position de ce taxi
		lastPositionOfTaxi.put(record.getMedallion(), caseArrivee);
		
		// Ce taxi est desormais vide. On l'ajoute a la liste des taxis vides de la case
		if (profitArea.containsKey(caseArrivee))
		{
			array10most.remove(profitArea.get(caseArrivee));
			profitArea.get(caseArrivee).ajouteTaxiVide(medaillon, currentTime - 2 * MINUTES_FENETRES_GAIN);
			
			// le nombre de taxi a augmente, le profit de cette peut diminuer 
			// et changer sa position dans le tableau des 10 meilleurs area
			array10most.add(profitArea.get(caseArrivee));
		}
		else
		{
			array10most.remove(profitArea.get(caseArrivee));
			Profitability tmp = new Profitability(globalIndex, currentTime, caseDepart);
			tmp.ajouteTaxiVide(medaillon, currentTime - 2 * MINUTES_FENETRES_GAIN);
			profitArea.put(caseArrivee, tmp);
			
			array10most.add(profitArea.get(caseArrivee));
		}
		
		// Supprime les taxis qui sont vides depuis plus de 30 min
		removeEmptyTaxis30min(currentTime - 2 * MINUTES_FENETRES_GAIN);
		
//		
//		for (String cell : profitArea.keySet())
//		{
//			System.out.println(profitArea.get(cell).earnings.values.size());
//			String profit = String.valueOf(profitArea.get(cell).earnings.getMediane());
//			System.out.println("Area : " + cell + "\tProfit : " + profit + "\t" + profitArea.get(cell).allEmptyTaxis.size() + "\t" + profitArea.get(cell).computeProfitability());
//		}
		
//		System.out.println("-------------");
		String result = sortAreas(record);
//		System.out.println(result);
		globalIndex += 1;
//		System.out.println(globalIndex + ";" + fenetre30min.size());
		return result;
	}
	
	
	private void removeEarnings15min(long time)
	{
		while (true)
		{
			DebsRecord elem = fenetre15min.getFirst();
			
			// on enleve le gain des courses qui sont en dehors de la fenetre de 15 min
			// et on enleve ces debsRecord de la fenetre
			if (elem.getDropoff_datetime() < time)
			{
				// trouve la case de depart de cette course
				String cell = convertToCell(elem.getPickup_latitude(), elem.getPickup_longitude());
				
				// on est sur que le gain peut etre enleve car si le debsRecord est dans la 
				// fenetre, alors on a ajoute son gain
				profitArea.get(cell).earnings.supprimeGain(elem.getFare_amount() + elem.getTip_amount());
				
				// supprime de la fenetre
				fenetre15min.removeFirst();
			}
			else 
			{
				return;
			}
		}
	}
	
	
	private void removeEmptyTaxis30min(long time)
	{
		while (true)
		{
			DebsRecord elem = fenetre30min.getFirst();
			
			// on enleve le taxi de la case de sa derniere position s'il est vide 
			// depuis plus de 30 min
			if (elem.getDropoff_datetime() < time)
			{
				// trouve la derniere position de ce taxi
				String dernierePositon = lastPositionOfTaxi.get(elem.getMedallion());
				if (profitArea.containsKey(dernierePositon))
				{
					profitArea.get(dernierePositon).supprimeTaxiVide(elem.getMedallion(), time);
				}
				
				lastPositionOfTaxi.remove(elem.getMedallion());
				
				// supprime de la fenetre
				fenetre30min.removeFirst();
			}
			else 
			{
				return;
			}
		}
	}
	/**
	 * Retourne la case a laquelle appartient la coordonnee (latitude,longitude).
	 * Renvoie "" si la case n'est pas dans la grille
	 * @param latitude
	 * @param longitude
	 * @return La case sous la forme d'un String. Les coordonnees de la case sont comprises entre
	 * 1 et 600. On retourne (cellX.cellY)
	 */
	private String convertToCell(float latitude, float longitude)
	{
		// latitude = ordonnee; longitude = abscisse
		
		// longueur et largeur de chaque case
		double step_abscisse = 0.005986 / 2;
		double step_ordonnee = 0.004491556 / 2;
		
		// origine de la grille
		double starting_abscisse = -74.913585;
		double starting_ordonnee = 41.474937;
		
		// nombre de cases separant le point en parametre de l'origine de la grille
		double cellX = (longitude - starting_abscisse) / step_abscisse;
		double cellY = (starting_ordonnee - latitude ) / step_ordonnee;
		
		// on ne garde que la partie entiere du nombre de cases
		Integer X = (int) (Math.round(cellX) + 1);
		Integer Y = (int) (Math.round(cellY) + 1);
		
		if (X < 1 || X > 600 || Y < 1 || Y > 600)
		{
			return "";
		}
		
		String result = X.toString() + "." + Y.toString();
		return result;
		
	}

	/**
	 * Trie les routes pour avoir les 10 routes les plus frequentes. Affiche les changements s'il y en a
	 */
	private String sortAreas(DebsRecord record)
	{
		// trie les elements
		Collections.sort(array10most, new ComparateurProfitability());
		
		
		// supprime les dernier elements si la taille est sup a 10.
		for(int i = 10; i < Math.max(10, array10most.size()); ++i)
		{
			array10most.remove(i);
		}
		
		// verifie s'il y a eu un changement
		String tmp = "";
		for(int i = 0; i < Math.min(10, array10most.size()); ++i)
		{
			tmp += array10most.get(i).getCell() + ",";
			tmp += array10most.get(i).getNbEmptyTaxis() + ",";
			tmp += array10most.get(i).earnings.getMediane() + ",";
			tmp += array10most.get(i).computeProfitability() + ",";
			
		}
		
		for(int i = Math.min(10, array10most.size()); i < 10; ++i)
		{
			tmp += "NULL,";
		}
		
		if (!tmp.equals(classement))
		{
			classement = tmp;
			String delay = String.valueOf(System.nanoTime() - delayStart);
			String result = formateDate(record.getPickup_datetime()) +"," 
				     + formateDate(record.getDropoff_datetime()) + "," 
			         + classement + delay;
//			System.out.println(result);
			return result;
		}
		
		return "";
	}
	
	private String formateDate(long time)
	{
		Date date = new Date(time);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String res = df2.format(date);
		return res;
	}
}
