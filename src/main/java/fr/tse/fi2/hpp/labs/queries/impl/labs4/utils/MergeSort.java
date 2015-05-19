package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

import java.util.Random;

public class MergeSort {

	/**
	 * Trie un tableau en utilisant l'algorithme du merge sort.
	 * @param arr
	 * @return
	 */
	public static int[] doMergeSort(int[] arr, int beginIndex, int endIndex)
	{
		int len = endIndex - beginIndex;
		
		if (len <= 1)
		{
			return arr;
		}
		
		else
		{
			int middleIndex = (beginIndex + endIndex) / 2;
			
			// fait le merge sort sur la partie gauche, puis la partie droite
			doMergeSort(arr, beginIndex, middleIndex);
			doMergeSort(arr, middleIndex,   endIndex);
			// fusionne les deux parties
			mergeArrays(arr, beginIndex, middleIndex, endIndex);
			
			return arr;
			
		}
		
		
	}
	
	/**
	 * Fusionne la partie gauche de arr avec la partie droite de arr dans un tableau annexe.
	 * Copie ce tableau annexe dans arr 
	 * @param arr
	 * @param beginIndex
	 * @param middleIndex
	 * @param endIndex
	 * @return
	 */
	public static void mergeArrays(int [] arr, int beginIndex, int middleIndex, int endIndex)
	{
		
		int len = endIndex - beginIndex;
		int[] tmp = new int[len];
		
		int i = beginIndex;
		int j = middleIndex;
		
		
		for(int pos = beginIndex; pos < endIndex; pos++)
		{
			if ( i < middleIndex && (j >= endIndex || arr[i] <= arr[j]))
			{
				tmp[pos - beginIndex] = arr[i];
				
				i++;
			}
			else
			{
				tmp[pos - beginIndex] = arr[j];
				j++;
			}
		}
		
		// copie le tableau temporaire dans arr
		System.arraycopy(tmp, 0, arr, beginIndex, len);
	}
	
	/**
	 * Trie le tableau arr avec l'algorithme du merge sort, mais trie les sous-tableaux
	 * avec l'algorithme du tri par insertion si la taille de ceux ci est <= CUTOFF
	 * @param arr
	 * @return
	 */
	public static int[] doMergeSortUpgraded(int[] arr, final int CUTOFF, int beginIndex, int endIndex)
	{
		int len = endIndex - beginIndex;
		
		if (len <= CUTOFF)
		{
			InsertionSort.doInsertionSort(arr);
			return arr;
		}
		
		else
		{
			
			int middleIndex = (beginIndex + endIndex) / 2;
			
			// fait le merge sort sur la partie gauche, puis la partie droite
			doMergeSortUpgraded(arr, CUTOFF, beginIndex, middleIndex);
			doMergeSortUpgraded(arr, CUTOFF, middleIndex,   endIndex);
			
			// fusionne les deux parties
			mergeArrays(arr, beginIndex, middleIndex, endIndex);
			
			return arr;
			
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
		int[] a1 = {1, 12, 3, 2};
		int[] a2 = generateRandomArray(100);

		printArray(a2);
		printArray(doMergeSortUpgraded(a2, 16, 0, a2.length));
		printArray(a2);
		System.out.println(isSorted(a2));

	}
}
