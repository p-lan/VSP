package services.events;

import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;


import spark.Request;
import spark.Response;
import util.Registration;

import java.util.List;


import org.json.JSONObject;

import static spark.Spark.*;

/**
 * Created by patricklanger on 12.04.16.
 */
public class Eventmanager {

    private static final String NAME = "lmnp_events";//"lmnp_events_Norman";
    private static final String DESCRIPTION = "Verwaltet Events";
    private static final String SERVICE = "events";
    private static final String URI = "/events";

    private static final Events EVENTS = new Events();

    private static int id = 0;

    public static String postEvent(Request req, Response res){
    	
        	
    	Event event = new Gson().fromJson(req.body(), Event.class);
    			
        
        System.out.println("eventmanager.postEvent2: "+event.getName());

        if(event.getGame().isEmpty() && event.getType().isEmpty() 
        							&& event.getName().isEmpty() && event.getReason().isEmpty()){
        	res.status(404);
        	return "Eines oder alle ben�tigten Felder (game,type,name oder reason) nicht angegeben!";
        }
        else{
        	event.setID(String.valueOf(id++));
            EVENTS.addEvent(event);

            return "ok";
        }
        
    }

    public static String getEvents(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("request");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");

        System.out.println("game: " + game);
        System.out.println("player: " + player);

        List<Event> passendeEvents = EVENTS.getEvents(game, type, name, reason, resource, player);

        res.status(200);
        return new Gson().toJson(passendeEvents);
    }

    public static String deleteEvent(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("request");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");

        System.out.println("game: " + game);
        System.out.println("player: " + player);

       
        EVENTS.delEvent(game, type, name, reason, resource, player);

        return "ok";
    }

    public static String getEvent(Request req, Response res){
        String eventid = req.params(":eventid");

        Event event = EVENTS.getEvent(eventid);

        return new Gson().toJson(event);
    }
    
    private static String isAlive(Request req, Response res){
    	res.status(200);
    	
    	return "ok";
    }

    public static void main(String[] args) {
        Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);
    	get("/", Eventmanager::isAlive);
    	get("/events", Eventmanager::getEvents);
    	get("/events/:eventid", Eventmanager::getEvent);
    	post("/events", Eventmanager::postEvent);
        delete("/events", Eventmanager::deleteEvent);
        
    }
}
