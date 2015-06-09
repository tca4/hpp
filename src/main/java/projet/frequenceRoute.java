package projet;

public class frequenceRoute {
	
	int compteur;
	long freshest_element;
	String route;
	int index;
	
	
	public int getCompteur() {
		return compteur;
	}


	public void setCompteur(int compteur) {
		this.compteur = compteur;
	}


	public long getFreshest_element() {
		return freshest_element;
	}


	public void setFreshest_element(long freshest_element) {
		this.freshest_element = freshest_element;
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
	
	


	public frequenceRoute(int compteur, long freshest_element, String route, int index) {
		super();
		this.compteur = compteur;
		this.freshest_element = freshest_element;
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
