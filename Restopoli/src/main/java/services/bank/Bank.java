package services.bank;

import java.util.HashMap;
import java.util.Map;

public class Bank {
	
	
	private Map<String,Player> zugehoerigeSpieler;
	
	
	public Bank(){
		this.zugehoerigeSpieler = new HashMap<String,Player>();
	}
	
	
	public boolean addPlayer(String id, Player player){
		
		//TODO check ob spieler schon vorhanden
		
		this.zugehoerigeSpieler.put(id, player);
		
		return true;
	}
	
	

}
