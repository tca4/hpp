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

}
