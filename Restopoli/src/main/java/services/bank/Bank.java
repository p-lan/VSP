package services.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	
	private Map<String,Account> zugehoerigeAccounts;
	private int bankID=0;
	
	
	public Bank(int id){
		this.zugehoerigeAccounts = new HashMap<String, Account>();
		this.bankID=id;
	}
	
	
	public getBankID(){
		
	}
	
	
	
	public void addPlayer(Account acc){
		Account p = zugehoerigeAccounts.get(acc.getId());
		if(p == null) {
			this.zugehoerigeAccounts.put(acc.getId(), acc);
		}
	}
	
	public int getKontostand(String playerID){
		
		Account p = zugehoerigeAccounts.get(playerID);
		if(p != null) {
			return p.getSaldo();
		}
				
		return 0;
	}
	
	

}
