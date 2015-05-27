package projet;

import java.util.LinkedList;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MostCommonRoutes extends AbstractQueryProcessor {

	long currentTime;
	final long MINUTES_FENETRES = 30 * 60 * 1000;
	LinkedList<DebsRecord> fenetre30min;
	
	public MostCommonRoutes(QueryProcessorMeasure measure) {
		super(measure);
		currentTime = 0;
		fenetre30min = new LinkedList<DebsRecord>();
	}

	@Override
	protected double process(DebsRecord record) {
		currentTime = record.getDropoff_datetime();
		fenetre30min.add(record);
		deleteOutsideWindow(currentTime - MINUTES_FENETRES);
		System.out.println(this.cellX(record.getPickup_longitude()));
		System.out.println(convertToCell(record.getPickup_latitude(),  -73.716385));
//		System.out.println(this.cellY(record.getPickup_latitude()));
//		
		System.out.println(fenetre30min.getFirst().getDropoff_datetime() - currentTime);
		
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
	private int convertToCell(float latitude, double longitude)
	{
		// latitude = ordonnee; longitude = abscisse
		
		// longueur et largeur de chaque case
		double step_abscisse = 0.005986;
		double step_ordonnee = 0.004491556;
		
		// origine de la grille
		double starting_abscisse = -74.913585 - step_abscisse/2;
		double starting_ordonnee = 41.474937 - step_ordonnee/2;
		
		// nombre de cases separant le point en parametre de l'origine de la grille
		Double cellX = (longitude - starting_abscisse) / step_abscisse;
		Double cellY = (latitude  - starting_ordonnee) / step_ordonnee;
		
		// on ne garde que la partie entiere du nombre de cases
		int result = (cellX.intValue() + 1) * 1000 + (cellY.intValue() + 1);
		return result;
		
		
	}

}
