package services.events;

import java.util.regex.Pattern;

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
    
    public boolean match(Event regexEvent){
    	return (
    			this.game == null || Pattern.matches(this.game, regexEvent.getGame()))
                && (this.type == null || Pattern.matches(this.type, regexEvent.getType()))
                && (this.name == null || Pattern.matches(this.name, regexEvent.getName()))
                && (this.reason == null || Pattern.matches(this.reason, regexEvent.getReason()))
                && (this.resource == null || Pattern.matches(this.resource, regexEvent.getResource()))
                && (this.player == null || Pattern.matches(this.player, regexEvent.getPlayer())
                );		
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

	@Override
	public String toString() {
		return "Event [id=" + id + ", game=" + game + ", type=" + type + ", name=" + name + ", reason=" + reason
				+ ", resource=" + resource + ", player=" + player + ", time=" + time + "]";
	}
    
}
