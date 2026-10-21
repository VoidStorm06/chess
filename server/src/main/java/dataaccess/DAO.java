package dataaccess;

import chess.ChessGame;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public interface DAO {
    void clearAllDatabases() throws DataAccessException;
    AuthData createUser(UserData user) throws DataAccessException;
    UserData getUser(UserData user) throws DataAccessException;
    int createNewGame(ChessGame game, String gameName) throws DataAccessException;
    HashMap<Integer, GameData> listAllGames() throws DataAccessException;
    void updateGame() throws DataAccessException;
    AuthData createAuth(String username) throws DataAccessException;
    AuthData getAuth(String username) throws DataAccessException;
    void deleteAuth(AuthData authData) throws DataAccessException;

}
