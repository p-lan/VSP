package services.bank;

import java.util.HashMap;
import java.util.Map;

public class Banken {
	
	
	private Map<String,Bank> zumSpielgehoerigeBanken;
	
	private int id=0;
	
	
	public Banken(){
		
		this.zumSpielgehoerigeBanken = new HashMap<String,Bank>();
		
	}
	
	
	
	
	public Map<String,Bank> getAllBanks(){
		
		return zumSpielgehoerigeBanken;
	}
	
	public void createBankForGame(String gameID){
		
		Bank bank = zumSpielgehoerigeBanken.get(gameID);
		if(bank == null) {
			bank = new Bank(id++);
			zumSpielgehoerigeBanken.put(gameID, bank);
		}
		
		
	}
	
	
	
	public void createBank(String gameID, Account player){

		Bank bank = zumSpielgehoerigeBanken.get(gameID);
		if(bank == null) {
			bank = new Bank(gameID);
			zumSpielgehoerigeBanken.put(gameID, bank);
		}
		bank.addPlayer(player);	
	}
	
	public int getKontostand(String gameID, String playerID){
		
		Bank bank = zumSpielgehoerigeBanken.get(gameID);
		
		return bank.getKontostand(playerID);
	}
	

}
