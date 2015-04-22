package fr.tse.fi2.hpp.labs.queries.impl;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class IncrementalAverage extends AbstractQueryProcessor {

	private int nb = 0;
	private float sum = 0;
	private IncrementalAverage writeProcess;

	public IncrementalAverage(QueryProcessorMeasure measure) {
		super(measure);
		
		
	}

	@Override
	protected double process(DebsRecord record) {
		nb++;
		sum += record.getFare_amount();
		
		return(sum/nb);
		//writeLine("current mean : " + (sum / nb));
	}

}