package services.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Banken {
	
	
	private Map<Integer,Bank> banken;
	
	public Banken(){
		this.banken = new HashMap<Integer,Bank>();
		
	}
	
	public List<Bank> getBanken(){		
		return new ArrayList<>(banken.values());
	}
	
	public Bank getBank(int bankID){
		
		return banken.get(bankID);
	}
	
	public void addBank(int bankid,Bank bank){
		banken.put(bankid, bank);
	}
	
		

}
