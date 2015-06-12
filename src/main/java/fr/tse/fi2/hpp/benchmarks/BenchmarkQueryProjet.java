package fr.tse.fi2.hpp.benchmarks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Warmup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import projet.MostCommonRoutes;
import projet.MostProfitableAreas;
import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.beans.measure.QueryProcessorMeasure;
import fr.tse.fi2.hpp.labs.dispatcher.StreamingDispatcher;
import fr.tse.fi2.hpp.labs.main.MainStreaming;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;


@State(Scope.Thread)
@Warmup(iterations = 0)
@Fork(1)
@Measurement(iterations = 1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class BenchmarkQueryProjet {

	final static Logger logger = LoggerFactory.getLogger(MainStreaming.class);
	
	
	@Benchmark
	public void run()
	{
		// Init query time measure
				QueryProcessorMeasure measure = new QueryProcessorMeasure();
				// Init dispatcher
				StreamingDispatcher dispatch = new StreamingDispatcher(
						"src/main/resources/data/sorted_data.csv");

				long start = System.nanoTime();
				// create an instance of the two queries
				MostCommonRoutes query1 = new MostCommonRoutes(measure);
				MostProfitableAreas query2 = new MostProfitableAreas(measure);
				// Query processors
				List<AbstractQueryProcessor> processors = new ArrayList<>();
				// Add you query processor here
				processors.add(query1);
				processors.add(query2);
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
				// Output measure and ratio per query processor
				measure.setProcessedRecords(dispatch.getRecords());
				measure.outputMeasure();
	}
	
	
	
}
