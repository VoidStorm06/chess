package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.HashMap;

public class SQLDAO implements DAO {

    @Override
    public void clearAllDatabases() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public int createNewGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public HashMap<Integer, GameData> listAllGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {

    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {

    }
}
