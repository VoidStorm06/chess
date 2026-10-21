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
        return createAuth(user);
    };
    public UserData getUser(UserData user) {
        return userList.get(user.username());
    };
    public void createNewGame () {
        var gameData = new GameData(gameID, "", "" , "" , new ChessGame());
        gameList.put(gameID, gameData);
        gameID++;
    };
    public HashMap<Integer, GameData> listAllGames() {
        return gameList;
    };
    public void updateGame(String user, ChessGame.TeamColor color, Integer gameID) {
        var prevGameData = gameList.get(gameID);
        GameData gameData;
        if (color == ChessGame.TeamColor.BLACK) {
             gameData = new GameData(gameID, prevGameData.whiteUsername(), user,
                     prevGameData.gameName(), prevGameData.game());
        } else {
            gameData = new GameData(gameID, user, prevGameData.blackUsername(),
                    prevGameData.gameName(), prevGameData.game());
        }
        gameList.remove(gameID);
        gameList.put(gameID, gameData);
    };
    public AuthData createAuth(UserData user) {
        AuthData authData = new AuthData(UUID.randomUUID().toString(), user.username());
        authList.put(authData.username(), authData);
        return authData;
    };
    public AuthData getAuth(UserData user) {
        return authList.get(user.username());
    };
    public void deleteAuth(AuthData authData) {
        authList.remove(authData.username());
    };
}
