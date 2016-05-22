package client;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.eclipse.jetty.websocket.api.*;
import org.json.JSONArray;
import org.json.JSONObject;
import spark.Request;
import spark.Response;

import java.util.ArrayList;
import java.util.List;

import static spark.Spark.*;

/**
 * Created by patricklanger on 12.04.16.
 */
public class Clientmanager {

    private static final String NAME = "lmnp_client";
    private static final String DESCRIPTION = "clientService.Clientmanager";
    private static final String SERVICE = "clientService.Clientmanager Service";
    private static final String URI = "/client";

    private static final String YELLOW_PAGES = "http://172.18.0.5:4567";

    private static List<Client> _clientList = new ArrayList<>();
    private static int nextUserId;

    private static List<String> openGames = new ArrayList<>();

    public static void main(String[] args) {

        //Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);

        nextUserId = 0;

        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        webSocket("/user", ClientWebSocketHandler.class);
        init();

//        TODO siehe die beiden folgenden Zeilen
//        get("/", Clientmanager::isAlive);
//        post("/client/event", Clientmanager::postEvent);
        post("/client/turn", Clientmanager::setTurn);
    }

    /**
     * Behindert momentan nocht den Aufruf der website...
     * TODO Funktion muss laufen!!
     * @param req
     * @param res
     * @return
     */
    private static String isAlive(Request req, Response res){
        res.status(200);

        return "ok";
    }

    //-----------------------------------POST EVENT------------------------------

    /**
     *
     * @param request
     * @param response
     * @return
     */
    private static String postEvent(Request request, Response response) {

        return "";
    }

    //-----------------------------------SEND MSG------------------------------

    /**
     * Verschicke Nachrichten ueber den Websocket
     * @param session die Session in der gesendet werden soll
     * @param obj das Objekt kann String oder List<String> sein
     * @param msg Der Key fuer das JSONObjekt das verschickt wird
     */
    static void sendIt(Session session, Object obj, String msg){

        JSONObject json = new JSONObject();

        if (obj instanceof List){
            int i = 0;
            JSONArray arr = new JSONArray();
            for (String s : (List<String>) obj) {
                arr.put(s);
            }
            json.put(msg, arr);
        } else if (obj instanceof String){
            json.put(msg, obj);
        } else {
            json.put("ERROR", obj.getClass().getName() + " is not allowed!");
        }

        try {
            session.getRemote().sendString(String.valueOf(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------SET TURN------------------------------

    /**
     * Spieler bescheid geben dass er an der Reihe ist
     * @param request siehe raml
     * @param response
     * @return ok, was auch immer passiert TODO was muss ich returnen?
     */
    private static String setTurn(Request request, Response response) {

        String player = new JSONObject(request.body()).get("player").toString();
        String[] playersplit = player.split("/");
        String name = playersplit[playersplit.length-1];

        System.out.println("Its your turn, " + getClient(name).get_username());

        sendIt(getClient(name).get_session(), "It's your turn, Baby!", "turn");

        return "OK";
    }

    /**
     * suche mir den passenden Client zum Namen
     * @param name der Name
     * @return der Client
     */
    private static Client getClient (String name){
        for (Client client: _clientList) {
            if (client.get_username().equals(name)){
                return client;
            }
        }
        return null;
    }

    //-----------------------------------ROLL DICE------------------------------

    /**
     * Such die Dice-URI zum game
     * @param gameUri die GameURI
     * @return die DiceURI
     */
    private static String getDice(String gameUri){
        String diceUri = null;

        JSONArray antwort;
        JSONObject antwortElem;

        try {
            antwort = Unirest.get(gameUri).asJson().getBody().getArray();

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                JSONObject components = (JSONObject) antwortElem.get("services");
                diceUri = (String) components.get("dice") + "/dice";
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return diceUri;
    }

    /**
     * Suche den passenden Clienten zu der Session
     * @param session Die Session
     * @return Der Client
     */
    private static Client getClient (Session session){
        for (Client client: _clientList) {
            if (client.get_session().equals(session)){
                return client;
            }
        }
        return null;
    }

    /**
     * Rolle den Dice
     * @param session Die Session von der ein Dice-Wurf angevordert wird
     */
    public static void rollDice(Session session) {

        Client client = getClient(session);
        if (client != null){

            String diceUri = getDice(client.get_gameUrl());
            if (diceUri != null){
                System.out.println(diceUri);
                JSONObject antwort;
                JSONObject antwortElem;

                try {
                    antwort = Unirest.get(diceUri).asJson().getBody().getObject();
                    int number = (Integer) antwort.get("number");
                    System.out.println(client.get_username() + " has a " + number);
                    sendIt(session, number+"", "rolleddice");
                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }
    }

    //-----------------------------------SIGN UP------------------------------

    /**
     * Pruefe ob der Username schon vergeben ist
     * @param name Der zu pruefende Name
     * @return true wenn der Name noch frei ist
     */
    private static boolean usernameIsAvailable(String name){
        boolean isAvailable = true;

        for (Client client: _clientList) {
            if (client.get_username().equals(name)){
                isAvailable = false;
            }
        }

        return isAvailable;
    }

    /**
     * Trage den User fuer ein Game ein
     * @param session Die Session von der der Auftrag kommt
     * @param name Der noch zu pruefende Wunschname des Users
     * @param game Das ausgewaehlte Game des Users
     */
    static void signupUser(Session session, String name, String game){
        if (usernameIsAvailable(name)){
            Client client = new Client(nextUserId++, session, name);
            _clientList.add(client);

            JSONObject player = new JSONObject();
            player.put("user", "/user/"+name);
            player.put("ready", false);
            try {
                HttpResponse<String> res = Unirest.post(game + "/players")
                        .header("Content-Type", "application/json")
                        .body(player)
                        .asString();

                client.set_gameUrl(game);

                sendIt(session, "Hi " + name + ", good Luck for you. I hope you get rich!", "signedup");
            } catch (UnirestException e) {
                e.printStackTrace();

                sendIt(session, "Hi " + name + ", something went wrong : " + e, "notsignedup");
            }
        } else {
            sendIt(session, "Hey Dude, you need a fancy name!", "notsignedup");
        }

    }

    //-----------------------------------GET GAMES------------------------------

    /**
     * Suche die URIs für alle offenen verfuegbaren Games!
     * @param session session des Users
     */
    static void getGames(Session session) {
        JSONArray antwort;
        JSONObject antwortElem;
        List<String> openGameUris = new ArrayList<>();

        //fragt alle Services ab die den angegebenen namen haben
        try {
            antwort = Unirest.get(YELLOW_PAGES + "/services/of/name/lmnp_games").asJson().getBody().getArray();

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                JSONArray serviceIds = (JSONArray) antwortElem.get("services");
                for(Object serviceId : serviceIds){
                    System.out.println("searchGameUrls for: " + serviceId.toString());
                    String gameUri = searchGameUri(serviceId.toString());
                    System.out.println("gameUri: " + gameUri);
                    List<String> games = searchGames(gameUri);
                    for (String game: games) {
                        if (gameIsOpen(game)){
                            openGameUris.add(game);
                        }
                    }
                }
            }
            sendIt(session, openGameUris.toString(), "games");
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    /**
     * Pruef ob das Game der uebergebenen URI noch nicht FINISHED ist!
     * @param game URI fuer das Game
     * @return true wenn Game noch offen
     */
    private static boolean gameIsOpen(String game) {
        boolean gameIsOpen = false;
        JSONArray antwort;
        JSONObject antwortElem;

        try {
            antwort = Unirest.get(game).asJson().getBody().getArray();

            System.out.println(antwort.toString());

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                if(!antwortElem.get("status").toString().matches("FINISHED")){
                    gameIsOpen = true;
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return gameIsOpen;
    }

    /**
     * Suche alle Games in diesem Service
     * @param gameUri URI des Game-Service
     * @return Liste GameIDs
     */
    private static List<String> searchGames(String gameUri) {
        List<String> games = new ArrayList<>();
        JSONArray antwort;
        JSONObject antwortElem;

        try {
            antwort = Unirest.get(gameUri + "/games").asJson().getBody().getArray();

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                JSONArray gamesJson = (JSONArray) antwortElem.get("games");
                for(Object game : gamesJson){
                    games.add(gameUri + game.toString());
                }
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return games;
    }

    /**
     * Suche ServiceURI fuer diese ServiceID
     * @param serviceId ID des Services
     * @return seine URI
     */
    private static String searchGameUri(String serviceId){
        String gameUri = "";
        JSONArray antwort;
        JSONObject antwortElem;

        try {
            antwort = Unirest.get(YELLOW_PAGES + serviceId).asJson().getBody().getArray();

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                gameUri = antwortElem.get("uri").toString();
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return gameUri;
    }
}
