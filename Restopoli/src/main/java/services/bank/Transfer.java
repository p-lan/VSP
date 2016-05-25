package services.bank;

import services.bank.Account;

public class Transfer {
	
	
	private String id;
	private Account from;
	private Account to;
	private int amount;
	private String reason;
	
	public Transfer(String id, Account from, Account to, int amount, String reason) {
		this.id = id;
		this.from = from;
		this.to = to;
		this.amount = amount;
		this.reason = reason;
	}

	public String getId() {
		return id;
	}

	public Account getFrom() {
		return from;
	}

	public Account getTo() {
		return to;
	}

	public int getAmount() {
		return amount;
	}

	public String getReason() {
		return reason;
	}	
	
}
