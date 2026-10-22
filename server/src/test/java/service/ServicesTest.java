package service;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import model.AuthData;
import model.UserData;
import org.eclipse.jetty.util.log.Log;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ServicesTest {
    private UserData tempUser = new UserData("bob", "bob", "bob");
    private final MemoryDAO tempDAO = new MemoryDAO();
    private final Services tempServices = new Services(tempDAO);

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

    @Test
    void createNewGame() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        CreateGameRequest request = new CreateGameRequest(authToken.authToken(), "Name");
        CreateGameResponse response = tempServices.createNewGame(request);
        assertEquals(1, response.gameID());
        request = new CreateGameRequest(authToken.authToken(), "Name2");
        response = tempServices.createNewGame(request);
        assertEquals(2, response.gameID());
    }

    @Test
    void failCreateNewGame() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        CreateGameRequest request = new CreateGameRequest("", "Name");
        assertThrows(DataAccessException.class, () -> {tempServices.createNewGame(request);});

    }

    @Test
    void testViewGames() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        CreateGameRequest request = new CreateGameRequest(authToken.authToken(), "");
        CreateGameResponse response = tempServices.createNewGame(request);
        request = new CreateGameRequest(authToken.authToken(), "");
        response = tempServices.createNewGame(request);
        Collection<ShowGamesResponse> gameList = tempServices.viewAllGames(authToken.authToken());
        assertEquals(2, gameList.size());
    }

    @Test
    void testJoinGameBlack() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        CreateGameRequest request = new CreateGameRequest(authToken.authToken(), "");
        CreateGameResponse response = tempServices.createNewGame(request);
        JoinRequest jrequest = new JoinRequest("BLACK", 1);
        tempServices.joinCurrGame(authToken.authToken(), jrequest);
        Collection<ShowGamesResponse> gameList = tempServices.viewAllGames(authToken.authToken());
        ShowGamesResponse game = new ArrayList<>(gameList).getFirst();
        assertEquals(tempUser.username(), game.blackUsername());
    }

    @Test
    void testJoinGameWhite() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        CreateGameRequest request = new CreateGameRequest(authToken.authToken(), "");
        CreateGameResponse response = tempServices.createNewGame(request);
        JoinRequest jrequest = new JoinRequest("WHITE", 1);
        tempServices.joinCurrGame(authToken.authToken(), jrequest);
        Collection<ShowGamesResponse> gameList = tempServices.viewAllGames(authToken.authToken());
        ShowGamesResponse game = new ArrayList<>(gameList).getFirst();
        assertEquals(tempUser.username(), game.whiteUsername());
    }

    @Test
    void joinNonexistentGame() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        JoinRequest jrequest = new JoinRequest("WHITE", 1);
        assertThrows(DataAccessException.class, () -> {tempServices
                    .joinCurrGame(authToken.authToken(), jrequest);});
    }

    @Test
    void deleteDatabases() throws DataAccessException {
        var authToken = tempServices.registerUser(tempUser);
        CreateGameRequest request = new CreateGameRequest(authToken.authToken(), "");
        CreateGameResponse response = tempServices.createNewGame(request);
        request = new CreateGameRequest(authToken.authToken(), "");
        response = tempServices.createNewGame(request);
        Collection<ShowGamesResponse> gameList = tempServices.viewAllGames(authToken.authToken());
        assertEquals(2, gameList.size());
        tempServices.deleteDatabases();
        assertNull(tempDAO.getGame(1));
        assertNull(tempDAO.getGame(2));
        assertNull(tempDAO.getUser(tempUser));
        assertNull(tempDAO.getAuth(authToken));
    }
}