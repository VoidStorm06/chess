package dataaccess;

import model.UserData;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.SQLException;

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
    }
}