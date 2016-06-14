package services.events;

public class Subscription {
	
		
	private int intID;
	private String id; // uri zur subscription
	private String game;
	private String interrestetUri;
	private Event reqexEvent;
	
	
	public Subscription(int intID, String id, String game, String interrestetUri, Event reqexEvent) {
		this.intID=intID;
		this.id = id;
		this.game = game;
		this.interrestetUri = interrestetUri;
		this.reqexEvent = reqexEvent;
	}


	public String getId() {
		return id;
	}


	public String getGame() {
		return game;
	}


	public String getInterrestetUri() {
		return interrestetUri;
	}


	public Event getReqexEvent() {
		return reqexEvent;
	}


	public int getIntID() {
		return intID;
	}


	@Override
	public String toString() {
		return "Subscription [id=" + id + ", game=" + game + ", interrestetUri=" + interrestetUri + ", reqexEvent="
				+ reqexEvent + "]";
	}
	
}
