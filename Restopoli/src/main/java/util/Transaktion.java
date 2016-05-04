package util;

import services.bank.Player;

public class Transaktion {

	private Player player;
	private int amount;
	
	public Transaktion(Player player, int amount) {
		this.player = player;
		this.amount = amount;
	}
	
	public Player getAccount() {
		return this.player;
	}
	
	public int getAmount() {
		return this.amount;
	}
	
	
}
