package projet;

import java.util.ArrayList;
import java.util.Iterator;

import org.apache.commons.math3.util.Pair;


/**
 * This classe is composed of :
 * - a list of earning to calculate the median
 * - a list of empty taxis in this area
 * - the date of the most recent item
 * @author tca
 *
 */
public class Profitability 
{
	ListeMediane earnings;
	ArrayList<Pair<String, Long>> allEmptyTaxis;
	long freshestElement;
	
	
	
	public Profitability()
	{
		earnings = new ListeMediane();
		allEmptyTaxis = new ArrayList<Pair<String, Long>>();
	}
	
	
	public double computeProfitability()
	{
		if (allEmptyTaxis.size() == 0)
		{
			return -1f;
		}
		
		else
		{
			return earnings.getMediane() / allEmptyTaxis.size();
		}
	}
	
	
	public void ajouteTaxiVide(String nomTaxi, long time)
	{
		Pair<String, Long> tmp = new Pair<String, Long>(nomTaxi, time);
		allEmptyTaxis.add(tmp);
	}
	
	
	public void supprimeTaxiVide(String nomTaxi, long time)
	{
		int i = 0;
		
		if (allEmptyTaxis.size() != 0)
		{
			for(Pair<String, Long> tmp : allEmptyTaxis)
			{
				
				if (tmp.getKey().equals(nomTaxi))
				{
					if (tmp.getSecond() < time)
					{
						break;
					}
					else
					{
						return;
					}
				}
				i++;
			}
			System.out.println("i : " + i + "\tSize :" + allEmptyTaxis.size());
			allEmptyTaxis.remove(i);
				
		}
	}
}
