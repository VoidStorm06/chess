package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class SQLDAOTest {
    @Test
    void createUser() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
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
        sqlDAO.clearAllDatabases();
    }


    @Test
    void getUser() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        UserData testUser = new UserData("bill", "bill", "bill.gmail.com");
        sqlDAO.createUser(testUser);
        var getUser = sqlDAO.getUser(testUser);
        assertEquals(testUser.username(), getUser.username());
        assertEquals(testUser.email(), getUser.email());
        assertTrue(BCrypt.checkpw(testUser.password(), getUser.password()));
        sqlDAO.clearAllDatabases();
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
        int gameId = sqlDAO.createNewGame("First Game");
        assertEquals(1, gameId);
        gameId = sqlDAO.createNewGame("Second Game");
        assertEquals(2, gameId);
        sqlDAO.clearAllDatabases();
        gameId = sqlDAO.createNewGame("New First Game");
        assertEquals(1, gameId);
        sqlDAO.clearAllDatabases();
    }

    @Test
    void listAllGames() throws DataAccessException {
        DAO sqlDAO = new SQLDAO();
        int gameId = sqlDAO.createNewGame("First Game");
        HashMap<Integer, GameData> gameList = new HashMap<>();
        gameList.put(1, new GameData(1, "",
                "", "First Game", new ChessGame()));
        HashMap<Integer, GameData> returnedGameList = sqlDAO.listAllGames();
        assertEquals(gameList, returnedGameList);
        sqlDAO.clearAllDatabases();
    }
}