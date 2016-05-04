package services.bank;

public class Player {
	
	
	private String id="";
	private String name= "";
	private int kontostand=0;
	
	
	
	public Player(String id, String name, int kontostand){
		this.id=id;
		this.name=name;
		this.kontostand=kontostand;
	}



	public String getId() {
		return this.id;
	}



	public String getName() {
		return this.name;
	}



	public int getKontostand() {
		return this.kontostand;
	}


}
