package services.bank;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import util.Registration;

import java.util.List;

import static spark.Spark.*;

/**
 * Created by norman gersdorf on 03.05.16.
 */
public class BankService {

    private static final String NAME = "lmnp_banks";
    private static final String DESCRIPTION = "Bank service";
    private static final String SERVICE = "bank";
    private static final String URI = "/banks";

    
    private static Banken bankmanager = new Banken();

    
 
    public static String getBanken(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postBanken(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String getBank(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postBank(Request req, Response res){
    	//TODO
    	return "ok";
    }
 
    public static String getTransfers(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String getTransfer(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postTransferFromUserToUser(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postTransferFromBankToUser(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postTransferFromUserToBank(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postTransaction(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String getTransaction(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String putTransaction(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String delTransaction(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String getAccounts(Request req, Response res){
    	//TODO
    	return "ok";
    }
    
    public static String postAccounts(Request req, Response res){
    	String gameID = req.params(":bankid");
    	String playerID = req.params("playerid");
    	String playerName = req.params("name");
    	int playerSaldo = 0;
    	
    	    	
    	bankmanager.createBank(gameID, new Account(playerID,playerName,playerSaldo));
    	res.status(200);
    	return "ok";
    }
    
    public static String getAccountSaldo(Request req, Response res){
    	String gameID = req.params(":bankid");
    	String playerID = req.params(":accountid");
        
    	
    	bankmanager.getKontostand(gameID, playerID);
    	
    	//TODO
    	
    	
    	return "ok";
    }
    
    
    private static String isAlive(Request req, Response res){
    	res.status(200);
    	return "ok";
    }
    
    

    public static void main(String[] args) {
        Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);
        //isAlive
        get("/", BankService::isAlive);
        
        // banken erstellen oder anzeigen
        get("/banks", BankService::getBanken); 
        post("/banks", BankService::postBanken);
        
        // bank holen und oder bearbeiten
        get("/banks/:bankid", BankService::getBank); 
        put("/banks/:bankid", BankService::postBank);
        
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
