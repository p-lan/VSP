package client;

/**
 * Created by patricklanger on 20.05.16.
 */
public class Client {

    private int _id;
    private String _username;
    private String _gameUrl;
    private String _userUrl;

    /**
     * Constructor
     * @param id zur Verwaltung im Clientmanager
     */
    public Client(int id){
        _id = id;
    }


    // ---- Getter und Setter ---- //

    public String get_userUrl() {
        return _userUrl;
    }

    public void set_userUrl(String _userUrl) {
        this._userUrl = _userUrl;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String get_username() {
        return _username;
    }

    public void set_username(String _username) {
        this._username = _username;
    }

    public String get_gameUrl() {
        return _gameUrl;
    }

    public void set_gameUrl(String _gameUrl) {
        this._gameUrl = _gameUrl;
    }

}
