package services.events;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import util.Registration;

import java.util.List;

import static spark.Spark.*;

/**
 * Created by patricklanger on 12.04.16.
 */
public class Eventmanager {

    private static final String NAME = "lmnp_events_Norman";
    private static final String DESCRIPTION = "Service.Events verwaltet Events";
    private static final String SERVICE = "eventsService.Eventmanager Service";
    private static final String URI = "http://abs775_events:4567/";

    private static final Events EVENTS = new Events();

    private static int id = 0;

    public static String postEvent(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("reason");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");
        String time = req.queryParams("time");

        Event event = new Event(String.valueOf(id++), game, type, name, reason, resource, player, time);
        EVENTS.addEvent(event);

        return "ok";
    }

    public static String getEvents(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("reason");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");

        List<Event> passendeEvents = EVENTS.getEvents(game, type, name, reason, resource, player);

        res.status(200);
        return new Gson().toJson(passendeEvents);
    }

    public static String deleteEvent(Request req, Response res){
    	String game = req.queryParams("game");
        String type = req.queryParams("type");
        String name = req.queryParams("name");
        String reason  = req.queryParams("reason");
        String resource = req.queryParams("resource");
        String player = req.queryParams("player");

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
        //Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);
    	get("/", Eventmanager::isAlive);
    	post("/events", Eventmanager::postEvent);
        get("/events", Eventmanager::getEvents);
        delete("/events", Eventmanager::deleteEvent);
        get("/events/:eventid", Eventmanager::getEvent);
    }
}
