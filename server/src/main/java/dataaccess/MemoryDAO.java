package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class MemoryDAO implements DAO {
    private int gameID;
    final private HashMap<Integer, GameData> gameList = new HashMap<>();
    final private HashMap<String, UserData> userList = new HashMap<>();
    final private HashMap<String, AuthData> authList = new HashMap<>();

    public MemoryDAO() throws DataAccessException {
        gameID = 1;
    };

    @Override
    public void clearAllDatabases() {
        gameList.clear();
        userList.clear();
        authList.clear();
        gameID = 1;
    };
    public AuthData createUser(UserData user) {
        userList.put(user.username(), user);
        return createAuth(user.username());
    };
    public UserData getUser(UserData user) {
        return userList.get(user.username());
    };
    public int createNewGame (ChessGame game, String gameName) {
        var gameData = new GameData(gameID, "", "" , gameName ,game);
        gameList.put(gameID, gameData);
        gameID++;
        return 0;
    };
    public HashMap<Integer, GameData> listAllGames() {
        return null;
    };
    public void updateGame() {
    };
    public AuthData createAuth(String username) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), username);
        authList.put(authData.username(), authData);
        return authData;
    };
    public AuthData getAuth(String username) {
        return authList.get(username);
    };
    public void deleteAuth(AuthData authData) {
        authList.remove(authData.username());
    };
}
