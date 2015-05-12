package fr.tse.fi2.hpp.labs.queries.impl.labs4.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;

public class BloomFilter {

	private String[] salts;
	private byte[] filter;
	private int sizeFilter;
	private int nbHashFunctions;
	
	public BloomFilter(int sizeFilter, int nbHashFunctions) 
	{
		super();
		
		// initialise le tableaux des sels, représentant les K fonctions de hachage différentes
		this.salts = new String[nbHashFunctions];
		for(int i = 0; i < nbHashFunctions; i++)
		{
			salts[i] = Integer.toString(i + 1);
		}
		
		// initialise le filtre avec la taille sizeFilter et le remplit de 0
		this.filter = new byte[sizeFilter];
		Arrays.fill(this.filter, (byte) 0);
		
		// indique la taille du filtre, et le nombre de fonctions de hachage
		this.sizeFilter = sizeFilter;
		this.nbHashFunctions = nbHashFunctions;
		
	}
	
	/**
	 * Ajoute un DebsRecord dans le filtre de Bloom. Hash le record avec l'ensemble des sels différents Pour
	 * chaque résultat p, met la case p à 1.
	 * @param rec
	 */
	public void add(DebsRecord rec)
	{
		for(String salt : this.salts)
		{
			int position = SHA3Util.digest(rec.toString(), salt, 512, sizeFilter);
			System.out.println(position);
			this.filter[position] = 1;
		}
	}
	
	/**
	 * Verifie si rec appartient au bloom filter. Hash rec avec les sels differents. Pour chaqu hash, verifie 
	 * que la case correspondante est à 1. Renvoie false sinon
	 * @param rec
	 * @return
	 */
	public boolean contain(DebsRecord rec)
	{
		for(String salt : this.salts)
		{
			int position = SHA3Util.digest(rec.toString(), salt, 512, sizeFilter);
			if (this.filter[position] != 1)
				{
				return false;
				}
		}
		
		return true;
	}
	
	
	public static void main(String[] args) 
	{
		BloomFilter tmp = new BloomFilter(100, 3);

		
		DebsRecord test = new DebsRecord("07290D3599E7A0D62097A346EFCC1FB5",
										"E7750A37CAB07D0DFF0AF7E3573AC141",
										(long) 0,
										(long) 4,
										(long)120, (float)0.44, (float)-73.956528, (float)40.716976, (float)-73.962440, (float)40.715008,"CSH", (float)3.50, (float)0.50, (float)0.50, (float)0.00 , (float)0.00, (float)4.50, false);
		
		DebsRecord test2 = new DebsRecord("B5",
				"E7750A37CAB07D0DFF0AF7E3573AC141",
				(long) 0,
				(long) 4,
				(long)120, (float)0.44, (float)-73.956528, (float)40.716976, (float)-73.962440, (float)40.715008,"CSH", (float)3.50, (float)0.50, (float)0.50, (float)0.00 , (float)0.00, (float)4.50, false);

		tmp.add(test);
		
		for(byte b : tmp.filter)
		{
			System.out.print(b);
		}
		
		
		System.out.println("\n" + tmp.contain(test));
		System.out.println(tmp.contain(test2));
	
	}

}
