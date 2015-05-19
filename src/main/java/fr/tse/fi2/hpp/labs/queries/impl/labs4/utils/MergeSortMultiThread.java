package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MergeSortMultiThread extends RecursiveTask<int[]> {

	private static final int CUTOFF = 20;
	int[] arr;
	int beginIndex, endIndex;
	
	public MergeSortMultiThread(int[] arr, int begin,  int end) {
		super();
		this.arr = arr;
		this.beginIndex = begin;
		this.endIndex = end;
		
	}

	@Override
	public int[] compute() 
	{
		int len = endIndex - beginIndex;
		
		if (len < CUTOFF) 
		{
			InsertionSort.doInsertionSort(arr);
			return arr;
		} 
		
		else 
		{
			// Subtasks : divise le tableau en deux sous tableaux. Cree une sous tache pour
			// chacun des sous tableaux
			
			int middleIndex = (beginIndex + endIndex) / 2;
			
			// fait le merge sort sur la partie gauche, puis la partie droite
			MergeSortMultiThread subtask1 = new MergeSortMultiThread(arr, beginIndex, middleIndex);
			MergeSortMultiThread subtask2 = new MergeSortMultiThread(arr, middleIndex,   endIndex);

			//Start : lance chaque sous tache
			subtask1.fork();
			subtask2.fork();
			
			//Join :  fusionne les deux parties
			subtask1.join();
			subtask2.join();
			MergeSort.mergeArrays(arr, beginIndex, middleIndex, endIndex);
			return arr;
			
		}

	}
	
	public static void main(String[] args) 
	{
		int[] a1 = {1, 12, 3, 7, 8, 2, 4, 3, 3, 27, 2};
		MergeSortMultiThread m1 = new MergeSortMultiThread(a1, 0, a1.length);
		
		MergeSort.printArray(m1.compute());
	}

}
