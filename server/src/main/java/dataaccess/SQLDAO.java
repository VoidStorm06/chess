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
                ps.executeUpdate();
            }
            var gameStatement = "TRUNCATE game";
            try (PreparedStatement ps = conn.prepareStatement(gameStatement)) {
                ps.executeUpdate();
            }
            var authStatement = "TRUNCATE auth";
            try (PreparedStatement ps = conn.prepareStatement(authStatement)) {
                ps.executeUpdate();
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

    private final String[] tableStatements = {
        """
        CREATE TABLE IF NOT EXISTS user (
        'username' varchar(255) NOT NULL, 
        'password' varchar(255) NOT NULL, 
        'email' varchar(255) NOT NULL, 
        PRIMARY KEY ('username'))
        """, """
        CREATE TABLE IF NOT EXISTS auth (
        'username' varchar(255) NOT NULL, 
        'authToken' varchar(255) NOT NULL, 
        PRIMARY KEY ('username'))
        """, """
        CREATE TABLE IF NOT EXISTS game (
        'gameID' int NOT NULL AUTO_INCREMENT, 
        'whiteUsername' varchar(255) NOT NULL, 
        'blackUsername' varchar(255) NOT NULL, 
        'gameName' varchar(255) NOT NULL, 
        'game' JSON DEFAULT NULL, 
        PRIMARY KEY ('gameID')) 
        """
    };

    public void configureDatabases() throws DataAccessException {
        DatabaseManager.createDatabase();
        //create tables within database
        try (Connection conn = DatabaseManager.getConnection()) {
            for (var statement : tableStatements) {
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.executeQuery();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot configure database");
        }

    }
}
