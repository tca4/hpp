package fr.tse.fi2.hpp.benchmarks;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.queries.impl.labs4.utils.MergeSort;
import fr.tse.fi2.hpp.labs.queries.impl.labs4.utils.MergeSortMultiThread;

@State(Scope.Thread)
@Warmup(iterations = 3)
@Fork(1)
@Measurement(iterations = 5)
public class BenchmarkMergeSort 
{
	@Benchmark
	@BenchmarkMode({/*Mode.Throughput,*/ Mode.AverageTime/*, Mode.SampleTime*/})
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void doBenchmark()
	{
		
		int[] testArray = MergeSort.generateRandomArray(10000000);
		//MergeSort.doMergeSortUpgraded(testArray, 32);
		MergeSortMultiThread m = new MergeSortMultiThread(testArray);
		m.compute();
	}
}
