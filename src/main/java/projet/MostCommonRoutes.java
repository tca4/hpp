package projet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MostCommonRoutes extends AbstractQueryProcessor {

	long currentTime;
	final long MINUTES_FENETRES = 30 * 60 * 1000;
	LinkedList<DebsRecord> fenetre30min;
	HashMap<String, frequenceRoute> compteurRoute;
	int globalIndex;
	
	public MostCommonRoutes(QueryProcessorMeasure measure) {
		super(measure);
		currentTime = 0;
		fenetre30min = new LinkedList<DebsRecord>();
		compteurRoute = new HashMap<String, frequenceRoute>(10000);
		globalIndex = 0;
	}

	
	@Override
	protected double process(DebsRecord record) {
		currentTime = record.getDropoff_datetime();
		
		// ajout du debsRecord dans la fenetre
		fenetre30min.add(record);
		
		// incremente la route du debsRecord dans la HashMap
		String key = convertToRoute(record);
		//System.out.println(key);
		if (compteurRoute.containsKey(key))
		{
			compteurRoute.get(key).incrementeCompteur();
			compteurRoute.get(key).setFreshesht_element(record.getDropoff_datetime());
		}
		else
		{
			frequenceRoute tmp = new frequenceRoute(1, record.getDropoff_datetime(), key, globalIndex);
			compteurRoute.put(key, tmp);
		}
		
		// supprime les debsrecord qui ne sont plus dans la fenetre des 30 min, ainsi que
		// les routes associees dans la Hashmap
		deleteOutsideWindow(currentTime - MINUTES_FENETRES);
		
		// ordonne les routes de la HashMap et affiche les 10 premieres
		System.out.println("---------------");
		sortHashMap();
		
		
//		System.out.println(fenetre30min.getFirst().getDropoff_datetime() - currentTime);
		globalIndex += 1;
		return 0;
	}
	
	
	/**
	 * Supprime les elements en debut de liste qui ne sont plus dans la fenetre de 30 min, c'est-à-dire ceux dont le temps
	 * est inferieur à time.
	 * @param time
	 */
	private void deleteOutsideWindow(long time)
	{
		
		while (true)
		{
			if (fenetre30min.getFirst().getDropoff_datetime() < time)
			{
				// decrementer le compteur de la route dans la hashmap
				String key = convertToRoute(fenetre30min.getFirst());
				if  (compteurRoute.get(key).getCompteur() >= 1)
				{
					compteurRoute.get(key).decrementeCompteur();
				}
				else
				{
					compteurRoute.remove(key);
				}
				
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
	 * @param latitude
	 * @param longitude
	 * @return La case sous la forme d'un entier. Les coordonnees de la case sont comprises entre
	 * 1 et 300. On retourne (cellX * 1000 + cellY)
	 */
	private String convertToCell(float latitude, float longitude)
	{
		// latitude = ordonnee; longitude = abscisse
		
		// longueur et largeur de chaque case
		double step_abscisse = 0.005986;
		double step_ordonnee = 0.004491556;
		
		// origine de la grille
		double starting_abscisse = -74.913585;
		double starting_ordonnee = 41.474937;
		
		// nombre de cases separant le point en parametre de l'origine de la grille
		double cellX = (longitude - starting_abscisse) / step_abscisse;
		double cellY = (starting_ordonnee - latitude ) / step_ordonnee;
		
		// on ne garde que la partie entiere du nombre de cases
		Integer X = (int) (Math.round(cellX) + 1);
		Integer Y = (int) (Math.round(cellY) + 1);
		String result = X.toString() + "." + Y.toString();
		return result;
		
	}
	
	
	/**
	 * Renvoie la route du debsRecord sous forme de String
	 * @param record
	 * @return
	 */
	private String convertToRoute(DebsRecord record)
	{
		String route = convertToCell(record.getPickup_latitude(),  record.getPickup_longitude()).toString() + ";" 
				+ convertToCell(record.getDropoff_latitude(),  record.getDropoff_longitude()).toString();
		return route;
	}
	
	private void sortHashMap()
	{
		ArrayList<frequenceRoute> tableau = new ArrayList<frequenceRoute>();
		
		for(String route : compteurRoute.keySet())
		{
			tableau.add(compteurRoute.get(route));
		}
		
		Collections.sort(tableau, new comparateurFrequenceRoute());
		
		for(frequenceRoute elem : tableau)
		{
			System.out.println(elem.getRoute() + " " + elem.getCompteur() + " " + elem.getFreshesht_element() + " " + elem.getIndex());
		}
	}

	
}
