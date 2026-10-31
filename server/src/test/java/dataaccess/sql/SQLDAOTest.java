package dataaccess.sql;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import dataaccess.DAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SQLDAOTest {
    @Test
    void createUser() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        UserData testUser = new UserData("bob", "bob", "bob.gmail.com");
        sqlDAO.createUser(testUser);
        try (Connection conn = DatabaseManager.getConnection()) {
           try (var preppedStatement = conn
                   .prepareStatement("SELECT username, password, email FROM user WHERE username='bob'")) {
               try (var response = preppedStatement.executeQuery()) {
                   while(response.next()) {
                       assertEquals("bob",response.getString("username"));
                       assertEquals("bob.gmail.com",response.getString("email"));
                       assertTrue(BCrypt.checkpw("bob", response.getString("password")));
                   }
               }
           }
        } catch (SQLException ex) {
           throw new DataAccessException("Cannot access database", ex);
        }
    }

    @Test
    void getUser() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        UserData testUser = new UserData("bill", "bill", "bill.gmail.com");
        sqlDAO.createUser(testUser);
        var getUser = sqlDAO.getUser(testUser);
        assertEquals(testUser.username(), getUser.username());
        assertEquals(testUser.email(), getUser.email());
        assertTrue(BCrypt.checkpw(testUser.password(), getUser.password()));
    }

    @Test
    void clearAllDatabases() throws DataAccessException{
        DAO sqlDAO = new SQLDAO();
        UserData testUser = new UserData("bob", "bob", "bob.gmail.com");
        sqlDAO.createUser(testUser);
        sqlDAO.clearAllDatabases();
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = DATABASE() AND table_name = 'user'")) {
                try(var response = ps.executeQuery()) {
                    assertTrue(response.next());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = DATABASE() AND table_name = 'auth'")) {
                try(var response = ps.executeQuery()) {
                    assertTrue(response.next());
                }
            }
            try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM information_schema.tables " +
                    "WHERE table_schema = DATABASE() AND table_name = 'game'")) {
                try(var response = ps.executeQuery()) {
                    assertTrue(response.next());
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException("Cannot access database");
        }
    }

    @Test
    void createNewGame() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        int gameId = sqlDAO.createNewGame("First Game");
        assertEquals(1, gameId);
        gameId = sqlDAO.createNewGame("Second Game");
        assertEquals(2, gameId);
        sqlDAO.clearAllDatabases();
        gameId = sqlDAO.createNewGame("New First Game");
        assertEquals(1, gameId);
    }

    @Test
    void listAllGames() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        sqlDAO.createNewGame("First Game");
        HashMap<Integer, GameData> gameList = new HashMap<>();
        gameList.put(1, new GameData(1, null,
                null, "First Game", new ChessGame()));
        HashMap<Integer, GameData> returnedGameList = sqlDAO.listAllGames();
        assertEquals(gameList, returnedGameList);
    }

    @Test
    void updateGame() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        int gameId = sqlDAO.createNewGame("First Game");
        GameData gameData = sqlDAO.getGame(gameId);
        assertEquals("First Game", gameData.gameName());
        assertNull(gameData.whiteUsername());
        assertNull(gameData.blackUsername());
        assertEquals(new ChessGame(), gameData.game());
        sqlDAO.updateGame(new GameData(1,
                "Bob", null, "New First Game", new ChessGame()));
        gameData = sqlDAO.getGame(gameId);
        assertEquals("New First Game", gameData.gameName());
        assertEquals("Bob", gameData.whiteUsername());
        assertNull(gameData.blackUsername());
        assertEquals(new ChessGame(), gameData.game());
        ChessGame newGame = new ChessGame();
        ChessGame testNewGame = new ChessGame();
        try {
            newGame.makeMove(new ChessMove(new ChessPosition(2,1),
                    new ChessPosition(4, 1), null));
            testNewGame.makeMove(new ChessMove(new ChessPosition(2,1),
                    new ChessPosition(4, 1), null));
        } catch (InvalidMoveException e) {
            throw new RuntimeException(e);
        }
        sqlDAO.updateGame(new GameData(1,
                "Bob", "Billy", "First Game", newGame));
        gameData = sqlDAO.getGame(gameId);
        assertEquals("First Game", gameData.gameName());
        assertEquals("Bob", gameData.whiteUsername());
        assertEquals("Billy", gameData.blackUsername());
        assertEquals(testNewGame, gameData.game());
        sqlDAO.clearAllDatabases();
    }

    @Test
    void getGame() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        int gameId = sqlDAO.createNewGame("First Game");
        var gameData = sqlDAO.getGame(gameId);
        assertEquals(1, gameData.gameID());
        assertNull(gameData.whiteUsername());
        assertNull(gameData.blackUsername());
        assertEquals("First Game", gameData.gameName());
        assertEquals(new ChessGame(), gameData.game());
    }

    @Test
    void createAuth() throws DataAccessException{
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        UserData userData = new UserData("Bob", "Bob", "Bob");
        var authData = sqlDAO.createAuth(userData);
        var testAuthData = sqlDAO.getAuth(authData);
        assertEquals(authData, testAuthData);
    }

    @Test
    void getAuth() throws DataAccessException{
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        UserData userData = new UserData("Bob", "Bob", "Bob");
        var authData = sqlDAO.createAuth(userData);
        AuthData newAuthData = new AuthData("", authData.authToken());
        var testAuthData = sqlDAO.getAuth(newAuthData);
        assertEquals(authData, testAuthData);
    }

    @Test
    void deleteAuth() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        sqlDAO.clearAllDatabases();
        UserData userData = new UserData("Bob", "Bob", "Bob");
        var authData = sqlDAO.createAuth(userData);
        assertNotNull(sqlDAO.getAuth(authData));
        sqlDAO.deleteAuth(authData);
        assertNull(sqlDAO.getAuth(authData));
    }
}