package services.bank;

import com.google.gson.Gson;

import services.events.Eventmanager;
import spark.Request;
import spark.Response;
import util.Registration;

import java.util.List;

import static spark.Spark.*;

/**
 * Created by norman gersdorf on 03.05.16.
 */
public class BankService {

    private static final String NAME = "lmnp";
    private static final String DESCRIPTION = "Bank service";
    private static final String SERVICE = "bank";
    private static final String URI = "/banks";

   

    public static String kontoErstellen(Request req, Response res){
    	String gameID = req.params(":gameid");
      
    	return "ok";
    }

    public static String kontostandAbfragen(Request req, Response res){
    	String gameID = req.params(":gameid");
    	String playerID = req.params(":playerid");
        
    	return "ok";
    }
    
    public static String geldUeberweisen(Request req, Response res){
    	String gameID = req.params(":gameid");
    	String to = req.params(":to");
    	String amount = req.params(":amount");
    	
        return "ok";
    }
    
    public static String geldEinzug(Request req, Response res){
    	String gameID = req.params(":gameid");
    	String from = req.params(":from");
    	String amount = req.params(":amount");
    	    	
        return "ok";
    }
    
    public static String geldUebertragen(Request req, Response res){
    	String gameID = req.params(":gameid");
    	String from = req.params(":from");
    	String to = req.params(":to");
    	String amount = req.params(":amount");
    	
        return "ok";
    }
    
    private static String isAlive(Request req, Response res){
    	res.status(200);
    	return "ok";
    }
    
    

    public static void main(String[] args) {
        Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);
        
        get("/", BankService::isAlive);
        
        post("/banks/:gameid/players", BankService::kontoErstellen);
        get("/banks/:gameid/players/:playerid", BankService::kontostandAbfragen);
        post("/banks/:gameid/transfer/to/:to/:amount", BankService::geldUeberweisen);
        post("/banks/:gameid/transfer/from/:from/:amount", BankService::geldEinzug);
        post("/banks/:gameid/transfer/from/:from/to/:to/:amount", BankService::geldUebertragen);
    }
}
