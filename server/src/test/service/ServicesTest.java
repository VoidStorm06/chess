package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {
    private UserData tempUser = new UserData("bob", "bob", "bob");
    private MemoryDAO tempDAO = new MemoryDAO();
    private Services tempServices = new Services(tempDAO);

    @Test
    void registerUserExists() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        var returnUser = tempDAO.getUser(tempUser);
        assertEquals(tempUser, returnUser);
    }

    @Test
    void registerUserDoesNotExist() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        var returnUser = tempDAO.getUser(tempUser);
        tempUser = new UserData("bill", "bill", "bill");
        assertNotEquals(tempUser, returnUser);
    }

    @Test
    void cannotRegisterSameUsername() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        var duplicateUser = new UserData("bob", "bob", "bob");
        assertThrows(DataAccessException.class, () -> {tempServices.registerUser(duplicateUser);});
    }

    @Test
    void logOut() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        var logoutRequest = new LogoutRequest(authToken.authToken());
        tempServices.logOut(logoutRequest);
        assertNull(tempDAO.getAuth(authToken));
    }

    @Test
    void logOutFailed() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        var logoutRequest = new LogoutRequest(authToken.authToken());
        tempServices.logOut(logoutRequest);
        assertThrows(DataAccessException.class, () -> {tempServices.logOut(logoutRequest);});
    }

    @Test
    void logIn() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        var logoutRequest = new LogoutRequest(authToken.authToken());
        tempServices.logOut(logoutRequest);
        var loginRequest = new LoginRequest("bob", "bob");
        authToken = tempServices.logIn(loginRequest);
        assertNotNull(tempDAO.getAuth(authToken));
    }

    @Test
    void logInFailed() throws DataAccessException {
        var loginRequest = new LoginRequest("bob", "bob");
        assertThrows(DataAccessException.class, () -> {tempServices.logIn(loginRequest);});
    }
}