package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

import java.util.Random;

public class MergeSort {

	/**
	 * Trie un tableau en utilsant l'algorithme du merge sort.
	 * @param arr
	 * @return
	 */
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
	
	/**
	 * Fusionne le tableau arr1 avec le tableau arr2, en triant les éléments
	 * @param arr1
	 * @param arr2
	 * @return
	 */
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
	
	/**
	 * Trie le tableau arr avec l'algorithme du merge sort, mais trie les sous-tableaux
	 * avec l'algorithme du tri par insertion si la taille de ceux ci est <= CUTOFF
	 * @param arr
	 * @return
	 */
	public static int[] doMergeSortUpgraded(int[] arr, final int CUTOFF)
	{
		int len = arr.length;
		
		if (len <= CUTOFF)
		{
			return InsertionSort.doInsertionSort(arr);
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
			
			return mergeArrays(doMergeSortUpgraded(leftArr, CUTOFF), doMergeSortUpgraded(rightArr, CUTOFF));
			
		}
	}
	
	/**
	 * Affiche un tableau d'entiers, avec un espace entre chaque entier
	 * @param arr
	 */
	public static void printArray(int[] arr)
	{
		for(int i = 0; i < arr.length; i++)
		{
			System.out.print(arr[i] + " ");
		}
		
		System.out.println();
	}
	
	/**
	 * Genere un tableau de int de taille size dont les valeurs sont entre 0 et 100 000
	 * @param size
	 * @return
	 */
	public static int[] generateRandomArray(int size)
	{
		int[] result = new int[size];
		Random rdm = new Random();
		
		for(int k = 0; k < size; k++)
		{
			 result[k] = rdm.nextInt(100000);
		}
		
		return result;
	}
	
	/**
	 * Indique si un tableau est trié.
	 * @param arr
	 * @return
	 */
	public static boolean isSorted(int[] arr)
	{
		for(int k = 0; k < arr.length - 1; k++)
		{
			if (arr[k] > arr[k+1])
				return false;
		}
		
		return true;
	}
	
	public static void main(String[] args) 
	{
		int[] a1 = {1, 12, 3, 7, 8, 2, 4, 3, 3, 5, 2};
		int[] a2 = generateRandomArray(100);

		printArray(a2);
		printArray(doMergeSortUpgraded(a2, 16));
		System.out.println(isSorted(doMergeSort(a2)));
	
		

	}

}
