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

public class MostCommonRoutes extends AbstractQueryProcessor {

	long currentTime;
	final long MINUTES_FENETRES = 30 * 60 * 1000;
	LinkedList<DebsRecord> fenetre30min;
	HashMap<String, frequenceRoute> compteurRoute;
	ArrayList<frequenceRoute> array10most;
	int globalIndex;
	String classement;
	long delayStart;
	
	
	public MostCommonRoutes(QueryProcessorMeasure measure) {
		super(measure);
		currentTime = 0;
		fenetre30min = new LinkedList<DebsRecord>();
		compteurRoute = new HashMap<String, frequenceRoute>(10000);
		array10most = new ArrayList<frequenceRoute>();
		globalIndex = 0;
		classement = "";
		delayStart = 0;
	}

	
	@Override
	protected String process(DebsRecord record) {
		currentTime = record.getDropoff_datetime();
		delayStart = System.nanoTime();
		
		// get the route of the record
		String key = convertToRoute(record);
		
		if (!key.equals(""))
		{

		// ajout du debsRecord dans la fenetre
		fenetre30min.add(record);
		
		
		// incremente le nombre de passage de la route. Si elle n'a jamais ete rencontree, on la cree
		if (compteurRoute.containsKey(key))
		{
			compteurRoute.get(key).incrementeCompteur();
			compteurRoute.get(key).setFreshest_element(record.getDropoff_datetime());
		}
		else
		{
			frequenceRoute tmp = new frequenceRoute(1, record.getDropoff_datetime(), key, globalIndex);
			compteurRoute.put(key, tmp);
		}
		
		// supprime les debsrecord qui ne sont plus dans la fenetre des 30 min, ainsi que
		// les routes associees dans la Hashmap, ainsi que la route si elle est dans le tableau de 10 routes les plus frequentes
		deleteOutsideWindow(currentTime - MINUTES_FENETRES);
		
		
		// ajoute la route actuellement traitee dans le tableau des 10 plus frequentes si 
		// elle est meilleure que celle en derniere position ou que le tableau a moins de 10 cases
		if (array10most.size() < 10 || compteurRoute.get(key).getCompteur() >= array10most.get(array10most.size() -1).getCompteur())
		{
			array10most.add(compteurRoute.get(key));
		}
			
			
		// ordonne les 10 routes les plus frequentes et les affiche si il y eu un changement
//		System.out.println("---------------");
		String result = sortRoutes(record);
		
		// incremente le nombre de routes que l'on a traitee
		globalIndex += 1;
//		System.out.println(globalIndex);
		
		return result;
		
		}
		
		return "";
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
			// on supprime le premier element de la liste tant qu'il est en dehors de la fenetre
			// les elements sont classes par temps arrive croissant
			if (fenetre30min.getFirst().getDropoff_datetime() < time)
			{
				// decremente le compteur de la route dans la hashmap
				String key = convertToRoute(fenetre30min.getFirst());
				if  (compteurRoute.get(key).getCompteur() >= 1)
				{
					compteurRoute.get(key).decrementeCompteur();
					decrementeOccurenceRoute(key);
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
	 * Decremente l'occurence de la route en paramètre seulement si elle se trouve
	 * dans le tableau des 10 routes les plus frequentes. Cette route ne peut se 
	 * trouver qu'une seule fois dans le tableau, c'est pourquoi la fonction termine 
	 * lorsque l'on a decrementé.
	 * @param route
	 */
	void decrementeOccurenceRoute(String route)
	{
		for(frequenceRoute elem : array10most)
		{
			if (elem.getRoute().equals(route))
			{
				elem.decrementeCompteur();
				return;
			}
		}
	}
	
	/**
	 * Retourne la case a laquelle appartient la coordonnee (latitude,longitude).
	 * Renvoie "" si la case n'est pas dans la grille
	 * @param latitude
	 * @param longitude
	 * @return La case sous la forme d'un entier. Les coordonnees de la case sont comprises entre
	 * 1 et 300. On retourne (cellX.cellY)
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
		
		if (X < 1 || X > 300 || Y < 1 || Y > 300)
		{
			return "";
		}
		
		String result = X.toString() + "." + Y.toString();
		return result;
		
	}
	
	
	/**
	 * Renvoie la route du debsRecord sous forme de String. On retourne (caseDeDepart;caseDArrivee)
	 * Renvoie "" si une des cases depasse de la grille
	 * @param record
	 * @return
	 */
	private String convertToRoute(DebsRecord record)
	{
		String caseDepart  = convertToCell(record.getPickup_latitude(),  record.getPickup_longitude());
		String caseArrivee = convertToCell(record.getDropoff_latitude(), record.getDropoff_longitude());
		
		if (caseDepart.equals("") || caseArrivee.equals(""))
		{
			return "";
		}
		
		return caseDepart + "," + caseArrivee + ",";
	}
	
	
	/**
	 * Trie les routes pour avoir les 10 routes les plus frequentes. Affiche les changements s'il y en a
	 */
	private String sortRoutes(DebsRecord record)
	{
		// trie les elements
		Collections.sort(array10most, new comparateurFrequenceRoute());
		
		
		// supprime le dernier si la taille est sup a 10.
		if (array10most.size() > 10)
		{
			array10most.remove(array10most.size() -1);
		}
		
		// verifie s'il y a eu un changement
		String tmp = "";
		for(int i = 0; i < Math.min(10, array10most.size()); ++i)
		{
			tmp += array10most.get(i).getRoute();
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
