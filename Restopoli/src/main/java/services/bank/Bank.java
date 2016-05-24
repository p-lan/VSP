package services.bank;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Bank {
	
	
	private String bankID;
	private Account bankAccount;
	private String zugehörigesGame;
	private Map<Integer,Account> accounts;
	private Map<Integer,Transfer> transfers;
	private Map<Integer,Transaction> transactions;
	
	
	public Bank(String id, String zugehörigesGame){
		this.bankID=id;
		this.bankAccount= new Account(id);
		this.zugehörigesGame=zugehörigesGame;
		
		this.accounts = new HashMap<Integer, Account>();
		this.transfers = new HashMap<Integer, Transfer>();
		this.transactions = new HashMap<Integer, Transaction>();
	}


	public String getBankID() {
		return bankID;
	}
	
	public String getGame() {
		return zugehörigesGame;
	}


	public Account getBankAccount() {
		return bankAccount;
	}

	
	public Account getAccount(int accountID) {
		return accounts.get(accountID);
	}
	
		
	public List<Account> getAccounts() {
		return new ArrayList<>(accounts.values());
	}
	
	
	public Transfer getTransfer(int transferID) {
		return transfers.get(transferID);
	}
	
	
	public List<Transfer> getTransfers() {
		return new ArrayList<>(transfers.values());
	}
	
	
	public Transaction getTransaction(int transactionID) {
		return transactions.get(transactionID);
	}
	

	public List<Transaction> getTransactions() {
		return new ArrayList<>(transactions.values());
	}

	public void addAccount(int accountID, Account newAcc) {
		accounts.put(accountID, newAcc);
	}
	
	public void addTransfer(int transferID, Transfer newTransfer) {
		transfers.put(transferID, newTransfer);		
	}


	public void addTransaction(int transactionID, Transaction newTransaction) {
		transactions.put(transactionID, newTransaction);
	}


	public void delTransaction(int transID) {
		transactions.remove(transID);
	}


	


}
