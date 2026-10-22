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
    private int gameID = 1;
    final private HashMap<Integer, GameData> gameList = new HashMap<>();
    final private HashMap<String, UserData> userList = new HashMap<>();
    final private HashMap<String, AuthData> authList = new HashMap<>();

    public void clearAllDatabases() {
        gameList.clear();
        userList.clear();
        authList.clear();
        gameID = 1;
    };

    public void createUser(UserData user) {
        userList.put(user.username(), user);
    };

    public UserData getUser(UserData user) {
        return userList.get(user.username());
    };

    public int createNewGame (String gameName) {
        var gameData = new GameData(gameID, null, null,
                gameName , new ChessGame());
        gameList.put(gameID, gameData);
        int tempID=gameID;
        gameID++;
        return tempID;
    };

    public GameData getGame(Integer gameID)  {
        return gameList.get(gameID);
    }

    public HashMap<Integer, GameData> listAllGames() {
        return gameList;
    };

    public void updateGame(GameData gameData) {
        gameList.remove(gameData.gameID());
        gameList.put(gameData.gameID(), gameData);
    };

    public AuthData createAuth(UserData user) {
        AuthData authData = new AuthData(user.username(), UUID.randomUUID().toString());
        authList.put(authData.authToken(), authData);
        return authData;
    };

    public AuthData getAuth(AuthData authData) {
        return authList.get(authData.authToken());
    };

    public void deleteAuth(AuthData authData) {
        authList.remove(authData.authToken());
    };
}
