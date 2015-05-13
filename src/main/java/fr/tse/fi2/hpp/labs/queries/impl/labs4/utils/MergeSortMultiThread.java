package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class MergeSortMultiThread extends RecursiveTask<int[]> {

	private static final int CUTOFF = 20;
	int[] arr;
	
	public MergeSortMultiThread(int[] arr) {
		super();
		this.arr = new int[arr.length];
		System.arraycopy(arr, 0, this.arr, 0, arr.length);
	}

	@Override
	public int[] compute() 
	{
		int len = this.arr.length;
		
		if (len < CUTOFF) 
		{
			return InsertionSort.doInsertionSort(this.arr);
		} 
		
		else 
		{
			// Subtasks : divise le tableau en deux sous tableaux. Cree une sous tache pour
			// chacun des sous tableaux
			
			int[] leftArr = new int[len/2];
			int[] rightArr = new int[len - len/2];
			
			System.arraycopy(this.arr, 0, leftArr, 0, len/2);
			System.arraycopy(this.arr, len/2, rightArr, 0, len - len/2);
			
			MergeSortMultiThread subtask1 = new MergeSortMultiThread(leftArr);
			MergeSortMultiThread subtask2 = new MergeSortMultiThread(rightArr);

			
			//Start : lance chaque sous tache
			subtask1.fork();
			subtask2.fork();
			
			//Join
			return MergeSort.mergeArrays(subtask1.join(), subtask2.join());
			
		}

	}
	
	public static void main(String[] args) 
	{
		int[] a1 = {1, 12, 3, 7, 8, 2, 4, 3, 3, 5, 2};
		MergeSortMultiThread m1 = new MergeSortMultiThread(a1);
		
		MergeSort.printArray(m1.compute());
	
	}

}
