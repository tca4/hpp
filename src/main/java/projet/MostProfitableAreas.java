package projet;

import java.util.HashMap;
import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MostProfitableAreas extends AbstractQueryProcessor 
{
	
	long currentTime;
	final long MINUTES_FENETRES_GAIN = 15 * 60 * 1000;
	LinkedList<DebsRecord> fenetre15min;
	HashMap<String, ListeMediane> profitArea;
	

	public MostProfitableAreas(QueryProcessorMeasure measure) 
	{
		super(measure);
		fenetre15min = new LinkedList<DebsRecord>();
		profitArea  = new HashMap<String, ListeMediane>();
		
	}

	@Override
	protected double process(DebsRecord record) {
		currentTime = record.getDropoff_datetime();
		
		// informations concernant le trajet
		String caseDepart  = convertToCell(record.getPickup_latitude(), record.getPickup_longitude());
		float fare = record.getFare_amount();
		float tip  = record.getTip_amount();
		
		// si la case n'est pas dans la grille, aucun calcul n'est effectue
		if (caseDepart.equals(""))
		{
			return 0;
		}
		
		
		// Ajout ou mise à jour du profit de la case dans la hashmap
		// on ne doit prendre en compte que les trajets sans erreurs 
		// (donc fare et tip doivent etre positifs)
		if (fare >= 0 && tip >= 0)
		{
			// Ajout de la course dans la fenetre des 15 min
			fenetre15min.add(record);
			
			if (profitArea.containsKey(caseDepart))
			{
				profitArea.get(caseDepart).ajouteGain(fare + tip);
			}
			
			else
			{
				ListeMediane tmp = new ListeMediane();
				tmp.ajouteGain(fare + tip);
				profitArea.put(caseDepart, tmp);
			}
		}
		
		
		// On supprime les gains des courses qui datent de plus de 15 min
		removeEarnings15min(currentTime - MINUTES_FENETRES_GAIN);
		
		for (String cell : profitArea.keySet())
		{
			System.out.println(profitArea.get(cell).values.size());
			String profit = String.valueOf(profitArea.get(cell).getMediane());
			System.out.println("Area : " + cell + "\tProfit : " + profit);
		}
		
		System.out.println("-------------");
		return 0;
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
				profitArea.get(cell).supprimeGain(elem.getFare_amount() + elem.getTip_amount());
				
				// supprime de la fenetre
				fenetre15min.removeFirst();
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

}
