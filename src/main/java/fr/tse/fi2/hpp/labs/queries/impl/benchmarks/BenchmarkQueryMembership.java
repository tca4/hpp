package fr.tse.fi2.hpp.labs.queries.impl.benchmarks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.main.MainStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;
import fr.tse.fi2.hpp.labs.queries.impl.labs4.RouteMembershipProcessor;

@State(Scope.Thread)
@Warmup(iterations = 1)
@Fork(1)
@Measurement(iterations = 1)
public class BenchmarkQueryMembership {

	final static Logger logger = LoggerFactory.getLogger(MainStreaming.class);
	RouteMembershipProcessor q = null;
	
	public BenchmarkQueryMembership()
	{
		
	}
	
	
	@Setup
	public void init()
	{
		// Init query time measure
				QueryProcessorMeasure measure = new QueryProcessorMeasure();
				// Init dispatcher
				StreamingDispatcher dispatch = new StreamingDispatcher(
						"src/main/resources/data/sorted_data.csv");

				
				// create an instance of the query RouteMembership
				q = new RouteMembershipProcessor(measure);
				// Query processors
				List<AbstractQueryProcessor> processors = new ArrayList<>();
				// Add you query processor here
				processors.add(q);
				// Register query processors
				for (AbstractQueryProcessor queryProcessor : processors) {
					dispatch.registerQueryProcessor(queryProcessor);
				}
				// Initialize the latch with the number of query processors
				CountDownLatch latch = new CountDownLatch(processors.size());
				// Set the latch for every processor
				for (AbstractQueryProcessor queryProcessor : processors) {
					queryProcessor.setLatch(latch);
				}
				// Start everything
				for (AbstractQueryProcessor queryProcessor : processors) {
					// queryProcessor.run();
					Thread t = new Thread(queryProcessor);
					t.setName("QP" + queryProcessor.getId());
					t.start();
				}
				Thread t1 = new Thread(dispatch);
				t1.setName("Dispatcher");
				t1.start();

				// Wait for the latch
				try {
					latch.await();
				} catch (InterruptedException e) {
					logger.error("Error while waiting for the program to end", e);
				}
	}
	
	@Benchmark
	public int doBenchmark()
	{
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date_pickup = null;
		Date  date_dropoff = null;
		try {
			date_pickup = (Date)dateFormat.parse("2013-01-01 00:00:00");
			date_dropoff = (Date)dateFormat.parse("2013-01-01 00:02:00");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		DebsRecord test = new DebsRecord("07290D3599E7A0D62097A346EFCC1FB5",
										"E7750A37CAB07D0DFF0AF7E3573AC141",
										date_pickup.getTime(),
										date_dropoff.getTime(),
										(long)120, (float)0.44, (float)-73.956528, (float)40.716976, (float)-73.962440, (float)40.715008,"CSH", (float)3.50, (float)0.50, (float)0.50, (float)0.00 , (float)0.00, (float)4.50, false);
		
		System.out.println(q.isIn(test));
		return 0;
		
	}
}