package fr.tse.fi2.hpp.labs.queries.impl.benchmarks;

import static org.junit.Assert.assertTrue;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Warmup;

import fr.tse.fi2.hpp.labs.queries.impl.labs4.utils.MergeSort;

@State(Scope.Thread)
@Warmup(iterations = 1)
@Fork(1)
@Measurement(iterations = 1)
public class BenchmarkMergeSort 
{
	@Benchmark
	public void doBenchmark()
	{
		
		int[] testArray = MergeSort.generateRandomArray(1000000);
		MergeSort.isSorted(MergeSort.doMergeSort(testArray));
		
	}
}
