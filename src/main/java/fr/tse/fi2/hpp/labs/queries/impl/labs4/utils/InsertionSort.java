package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

public class InsertionSort {

	/**
	 * Trie un tableau d'entier en utilisant la méthode du tri par insertion
	 * @param arr
	 * @return
	 */
	public static void doInsertionSort(int[] arr)
	{
		int len = arr.length;
		
		// on parcourt les elements un par un
		for(int pos = 1; pos < len; pos++)
		{
			// on stocke la valeur de la case actuelle
			int cell_value = arr[pos];
			int j = pos;
			
			// on deplace l'element en cours vers la gauche tant que sa valeur est plus
			// grande que la case à sa gauche (et que son indice est positif)
			while(j > 0 && arr[j-1] > cell_value)
			{
				arr[j] = arr[j-1];
				j--;
			}
			
			arr[j] = cell_value;
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
	

	
	public static void main(String[] args) 
	{
		int[] a1 = {1, 12, 3, 7, 8, 2, 4, 3, 3, 5, 2};

		printArray(a1);
		doInsertionSort(a1);
		printArray(a1);

	}
}
