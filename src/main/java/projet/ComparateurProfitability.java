package projet;
import java.util.Comparator;

public class ComparateurProfitability implements Comparator {
	
	public int compare(Object obj1, Object obj2)
	{
		Profitability prob1 = (Profitability) obj1;
		Profitability prob2 = (Profitability) obj2;
		
		int diffProfitability = (int) Math.signum(prob2.computeProfitability() - prob1.computeProfitability());
		
		if (diffProfitability == 0)
		{
			// les profits sont identiques, trie selon la date
			int diffDate = (int) (prob2.getFreshestElement() - prob1.getFreshestElement());
			if (diffDate == 0)
			{
				return prob2.getIndex() - prob1.getIndex();
			}
			
			else
			{
				return diffDate;
			}
		}
		else
		{
			return diffProfitability;
		}
		
	}

}

