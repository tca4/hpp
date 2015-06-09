package projet;

import java.util.Comparator;

public class comparateurFrequenceRoute implements Comparator {
	
	public int compare(Object obj1, Object obj2)
	{
		frequenceRoute freq1 = (frequenceRoute) obj1;
		frequenceRoute freq2 = (frequenceRoute) obj2;
		
		int diffCompteur = freq2.getCompteur() - freq1.getCompteur();
		
		if (diffCompteur == 0)
		{
			// les frequence sont identiques, trie selon la date
			int diffDate = (int) (freq2.getFreshest_element() - freq1.getFreshest_element());
			if (diffDate == 0)
			{
				return freq2.getIndex() - freq1.getIndex();
			}
			
			else
			{
				return diffDate;
			}
		}
		else
		{
			return diffCompteur;
		}
		
	}

}
