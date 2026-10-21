package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public interface DAO {
    void clearAllDatabases() throws DataAccessException;
    void createUser(UserData user) throws DataAccessException;
    UserData getUser(UserData user) throws DataAccessException;
    void createNewGame() throws DataAccessException;
    HashMap<Integer, GameData> listAllGames() throws DataAccessException;
    void updateGame(GameData gameData) throws DataAccessException;
    AuthData createAuth(UserData user) throws DataAccessException;
    AuthData getAuth(UserData user) throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;

}
