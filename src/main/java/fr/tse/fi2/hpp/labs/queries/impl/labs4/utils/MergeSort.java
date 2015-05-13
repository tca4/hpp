package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

public class MergeSort {

	public static int[] doMergeSort(int[] arr)
	{
		int len = arr.length;
		
		if (len <= 1)
		{
			return arr;
		}
		
		else
		{
			int[] leftArr = new int[len/2];
			int[] rightArr = new int[len - len/2];
			
			for(int k = 0; k < len/2; k++)
			{
				leftArr[k] = arr[k];
			}
			
			for(int k = 0; k < len - len/2; k++)
			{
				rightArr[k] = arr[len/2 + k];
			}
			
			return mergeArrays(doMergeSort(leftArr), doMergeSort(rightArr));
			
		}
		
		
	}
	
	public static int[] mergeArrays(int [] arr1, int[] arr2)
	{
		int total = arr1.length + arr2.length;
		int[] result = new int[total];
		
		int i = 0;
		int j = 0;
		
		while( i < arr1.length && j < arr2.length)
		{
			if (arr1[i] <= arr2[j])
			{
				result[i+j] = arr1[i];
				i++;
			}
			else
			{
				result[i+j] = arr2[j];
				j++;
			}
		}
		
		while( i < arr1.length)
		{
			result[i+j] = arr1[i];
			i++;
		}
		
		while( j < arr2.length)
		{
			result[i+j] = arr2[j];
			j++;
		}
		
		return result;
	}
	
	public static void printArray(int[] arr)
	{
		for(int i = 0; i < arr.length; i++)
		{
			System.out.print(arr[i]);
		}
	}
	
	public static void main(String[] args) 
	{
		int[] a1 = {1, 11, 3, 7, 8, 2, 4, 3, 3, 5, 2};

		printArray(doMergeSort(a1));
		

	}

}
