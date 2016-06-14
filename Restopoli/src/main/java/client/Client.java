package client;

import org.eclipse.jetty.websocket.api.Session;

/**
 * Created by patricklanger on 20.05.16.
 */
public class Client {

    private int _id;
    private String _username;
    private String _gameUrl;
    private String _userUrl;
    private Session _session;
    private String _gameId;
    private String _pawnId;

    private String _bankaccountId;

    private String _boardId;

    /**
     * Constructor
     * @param id zur Verwaltung im Clientmanager
     */
    public Client(int id, Session session, String name){
        _id = id;
        _session = session;
        _username = name;
    }


    // ---- Getter und Setter ---- //


    public String get_bankaccountId() { return _bankaccountId; }

    public void set_bankaccountId(String _bankaccountId) { this._bankaccountId = _bankaccountId; }

    public String get_boardId() { return _boardId; }

    public void set_boardId(String _boardId) { this._boardId = _boardId; }

    public Session get_session() {
        return _session;
    }

    public void set_session(Session _session) {
        this._session = _session;
    }

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

    public void set_gameId(String _gameId) {
        this._gameId = _gameId;
    }

    public String get_gameId() {
        return _gameId;
    }

    public String get_pawnId() {
        return _pawnId;
    }

    public void set_pawnId(String _pawnId) {
        this._pawnId = _pawnId;
    }
}
