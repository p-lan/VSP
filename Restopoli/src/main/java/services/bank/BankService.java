package services.bank;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import util.Registration;

import java.util.List;

import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import static spark.Spark.*;

/**
 * Created by norman gersdorf on 03.05.16.
 */
public class BankService {

    private static final String NAME = "lmnp_banks";
    private static final String DESCRIPTION = "Bank service";
    private static final String SERVICE = "/banks";
    private static final String URI = "";

    
    private static Banken BANKEN = new Banken();
    private static int BANKENIDS=0;
    private static int ACCOUNTIDS=0;
    private static int TRANSFERIDS=0;
    private static int TRANSAKTIONIDS=0;
    
    
 // --------------------- banken erstellen oder anzeigen
    public static String getBanken(Request req, Response res){
    	
    	JSONArray bankenResourcen = new JSONArray();
		
    	for(Bank bank : BANKEN.getBanken()) {
    		bankenResourcen.put(bank.getBankID());
		}
		
		JSONObject json = new JSONObject();
		json.put("banks", bankenResourcen);
		
		res.status(HttpStatus.OK_200);
		return json.toString();
    }
    
    public static String postBanken(Request req, Response res){
    	
    	JSONObject json = new JSONObject(req.body());
    	String game = json.getString("game");
		
    	String path= req.pathInfo();
    	
    	int bankid=BANKENIDS++;
    	
		Bank bank = new Bank(path+"/"+bankid, game);
		
		BANKEN.addBank(bankid,bank);
		
		
		res.header(HttpHeader.LOCATION.asString(), bank.getBankID());
		res.status(HttpStatus.CREATED_201);
    	return "Bank: "+bank.getBankID()+ " wurde erfolgreich angelegt!";
    }
    
    public static String getBank(Request req, Response res){
    	
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	
    	Bank theBank = BANKEN.getBank(bankid);
        	
    	JSONObject json = new JSONObject();
    	
    	json.put("accounts", theBank.getBankID()+"/accounts");
    	json.put("transfers", theBank.getBankID()+"/transfers");
		
		
    	
    	res.status(HttpStatus.OK_200);
    	return json.toString();
    }
    
    public static String putBank(Request req, Response res){
    	//TODO
    	
    	
		res.status(HttpStatus.BAD_REQUEST_400);
    	return "Not implemented!";
    }
 
    public static String getTransfers(Request req, Response res){
    	
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	Bank theBank = BANKEN.getBank(bankid);
    	JSONArray bankTransfers = new JSONArray();
    	JSONObject json = new JSONObject();
    	
    	for(Transfer t : theBank.getTransfers()) {
    		bankTransfers.put(t.getId());
    	}
    	
    	
    	json.put("transfers", bankTransfers);
    	
    	
    	res.status(HttpStatus.OK_200);
    	return json.toString();
    }
    
    public static String getTransfer(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int transferID = Integer.parseInt(req.params(":transferid"));
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	Transfer trans = theBank.getTransfer(transferID);
    	JSONObject json = new JSONObject();
    	    	
    	json.put("from", trans.getFrom().getAccountID());
    	json.put("to", trans.getTo().getAccountID());
    	json.put("amount", trans.getAmount());
    	json.put("reason", trans.getReason());
    	
    	
    	res.status(HttpStatus.OK_200);
    	return json.toString();
    }
    
    /*
     *  // neuer transfer von account zu einem anderen
       "/banks/:bankid/transfer/from/:from/to/:to/:amount"
     */
    
    public static String postTransferFromUserToUser(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int accountIdFrom = Integer.parseInt(req.params(":from"));
    	int accountIdto = Integer.parseInt(req.params(":to"));
    	int amount = Integer.parseInt(req.params(":amount"));
    	
    	String transaction =req.queryParams("transaction");
    	int transactionID = Integer.parseInt(transaction.substring(transaction.lastIndexOf("/")+1));
    	
    	String reason = req.body();
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	Transaction trans = theBank.getTransaction(transactionID);
    	
    	String path=  theBank.getBankID()+"/transfers/";
    	int transferID = TRANSFERIDS++;
    	
    	
    	Transfer newTransfer = new Transfer(path+transferID,
    										theBank.getAccount(accountIdFrom),
    										theBank.getAccount(accountIdto),
    										amount,
    										reason); 
    	
    	theBank.addTransfer(transferID,newTransfer);
    	trans.addTransfer(newTransfer);
    	
    	
    	res.header(HttpHeader.LOCATION.asString(), newTransfer.getId());
		res.status(HttpStatus.CREATED_201);
    	return "Transfer: "+newTransfer.getId()+ " wurde erfolgreich angelegt!";
    }
    /*
     *  
        // neuer transfer von bank zu
       "/banks/:bankid/transfer/to/:to/:amount"
     */
    public static String postTransferFromBankToUser(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int accountIdto = Integer.parseInt(req.params(":to"));
    	int amount = Integer.parseInt(req.params(":amount"));
    	
    	String transaction =req.queryParams("transaction");
    	int transactionID = Integer.parseInt(transaction.substring(transaction.lastIndexOf("/")+1));
    	
    	String reason = req.body();
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	Transaction trans = theBank.getTransaction(transactionID);
    	
    	String path=  theBank.getBankID()+"/transfers/";
    	int transferID = TRANSFERIDS++;
    	
    	
    	Transfer newTransfer = new Transfer(path+transferID,
    										theBank.getBankAccount(),
    										theBank.getAccount(accountIdto),
    										amount,
    										reason); 
    	
    	theBank.addTransfer(transferID,newTransfer);
    	trans.addTransfer(newTransfer);
    	
    	
    	res.header(HttpHeader.LOCATION.asString(), newTransfer.getId());
		res.status(HttpStatus.CREATED_201);
    	return "Transfer: "+newTransfer.getId()+ " wurde erfolgreich angelegt!";
    }
    
	/*
	 *  // neuer transfer von account zu bank
	    "/banks/:bankid/transfer/from/:from/:amount"
	 */
    public static String postTransferFromUserToBank(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int accountIdFrom = Integer.parseInt(req.params(":from"));
    	int amount = Integer.parseInt(req.params(":amount"));
    	
    	String trans =req.queryParams("transaction");
    	int transactionID = Integer.parseInt(trans.substring(trans.lastIndexOf("/")+1));
    	String reason = req.body();
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	
    	Transaction transaction = theBank.getTransaction(transactionID);
    	
    	
    	String path=  theBank.getBankID()+"/transfers/";
    	int transferID = TRANSFERIDS++;
    	
    	
    	Transfer newTransfer = new Transfer(path+transferID,
    										theBank.getAccount(accountIdFrom),
    										theBank.getBankAccount(),
    										amount,
    										reason); 
    	
    	theBank.addTransfer(transferID,newTransfer);
    	transaction.addTransfer(newTransfer);
    	
    	res.header(HttpHeader.LOCATION.asString(), newTransfer.getId());
		res.status(HttpStatus.CREATED_201);
    	return "Transfer: "+newTransfer.getId()+ " wurde erfolgreich angelegt!";
    }
    /*
     * // startet eine transaction
        post("/banks/:bankid/transaction", BankService::postTransaction);
      */
    public static String postTransaction(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	String path= req.pathInfo();
    	
    	int transactionID= TRANSAKTIONIDS++;
    	
    	Transaction newTransaction = new Transaction(path+"/"+transactionID);
    	
    	
    	BANKEN.getBank(bankid).addTransaction(transactionID,newTransaction);

    	res.header(HttpHeader.LOCATION.asString(), newTransaction.getId());
		res.status(HttpStatus.CREATED_201);
    	return "Transaktion: "+newTransaction.getId()+ " wurde erfolgreich angelegt!";
    }
    
    /*
     *  // transactions status abfragen
        get("/banks/:bankid/transaction/:tid", BankService::getTransaction);
       */
    public static String getTransaction(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int transID = Integer.parseInt(req.params(":tid"));
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	
    	Transaction trans = theBank.getTransaction(transID);
    	JSONObject json = new JSONObject();
    	
    	
    	json.put("status", trans.getStatus());
    	
    	
    	res.status(HttpStatus.OK_200);
    	return json.toString();
    }
    /*
     *  // transaction uebergeben
        put("/banks/:bankid/transaction/:tid", BankService::putTransaction);
     */
    public static String putTransaction(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int transID = Integer.parseInt(req.params(":tid"));
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	
    	Transaction trans = theBank.getTransaction(transID);
    	
    	trans.commit();
 	
    	
		res.status(HttpStatus.ACCEPTED_202);
    	return "Transaktion: "+trans.getId()+ " wurde ausgeführt!";
    	
    }
    /*
     *  // transaction abbrechen -> roll back
        delete("/banks/:bankid/transaction/:tid", BankService::delTransaction);
     */
    public static String delTransaction(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int transID = Integer.parseInt(req.params(":tid"));
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	
    	Transaction trans = theBank.getTransaction(transID);
    	
    	trans.rollBack();
    	
    	theBank.delTransaction(transID);
    	    	
    	res.status(HttpStatus.ACCEPTED_202);
    	return "Transaktion: "+trans.getId()+ " wurde abgebrochen!";
    }
    
    
   
    public static String getAccounts(Request req, Response res){
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	Bank theBank = BANKEN.getBank(bankid);
    	JSONArray bankAccounts = new JSONArray();
    	JSONObject json = new JSONObject();
    	
    	for(Account a : theBank.getAccounts()) {
    		bankAccounts.put(a.getAccountID());
    	}
    	
    	json.put("accounts", bankAccounts);
    	
    	
    	
    	res.status(HttpStatus.OK_200);
    	return json.toString();
    }
    
    public static String postAccounts(Request req, Response res){
    	
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	String path= req.pathInfo();
    	
    	JSONObject json = new JSONObject(req.body());
    	
    	String player = json.getString("player");
    	int saldo = json.getInt("saldo");
    	int accountID= ACCOUNTIDS++;
    	
    	Account newAcc = new Account(path+"/"+accountID,player,saldo);
    	
    	
    	BANKEN.getBank(bankid).addAccount(accountID,newAcc);

    	res.header(HttpHeader.LOCATION.asString(), newAcc.getAccountID());
		res.status(HttpStatus.CREATED_201);
    	return "Account: "+newAcc.getAccountID()+ " wurde erfolgreich angelegt!";
    }
    
   
    public static String getAccountSaldo(Request req, Response res){
    	
    	int bankid = Integer.parseInt(req.params(":bankid"));
    	int accountID = Integer.parseInt(req.params(":accountid"));
    	
    	Bank theBank = BANKEN.getBank(bankid);
    	Account account = theBank.getAccount(accountID);
    	JSONObject json = new JSONObject();
    	
    	
    	json.put("player", account.getUriPlayer());
    	json.put("saldo", account.getSaldo());
    	
    	
    	res.status(HttpStatus.OK_200);
    	return json.toString();
    }
    
    
    private static String isAlive(Request req, Response res){
    	res.status(HttpStatus.OK_200);
    	return "ok";
    }
    
    

    public static void main(String[] args) {
        Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);
        //isAlive
        get("/", BankService::isAlive);
        
        
        // bei transfer wenn keine transaction angegeben 
        //wird einfach eine (temproräre) erstellen und durchführen!
        
        
        
        // banken erstellen oder anzeigen
        get("/banks", BankService::getBanken); 
        post("/banks", BankService::postBanken);
        
        // bank holen und oder bearbeiten
        get("/banks/:bankid", BankService::getBank); 
        put("/banks/:bankid", BankService::putBank);
        
        // transfers einer bank holen
        get("/banks/:bankid/transfers", BankService::getTransfers);
        
        // bestimmtes transfer holen
        get("/banks/:bankid/transfers/:transferid", BankService::getTransfer);
        
        // neuer transfer von account zu einem anderen
        post("/banks/:bankid/transfer/from/:from/to/:to/:amount", BankService::postTransferFromUserToUser);
        
        // neuer transfer von bank zu
        post("/banks/:bankid/transfer/to/:to/:amount", BankService::postTransferFromBankToUser);
        // neuer transfer von account zu bank
        post("/banks/:bankid/transfer/from/:from/:amount", BankService::postTransferFromUserToBank);
        // startet eine transaction
        post("/banks/:bankid/transaction", BankService::postTransaction);
        // transactions status abfragen
        get("/banks/:bankid/transaction/:tid", BankService::getTransaction);
        // transaction uebergeben
        put("/banks/:bankid/transaction/:tid", BankService::putTransaction);
        // transaction abbrechen -> roll back
        delete("/banks/:bankid/transaction/:tid", BankService::delTransaction);
        
        // liste aller accounts        
        get("/banks/:bankid/accounts", BankService::getAccounts);
        // bank account erstellen
        post("/banks/:bankid/accounts", BankService::postAccounts);
        // gibt den saldo eines spielers zurück
        get("/banks/:bankid/accounts/:accountid", BankService::getAccountSaldo);
        
        
    }
}
