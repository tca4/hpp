package projet;

import java.util.ArrayList;
import java.util.Collections;

public class ListeMediane 
{

	ArrayList<Float> values;
	
	
	public ListeMediane() 
	{
		values = new ArrayList<Float>();
	}

	
	public void ajouteGain(float f)
	{
		values.add(f);
	}
	
	
	public void supprimeGain(float f)
	{
		values.remove(f);
	}
	
	/**
	 * Renvoie la mediane de la suite de nombre dans values. Si il n'y a aucun nombre, renvoie 0.
	 * @return
	 */
	public float getMediane()
	{
		if (values.size() == 0)
		{
			return 0;	
		}
		
		Collections.sort(values);
		
		int middle = values.size() / 2;
		if (values.size() % 2 == 1)
		{
			return values.get(middle);
		}
		else
		{
			return (values.get(middle-1) + values.get(middle)) / 2;
		}
	}
	
	
	public static void main(String[] args) 
	{
		ListeMediane tmp = new ListeMediane();
		
		tmp.ajouteGain(3f);
		tmp.ajouteGain(13f);
		tmp.ajouteGain(7f);
		tmp.ajouteGain(5f);
		tmp.ajouteGain(21f);
		tmp.ajouteGain(23f);
		tmp.ajouteGain(23f);
		tmp.ajouteGain(40f);
		tmp.ajouteGain(23f);
		tmp.ajouteGain(14f);
		tmp.ajouteGain(12f);
		tmp.ajouteGain(56f);
		tmp.ajouteGain(23f);
		tmp.ajouteGain(29f);
		
		System.out.println(tmp.getMediane());

	}

}
