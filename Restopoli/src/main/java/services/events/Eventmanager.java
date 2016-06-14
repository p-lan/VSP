package services.events;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

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
 * Created by patricklanger on 12.04.16.
 */
public class Eventmanager {

    private static final String NAME = "lmnp_events";//"lmnp_events_Norman";
    private static final String DESCRIPTION = "Verwaltet Events";
    private static final String SERVICE = "events";
    private static final String URI = "";

    private static final Events EVENTS = new Events();

    private static int eventIDS = 0;
    private static int subscriptionIDS=0;

    public static String postEvent(Request req, Response res){
    	
        	
    	Event event = new Gson().fromJson(req.body(), Event.class);
    			
    	// abfrage auf wichtige felder
        if(!event.getGame().isEmpty() && !event.getType().isEmpty() 
        							&& !event.getName().isEmpty() && !event.getReason().isEmpty()){
        	
        	event.setID(req.pathInfo()+"/"+String.valueOf(eventIDS++));
        	
            EVENTS.addEvent(event);
            
            // subscripe
            
            List<Subscription> allSubs = EVENTS.getSubscriptions();
            
            for(Subscription sub : allSubs){
            	//regex test
            	if(sub.getReqexEvent().match(event)){
            		//Startet neuen thread für subscripe
            		new Thread(() -> {
		            		try {
								Unirest.post(sub.getInterrestetUri())
								.header(HttpHeader.CONTENT_TYPE.asString(), "appliction/json")
								.body(new Gson().toJson(event))
								.asString();
							} catch (UnirestException e) {
								// wenn post fehlschlägt wird subscription gelöscht
								EVENTS.delSubscription(sub.getIntID());
								System.out.println("Tote Subscription gelöscht:" + sub.getId());
							}
            		}).start();
            	}
            }
            res.header(HttpHeader.LOCATION.asString(), event.getId());
            res.status(HttpStatus.CREATED_201);
        	return "Event "+event.getId() +" wurde angelegt!";
        }
        else{
        	
        	res.status(HttpStatus.NOT_FOUND_404);
        	return "Eines oder alle benoetigten Felder (game,type,name oder reason) nicht angegeben!";
        }
        
    }

    public static String getEvents(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("request");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");

        List<Event> passendeEvents = EVENTS.getEvents(game, type, name, reason, resource, player);

        res.status(HttpStatus.OK_200);
        return new Gson().toJson(passendeEvents);
    }

    public static String deleteEvent(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("request");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");

        EVENTS.delEvent(game, type, name, reason, resource, player);

        res.status(HttpStatus.ACCEPTED_202);
    	return "Events wurden gelöscht!";
    }

    public static String getEvent(Request req, Response res){
        String eventid = req.pathInfo();
  
        Event event = EVENTS.getEvent(eventid);
        
        
        res.status(HttpStatus.OK_200);
        return new Gson().toJson(event);
    }
    
    //##################### Subscription #####################
    
    public static String getSubscriptions(Request req, Response res){
    	JSONArray subscriptions = new JSONArray();
		
    	for(Subscription sub : EVENTS.getSubscriptions()) {
    		subscriptions.put(sub.getId());
		}
		
		JSONObject json = new JSONObject();
		json.put("subscriptions", subscriptions);
		
		res.status(HttpStatus.OK_200);
		return json.toString();
    }
    
    public static String postSubscription(Request req, Response res){
		 
    	int subID = subscriptionIDS++;
    	JSONObject json = new JSONObject(req.body());
		String game = json.getString("game");
		String interrestetUri = json.getString("uri");
		
		Event regexEvent = new Gson().fromJson(json.getJSONObject("event").toString(), Event.class);
		regexEvent.setID(req.pathInfo()+"/"+String.valueOf(eventIDS++));
		
		/*
		JSONObject jsonEvent = json.getJSONObject("event");
		String eventID = req.pathInfo()+"/"+String.valueOf(eventIDS++);
		String eventGame = json.getString("game");
		String eventType = json.getString("type");
		String eventName = json.getString("name");
		String eventReason = json.getString("reason");
		String eventResource = json.getString("resource");
		String eventPlayer = json.getString("player");
		String eventTime = "";
				 
		Event regexEvent = new Event(eventID , eventGame , eventType , eventName , 
									eventReason, eventResource , eventPlayer , eventTime);
		
		*/
		
		Subscription sub = new Subscription(subID,req.pathInfo()+"/"+String.valueOf(subID), 
											game, interrestetUri, regexEvent);
		
		EVENTS.addSubscription(subID, sub);
    	
		res.header(HttpHeader.LOCATION.asString(), sub.getId());
		res.status(HttpStatus.CREATED_201);
    	return "Subscription "+sub.getId() +" wurde angelegt!";
    	
    }
    
    public static String deleteSubscription(Request req, Response res){
    	
    	int subscriptionID = Integer.parseInt(req.params(":bankid"));
    	
    	Subscription sub = EVENTS.getSubscription(subscriptionID);
    	
    	EVENTS.delSubscription(subscriptionID);
    	
    	
    	res.status(HttpStatus.ACCEPTED_202);
    	return "Subscription: "+sub.getId()+ " wurde gelöscht!";
    	
    }
    
    private static String isAlive(Request req, Response res){
    	res.status(HttpStatus.OK_200);
    	return "ok";
    }

    public static void main(String[] args) {
    	//exception handling über Spark exception handling
    	
        Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);
    	get("/", Eventmanager::isAlive);
    	get("/events", Eventmanager::getEvents);
    	get("/events/:eventid", Eventmanager::getEvent);
    	post("/events", Eventmanager::postEvent);
        delete("/events", Eventmanager::deleteEvent);
        //subcriptions
        get("/events/subscriptions", Eventmanager::getSubscriptions);
        post("/events/subscriptions", Eventmanager::postSubscription);
        delete("/events/subscriptions/subscriptions/subscriptionid", Eventmanager::deleteSubscription);
        
    }
}
