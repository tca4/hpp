package projet;

public class frequenceRoute {
	
	int compteur;
	long freshesht_element;
	String route;
	int index;
	
	
	public int getCompteur() {
		return compteur;
	}


	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}


	public long getFreshesht_element() {
		return freshesht_element;
	}


	public void setFreshesht_element(long freshesht_element) {
		this.freshesht_element = freshesht_element;
	}


	public String getRoute() {
		return route;
	}


	public void setRoute(String route) {
		this.route = route;
	}
	
	public int getIndex() {
		return index;
	}


	public void setIndex(int index) {
		this.index = index;
	}
	
	


	public frequenceRoute(int compteur, long freshesht_element, String route, int index) {
		super();
		this.compteur = compteur;
		this.freshesht_element = freshesht_element;
		this.route = route;
		this.index = index;
	}
	
	public void incrementeCompteur()
	{
		this.compteur += 1;
	}
	
	public void decrementeCompteur()
	{
		this.compteur -= 1;
	}
	

}
