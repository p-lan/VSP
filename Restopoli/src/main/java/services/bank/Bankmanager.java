package services.bank;

import java.util.HashMap;
import java.util.Map;

public class Bankmanager {
	
	
	private Map<String,Bank> zumSpielgehoerigeBanken;
	
	
	
	public Bankmanager(){
		
		this.zumSpielgehoerigeBanken = new HashMap<String,Bank>();
		
	}
	
	public boolean createBank(String gameID, Bank bank){
		
		//TODO check auf schon vorhanden
		this.zumSpielgehoerigeBanken.put(gameID, bank);
		
		return true;
		
	}
	
	
	

}
