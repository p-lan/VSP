package client;

import com.google.common.net.InetAddresses;
import com.google.gson.Gson;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.sun.jdi.Value;
import com.sun.tools.doclets.formats.html.SourceToHTMLConverter;
import org.apache.tools.ant.Project;
import org.eclipse.jetty.websocket.api.*;
import org.json.JSONArray;
import org.json.JSONObject;
import services.events.Event;
import spark.Request;
import spark.Response;
import util.Registration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static spark.Spark.*;

/**
 * Created by patricklanger on 12.04.16.
 */
public class Clientmanager {

    private static final String NAME = "lmnp_client";
    private static final String DESCRIPTION = "clientService.Clientmanager";
    private static final String SERVICE = "/client";
    private static final String URI = "/client";

    private static final String YELLOW_PAGES = "http://172.18.0.5:4567";

    private static List<Client> _clientList = new ArrayList<>();
    private static int nextUserId;

    public static void main(String[] args) {

        Registration.registriereService(NAME, DESCRIPTION, SERVICE, URI);

        nextUserId = 0;

        staticFileLocation("/public"); //index.html is served at localhost:4567 (default port)
        webSocket("/user", ClientWebSocketHandler.class);
        init();

        get("/client", Clientmanager::getInfo);
        post("/client/event", Clientmanager::postEvent);
        post("/client/turn", Clientmanager::setTurn);
    }

    private static String getInfo(Request request, Response response) {
        JSONObject json = new JSONObject();
        JSONArray clients = new JSONArray();
        clients.put(new JSONObject().put("events", "/client/event"));
        clients.put(new JSONObject().put("turn", "/client/turn"));
        json.put("components", clients);

        response.body(json.toString());

        return json.toString();
    }

    //-----------------------------------POST EVENT------------------------------

    /**
     *
     * @param request
     * @param response
     * @return
     */
    private static String postEvent(Request request, Response response) {

        System.out.println("Got a new event!");

        Event event = new Gson().fromJson(request.body(), Event.class);
        System.out.println(event.getGame() + " , " + event.getPlayer());

        for (Client c : _clientList){
            if (Pattern.matches(c.get_boardId(), event.getGame())){
                sendIt(c.get_session(), event, "event");
                if (Pattern.matches(event.getType(), "dice")){
                    if (Pattern.matches(c.get_pawnId(), event.getPlayer())){
                        //TODO geworfene Zahl statt id versenden
                        sendIt(c.get_session(), event.getName(), "yourdice");
                    }
                }
            }
        }

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
        } else if (obj instanceof Event){
            Event event = (Event) obj;
            json.put("event", event.getId());
            json.put("type", event.getType());
            json.put("name", event.getName());
            json.put("reason", event.getReason());
            json.put("resource", event.getResource());
            json.put("player", event.getPlayer());
        } else {
            json.put("ERROR", obj.getClass().getName() + " is not allowed!");
        }

        try {
            session.getRemote().sendString(String.valueOf(json));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------GET DICE SERVICES------------------------------

    public static void getDiceServices(Session session){
        List<String> diceUris = new ArrayList<>();
        try {
            JSONArray diceservices = (JSONArray) Unirest.get(YELLOW_PAGES + "/services/of/name/lmnp_dice").asJson().getBody().getObject().get("services");
            System.out.println("found this : " + diceservices.toString());

            for (Object diceID : diceservices){
                JSONObject dice = Unirest.get(YELLOW_PAGES + diceID.toString()).asJson().getBody().getObject();
                diceUris.add(dice.get("uri") + dice.getString("service"));
                System.out.println("Dices: " + diceUris.toString());
            }

            sendIt(session, diceUris.toString(), "dices");
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    }

    //-----------------------------------POST GAME------------------------------

    public static void postGame(Session session, String gamename, String diceUri){
        try {
            JSONArray gameservices = (JSONArray) Unirest.get(YELLOW_PAGES + "/services/of/name/lmnp_games").asJson().getBody().getObject().get("services");
            JSONObject game = Unirest.get(YELLOW_PAGES + gameservices.get(0)).asJson().getBody().getObject();

            JSONObject newgame = new JSONObject();
            JSONObject newgameServices = new JSONObject();

            newgameServices.put("dice", diceUri);
            newgame.put("name", gamename);
            newgame.put("services", newgameServices);

            System.out.println("Post this game : " + newgame.toString());
            System.out.println("here : " + game.getString("uri") + game.getString("service"));
            HttpResponse<String> res = Unirest.post(game.getString("uri") + game.getString("service"))
                    .header("Content-Type", "application/json")
                    .body(newgame)
                    .asString();

            //TODO Fehlermeldung
             if (res.getStatus() == 201){
                getGames(session);
             }


        } catch (UnirestException e) {
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

        String name = new JSONObject(request.body()).get("player").toString();
        System.out.println("POST Turn for: " + name);
        System.out.println("want call "+getClient(name).get_gameUrl());

        try {
            JSONObject game = Unirest.get(getClient(name).get_gameUrl()).asJson().getBody().getObject();
            JSONObject services = (JSONObject) game.get("services");
            String bankUri = services.getString("bank");
            JSONObject account = Unirest.get(bankUri+getClient(name).get_bankaccountId()).asJson().getBody().getObject();
            String saldo = account.get("saldo")+"";

            sendIt(getClient(name).get_session(), saldo, "saldo");


        } catch (UnirestException e) {
            e.printStackTrace();
        }

        System.out.println("Its your turn, " + getClient(name).get_username());

        sendIt(getClient(name).get_session(), "It's your turn, Baby!", "turn");

        return "OK";
    }

    /**
     * suche mir den passenden Client zum Namen
     * @param playerId der Name
     * @return der Client
     */
    private static Client getClient (String playerId){
        for (Client client: _clientList) {
            if (client.get_playerID().equals(playerId)){
                return client;
            }
        }
        return null;
    }

    //-----------------------------------ROLL DICE------------------------------

    /**
     * Such die Dice-URI zum game
     * @param client Der client der wuerfeln moechte
     * @return die DiceURI
     */
    private static String getDice(Client client){
        String diceUri = null;

        JSONArray antwort;
        JSONObject antwortElem;

        try {
            antwort = Unirest.get(client.get_gameUrl()).asJson().getBody().getArray();

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                JSONObject services = (JSONObject) antwortElem.get("services");

                String pawnUri = services.get("board") + client.get_pawnId();
                JSONObject pawn = Unirest.get(pawnUri).asJson().getBody().getObject();
                diceUri = services.get("board") + pawn.getString("roll");
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

            String rollUri = getDice(client);
            if (rollUri != null){
                System.out.println(client.get_username() +" wants to roll here : "+ rollUri);

                try {

                    HttpResponse<String> res = Unirest.post(rollUri)
                            .header("Content-Type", "application/json")
                            .asString();

                    JSONObject game = Unirest.get(client.get_gameUrl()).asJson().getBody().getObject();
                    JSONObject gameservices = game.getJSONObject("services");

                    JSONObject pawn = Unirest.get(gameservices.getString("board") + client.get_pawnId()).asJson().getBody().getObject();

                    sendIt(client.get_session(), pawn.get("position")+"", "place");

                    JSONObject place = Unirest.get(gameservices.getString("board") + pawn.getString("place")).asJson().getBody().getObject();

                    JSONObject broker = Unirest.get(gameservices.getString("board") + place.getString("broker")).asJson().getBody().getObject();

                    JSONObject owner = Unirest.get(gameservices.getString("board") + broker.getString("owner")).asJson().getBody().getObject();

                    System.out.println("Owner" + owner);

                    if (owner == null){
                        sendIt(client.get_session(), owner.getString("cost").toString(), "buy");
                    }

                } catch (UnirestException e) {
                    e.printStackTrace();
                }
            }
        } else {

        }
    }


    //-----------------------------------BUY------------------------------

    /**
     *
     * @param user
     */
    public static void buy(Session user) {
        Client client = getClient(user);

        JSONObject game = null;
        try {
            game = Unirest.get(client.get_gameUrl()).asJson().getBody().getObject();

            JSONObject gameservices = game.getJSONObject("services");

            JSONObject pawn = Unirest.get(gameservices.getString("board") + client.get_pawnId()).asJson().getBody().getObject();

            sendIt(client.get_session(), pawn.get("position")+"", "place");

            JSONObject place = Unirest.get(gameservices.getString("board") + pawn.getString("place")).asJson().getBody().getObject();

            JSONObject broker = Unirest.get(gameservices.getString("board") + place.getString("broker")).asJson().getBody().getObject();

            HttpResponse<String> res = Unirest.post(gameservices.getString("board") + broker.getString("owner"))
                    .header("Content-Type", "application/json")
                    .body(client.get_playerID())
                    .asString();

            sayReady(user);
        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

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
            JSONObject playerx;
            JSONObject gameSevices;

            try {
                JSONObject gameInformations = Unirest.get(game).asJson().getBody().getObject();
                String playersUri = gameInformations.get("players").toString();
                JSONObject gameServices = (JSONObject) gameInformations.get("services");
                JSONObject gameComponents = (JSONObject) gameInformations.get("components");
                String uri = gameServices.get("game").toString();
                System.out.println("send this: " + player.toString());
                HttpResponse<String> res = Unirest.post(uri + playersUri)
                        .header("Content-Type", "application/json")
                        .body(player)
                        .asString();


                gameSevices = Unirest.get(game).asJson().getBody().getObject().getJSONObject("services");
                System.out.println("game uri : "+gameSevices.get("game"));
                System.out.println(res.getHeaders().getFirst("Location"));
                playerx = Unirest.get(gameSevices.get("game")+res.getHeaders().getFirst("Location")).asJson().getBody().getObject();

                System.out.println("PlayerUris: " + playerx);

                client.set_gameUrl(game);
                client.set_boardId(gameComponents.getString("board"));
                System.out.println("save board: "+gameComponents.getString("board"));

                client.set_gameId(game.split("/")[game.split("/").length-1]);
                client.set_pawnId(playerx.get("pawn").toString());
                client.set_bankaccountId(playerx.get("account").toString());
                client.set_readyId(playerx.getString("ready"));
                client.set_playerID(playerx.getString("id"));

                System.out.println("PawnId : " + client.get_pawnId());

                subscribeEvents(client.get_pawnId());

                sendIt(session, name, "signedup");
            } catch (UnirestException e) {
                e.printStackTrace();

                sendIt(session, "Hi " + name + ", something went wrong : " + e, "notsignedup");
            }
        } else {
            sendIt(session, "Hey Dude, you need a fancy name!", "notsignedup");
        }

    }

    private static void subscribeEvents(String pawnId) {

        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject();
        JSONObject event = new JSONObject();
        event.put("player", pawnId);
        json.put("game", "default");
        json.put("uri", "http://"+ip.getHostAddress()+":4567/client/event");
        json.put("event", event);

        try {
            JSONArray eventsservices = (JSONArray) Unirest.get(YELLOW_PAGES + "/services/of/name/lmnp_events").asJson().getBody().getObject().get("services");
            JSONObject events = Unirest.get(YELLOW_PAGES + eventsservices.get(0)).asJson().getBody().getObject();

            JSONObject getevents = Unirest.get(events.getString("uri") + events.getString("service")).asJson().getBody().getObject();
            System.out.println("Subscribe here: "+ events.getString("uri") + getevents.getString("subscriptions"));
            System.out.println("with: " + json.toString());
            HttpResponse<String> res = Unirest.post(events.getString("uri") + getevents.getString("subscriptions"))
                    .header("Content-Type", "application/json")
                    .body(json)
                    .asString();

        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    //-----------------------------------READY------------------------------

    /**
     *
     * @param user
     */
    public static void sayReady(Session user) {

        Client client = null;

        for (Client c : _clientList){
            if (c.get_session().equals(user)){
                client = c;
            }
        }
        try {
            JSONArray gameservices = (JSONArray) Unirest.get(YELLOW_PAGES + "/services/of/name/lmnp_games").asJson().getBody().getObject().get("services");
            JSONObject game = Unirest.get(YELLOW_PAGES + gameservices.get(0)).asJson().getBody().getObject();

            String PlayerReadyUri = game.getString("uri") + client.get_readyId();
            System.out.println("say ready to : "+PlayerReadyUri);
            HttpResponse<String> ready = Unirest.put(PlayerReadyUri).asString();

            sendIt(user, "startgame", "startgame");

        } catch (UnirestException e) {
            e.printStackTrace();
        }


    }

    //-----------------------------------GET GAMES------------------------------

    /**
     * Suche die URIs für alle offenen verfuegbaren Games!
     * @param session session des Users
     */
    static void getGames(Session session) {
        JSONArray antwort; // alle services namens : lmnp_games
        JSONObject antwortElem;
        List<String> openGameUris = new ArrayList<>();

        //fragt alle Services ab die den angegebenen namen haben
        try {
            antwort = Unirest.get(YELLOW_PAGES + "/services/of/name/lmnp_games").asJson().getBody().getArray();

            for(Object jsonEntry : antwort) {
                antwortElem = (JSONObject)jsonEntry;

                JSONArray serviceIds = (JSONArray) antwortElem.get("services");
                for(Object serviceId : serviceIds){
                    //Search GameService Uri
                    JSONObject getServiceIdObj = Unirest.get(YELLOW_PAGES + serviceId).asJson().getBody().getObject();
                    String gameServiceUri = getServiceIdObj.get("uri").toString();
                    String completeGameServiceUri = gameServiceUri + getServiceIdObj.get("service").toString();
                    //Get Game Ids
                    JSONArray gamesArr = (JSONArray) Unirest.get(completeGameServiceUri).asJson().getBody().getObject().get("games");

                    //Search GameUris
                    List<String> games = new ArrayList<>();
                    for(Object a : gamesArr) {
                        String s = (String)a;
                        games.add(gameServiceUri + s);
                    }
                    for (String game: games) {
                        System.out.println("check game : "+game);
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
        JSONObject antwort;

        try {
            antwort = Unirest.get(game).asJson().getBody().getObject();
            if(!antwort.get("status").toString().matches("FINISHED")){
                gameIsOpen = true;
            }
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return gameIsOpen;
    }
}
