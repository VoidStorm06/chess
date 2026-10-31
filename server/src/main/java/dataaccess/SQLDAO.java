package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

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
            var gameStatement = "TRUNCATE TABLE game";
            try (PreparedStatement ps = conn.prepareStatement(gameStatement)) {
                ps.executeUpdate();
            }
            var authStatement = "TRUNCATE TABLE auth";
            try (PreparedStatement ps = conn.prepareStatement(authStatement)) {
                ps.executeUpdate();
            }
            var userStatement = "TRUNCATE TABLE user";
            try (PreparedStatement ps = conn.prepareStatement(userStatement)) {
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot update database");
        }
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT into user (username, password, email) VALUES (?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                String encryptedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
                ps.setString(2, encryptedPassword);
                ps.setString(3, user.email());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot update database", ex);
        }
    }

    @Override
    public UserData getUser(UserData user) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM user WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                try (var response = ps.executeQuery()) {
                    while(response.next()) {
                        if (BCrypt.checkpw(user.password(), response.getString("password"))) {
                            return new UserData(response.getString("username"),
                                    response.getString("password"), response.getString("email"));
                        }
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
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
        username varchar(255) NOT NULL, 
        password varchar(255) NOT NULL, 
        email varchar(255) NOT NULL, 
        PRIMARY KEY (username))
        """, """
        CREATE TABLE IF NOT EXISTS auth (
        username varchar(255) NOT NULL, 
        authToken varchar(255) NOT NULL, 
        PRIMARY KEY (username))
        """, """
        CREATE TABLE IF NOT EXISTS game (
        gameID int NOT NULL AUTO_INCREMENT, 
        whiteUsername varchar(255) NOT NULL, 
        blackUsername varchar(255) NOT NULL, 
        gameName varchar(255) NOT NULL, 
        game TEXT DEFAULT NULL, 
        PRIMARY KEY (gameID))
        """
    };

    public void configureDatabases() throws DataAccessException {
        DatabaseManager.createDatabase();
        //create tables within database
        try (Connection conn = DatabaseManager.getConnection()) {
            for (var statement : tableStatements) {
                try (PreparedStatement ps = conn.prepareStatement(statement)) {
                    ps.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot configure database");
        }

    }
}
