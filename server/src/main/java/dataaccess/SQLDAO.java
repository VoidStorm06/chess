package dataaccess;

import chess.ChessGame;
import chess.ChessPosition;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class SQLDAO implements DAO {

    public SQLDAO()  {
        try {
            configureDatabases();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
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
            var statement = "SELECT * FROM user WHERE username=?";
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
        int returnValue = 0;
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT into game (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(statement, PreparedStatement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, null);
                ps.setString(2, null);
                ps.setString(3, gameName);
                ps.setString(4, new Gson().toJson(new ChessGame()));
                ps.executeUpdate();
                var result = ps.getGeneratedKeys();
                if (result.next()) {
                    returnValue = result.getInt(1);
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database", ex);
        }
        return returnValue;
    }

    @Override
    public HashMap<Integer, GameData> listAllGames() throws DataAccessException {
        HashMap<Integer, GameData> gameList = new HashMap<>();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                try (var response = ps.executeQuery()) {
                    while (response.next()) {
                        GameData gameData = new GameData(response.getInt("gameID"),
                                response.getString("whiteUsername"),
                                response.getString("blackUsername"),
                                response.getString("gameName"),
                                deserializeGame(response.getString("game")));
                        gameList.put(response.getInt("gameID"), gameData);
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData gameData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE game Set gameID=?, whiteUsername=?, blackUsername=?, " +
                    "gameName=?, game=? WHERE gameID=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameData.gameID());
                ps.setString(2, gameData.whiteUsername());
                ps.setString(3, gameData.blackUsername());
                ps.setString(4, gameData.gameName());
                ps.setString(5, new Gson().toJson(gameData.game()));
                ps.setInt(6, gameData.gameID());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
    }

    @Override
    public GameData getGame(Integer gameID) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game WHERE gameID=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try(var response = ps.executeQuery()) {
                    if(response.next()) {
                        return new GameData(gameID, response.getString("whiteUsername"),
                                response.getString("blackUsername"),
                                response.getString("gameName"),
                                deserializeGame(response.getString("game")));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
        return null;
    }

    @Override
    public AuthData createAuth(UserData user) throws DataAccessException {
        String authToken = UUID.randomUUID().toString();
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "INSERT into auth (username, authToken) VALUES (?, ?)";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, user.username());
                ps.setString(2, authToken);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database", ex);
        }
        return new AuthData(user.username(), authToken);
    }

    @Override
    public AuthData getAuth(AuthData authData) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM auth WHERE authToken=?";
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                try(var response = ps.executeQuery()) {
                    if(response.next()) {
                        return new AuthData(response.getString("username"),
                                response.getString("authToken"));
                    }
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
        return null;
    }

    @Override
    public void deleteAuth(AuthData authData) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM auth WHERE authToken=?";
            try(PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, authData.authToken());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
    }

    private ChessGame deserializeGame(String game) {
        return new Gson().fromJson(game, ChessGame.class);
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
        PRIMARY KEY (authToken))
        """, """
        CREATE TABLE IF NOT EXISTS game (
        gameID int NOT NULL AUTO_INCREMENT, 
        whiteUsername varchar(255), 
        blackUsername varchar(255), 
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
