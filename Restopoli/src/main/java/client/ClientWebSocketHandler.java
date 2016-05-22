package client;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.util.List;
@WebSocket
/**
 * Created by patricklanger on 20.05.16.
 */
public class ClientWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
        //Client client = new Client(Clientmanager.getNextUserId());
        //Clientmanager.addToClientMap(user, client);
        Clientmanager.getGames(user);
        //Clientmanager.sendIt(user, "Hello User! Your id is : " + client.get_id(), "have fun!");
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {

    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        System.out.println("Websocket got a new message :");

        if (message.length() >= 3){

            String code = message.substring(0,3);
            message = message.substring(3,message.length());
            System.out.println(code +" : "+message);
            switch (code) {
                case "000" :
                    System.out.println("Failure-Code : " + message);
                    break;
                case "001" :
                    String game = message.substring(0, message.indexOf(','));
                    String name = message.substring(message.indexOf(',') + 1, message.length());
                    System.out.println(name + " want to play here : " + game);
                    Clientmanager.signupUser(user, name, game);
                    break;
                case "101" :
                    Clientmanager.rollDice(user);
                    break;
                default:
                    System.out.println("No Code : " + message);
                    break;
            }

        } else {
            Clientmanager.sendIt(user, "Ungueltiges Nachrichtenformat", "ERROR");
        }
    }

}
