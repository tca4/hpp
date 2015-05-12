package fr.tse.fi2.hpp.labs.queries.impl.labs4;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.labs4.utils.BloomFilter;


public class RouteMembershipProcessor extends AbstractQueryProcessor {

	private BloomFilter filtre; 
			
	public RouteMembershipProcessor(QueryProcessorMeasure measure) 
	{
		super(measure);
		this.filtre = new BloomFilter(28700000, 9);
	}

	@Override
	protected double process(DebsRecord record) {
		
		filtre.add(record);
//		System.out.println(record.getHack_license() + " -> Done");
		return 0;
	}
	
	public boolean isIn(DebsRecord recordToCheck)
	{
		return filtre.contain(recordToCheck);
	}

}
