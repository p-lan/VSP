package services.bank;

import java.util.ArrayList;
import java.util.List;

import services.bank.Account;

public class Transaction {
	
	public enum Status {
		ready,
		commited,
		deleted;
	}
	
	
	private String id;
	private List<Transfer> transfers;
	private Status status;
	
	
	public Transaction(String id) {
		this.id = id;
		transfers = new ArrayList<>();
		this.status=Status.ready;
	}


	public String getId() {
		return id;
	}


	public List<Transfer> getTransfers() {
		return transfers;
	}


	public Status getStatus() {
		return status;
	}


	public void addTransfer(Transfer newTransfer) {
		transfers.add(newTransfer);		
	}


	public void commit() {
		if(this.status == Status.ready){
			for(Transfer t :this.transfers){
				ausfuehren(t);
			}
			this.status = Status.commited;
		}
	}


	public void rollBack() {
		if(this.status == Status.commited){
			
			for(Transfer t :this.transfers){
				zurueckNehmen(t);
			}
			
		}
		this.status = Status.deleted;
	}
	
	public void ausfuehren(Transfer trans) {
		Account from = trans.getFrom();
		Account to= trans.getTo();
		int amount = trans.getAmount();
		
		int newSaldoFrom = from.getSaldo()-amount;
		int newSaldoTo = to.getSaldo()+amount;
		
		from.setSaldo(newSaldoFrom);
		to.setSaldo(newSaldoTo);
		
		
		
		
	}

	public void zurueckNehmen(Transfer trans) {
		Account from = trans.getFrom();
		Account to= trans.getTo();
		int amount = trans.getAmount();
		
		int newSaldoFrom = from.getSaldo()+amount;
		int newSaldoTo = to.getSaldo()-amount;
		
		from.setSaldo(newSaldoFrom);
		to.setSaldo(newSaldoTo);
	}

	
	
}
