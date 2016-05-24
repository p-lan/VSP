package services.bank;



public class Account {
	
	private String accountID;
	private String accountURI;
	private String uriPlayer;
	private int saldo=0;
		
	
	public Account(String accountID, String uriPlayer, int saldo){
		this.accountID=accountID;
		this.uriPlayer=uriPlayer;
		this.saldo=saldo;
	}
	
	public Account(String accountID){
		this.accountID=accountID;
		this.uriPlayer="";
		this.saldo=Integer.MAX_VALUE;
	}

	public String getAccountID() {
		return accountID;
	}

	public String getUriPlayer() {
		return uriPlayer;
	}

	public int getSaldo() {
		return saldo;
	}

	public void setSaldo(int saldo) {
		this.saldo = saldo;
	}
	
	
}
