package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

public class SQLDAO implements DAO {

    public SQLDAO() throws DataAccessException {
        configureDatabases();
    }

    @Override
    public void clearAllDatabases() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var userStatement = "TRUNCATE user";
            try (PreparedStatement ps = conn.prepareStatement(userStatement)) {
                ps.executeQuery();
            }
            var gameStatement = "TRUNCATE game";
            try (PreparedStatement ps = conn.prepareStatement(gameStatement)) {
                ps.executeQuery();
            }
            var authStatement = "TRUNCATE auth";
            try (PreparedStatement ps = conn.prepareStatement(authStatement)) {
                ps.executeQuery();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot update database");
        }
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

    public void configureDatabases() throws DataAccessException {
        DatabaseManager.createDatabase();
        //create tables within database
    }
}
