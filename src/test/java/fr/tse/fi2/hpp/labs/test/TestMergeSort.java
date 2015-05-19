package fr.tse.fi2.hpp.labs.test;

import static org.junit.Assert.*;

import org.junit.Test;
import fr.tse.fi2.hpp.labs.queries.impl.labs4.utils.*;

public class TestMergeSort {

	@Test
	public void test() {
		
		int[] testArray = { 1, 2, 3, 3, 1, 4, 1, 1};
		
		assertTrue(MergeSort.isSorted(MergeSort.doMergeSort(testArray, 0, testArray.length)));
	}
	
	@Test
	public void multipleTests()
	{
		final int[] SIZES = { 100000, 1000000, 10000000 };
		final int NB_TESTS = 30;
		
		for(int k = 0; k < NB_TESTS; k++)
		{
			int[] testArray = MergeSort.generateRandomArray(SIZES[k % 3]);
			assertTrue(MergeSort.isSorted(MergeSort.doMergeSort(testArray, 0, testArray.length)));
			System.out.println("Test " + (k+1) + "/" + NB_TESTS + " -> Done");
			testArray=null;
		}
		
	}
	
	
	

}
