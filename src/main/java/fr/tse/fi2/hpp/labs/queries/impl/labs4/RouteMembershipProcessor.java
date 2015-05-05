package fr.tse.fi2.hpp.labs.queries.impl.labs4;

import java.util.ArrayList;
import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

@State(Scope.Benchmark)
@Fork(1)
@Measurement(iterations = 1)
@Warmup(iterations = 1)
public class RouteMembershipProcessor extends AbstractQueryProcessor {

	private List<DebsRecord> allRecords; 
			
	public RouteMembershipProcessor(QueryProcessorMeasure measure) {
		super(measure);
		allRecords = new ArrayList<DebsRecord>();
	}

	@Override
	protected double process(DebsRecord record) {
		
		allRecords.add(record);
//		System.out.println(record.getHack_license());
		return 0;
	}
	

	
	
	
	public boolean isIn(DebsRecord recordToCheck)
	{
		for(DebsRecord rec : allRecords)
			{
				if (recordToCheck.getPickup_longitude() == rec.getPickup_longitude()
						&& recordToCheck.getPickup_latitude() == rec.getPickup_latitude()
						&& recordToCheck.getDropoff_longitude() == rec.getDropoff_longitude()
						&& recordToCheck.getDropoff_latitude() == rec.getDropoff_latitude()
						&& recordToCheck.getHack_license().equals(rec.getHack_license()) )
						{
							return true;
						}
			}
		
		return false;
	}

}
