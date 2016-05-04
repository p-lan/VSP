package services.events;

public class Event {

    private String id;
    private String game;//not null
    private String type;//not null
    private String name;//not null
    private String reason; //not null
    private String resource;
    private String player;
    private String time;

    public Event(String id, String game, String type, String name, String reason, String resource, String player, String time) {
        this.id = id;
        this.game = game;
        this.type = type;
        this.name = name;
        this.reason = reason;
        this.resource = resource;
        this.player = player;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public String getGame() {
        return game;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getReason() {
        return reason;
    }

    public String getResource() {
        return resource;
    }

    public String getPlayer() {
        return player;
    }

    public String getTime() {
        return time;
    }
    public void setID(String id){
    	this.id=id;
    }

}
