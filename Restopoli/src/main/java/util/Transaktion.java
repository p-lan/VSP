package util;

import services.bank.Account;

public class Transaktion {

	private Account player;
	private int amount;
	
	public Transaktion(Account player, int amount) {
		this.player = player;
		this.amount = amount;
	}
	
	public Account getAccount() {
		return this.player;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	
}
