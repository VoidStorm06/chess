package client;

import model.CreateGameRequest;
import model.LoginRequest;
import model.LogoutRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    private static String url;
    private static ServerFacade serverFacade;

    @BeforeAll
    public static void init() throws Exception {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        url = String.format("http://localhost:%d", port);
        serverFacade = new ServerFacade(url);
    }

    @BeforeEach
    public void start() throws Exception {
        serverFacade.deleteAll();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() throws Exception {
        UserData user = new UserData("boring", "bob", "bob");
        var temp = Assertions.assertDoesNotThrow(() -> serverFacade.registerUser(user));
        Assertions.assertEquals(user.username(), temp.username());
    }

    @Test
    public void badRegisterTest() {
        UserData user = new UserData("bob", "bob", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> serverFacade.registerUser(user));
    }

    @Test
    public void LoginTest() throws Exception {
        UserData user = new UserData("bo", "bo", "bob");
        var temp = serverFacade.registerUser(user);
        serverFacade.logoutUser(new LogoutRequest(temp.authToken()));
        var auth = Assertions.assertDoesNotThrow(() ->
                serverFacade.loginUser(new LoginRequest("bo", "bo")));
    }

    @Test
    public void CreateGame() throws Exception {
        UserData user = new UserData("joe", "joe", "joe");
        var temp = serverFacade.registerUser(user);
        Assertions.assertDoesNotThrow(() ->
                serverFacade.createGame(new CreateGameRequest(temp.authToken(), "game1")));
    }

    @Test
    public void CreateInvalidGame() throws Exception {
        UserData user = new UserData("jo", "jo", "jo");
        var temp = serverFacade.registerUser(user);
        Assertions.assertThrows(Exception.class, () ->
                serverFacade.createGame(new CreateGameRequest(temp.authToken(), null)));
    }

    @Test
    public void ListGames() throws Exception {
        UserData user = new UserData("ben", "ben", "ben");
        var temp = serverFacade.registerUser(user);
        serverFacade.createGame(new CreateGameRequest(temp.authToken(), "game1"));
        serverFacade.createGame(new CreateGameRequest(temp.authToken(), "game2"));
        var games = Assertions.assertDoesNotThrow(() -> serverFacade.listGames(temp.authToken()));
        Assertions.assertEquals(2, games.size());
        serverFacade.createGame(new CreateGameRequest(temp.authToken(), "game3"));
        games = Assertions.assertDoesNotThrow(() -> serverFacade.listGames(temp.authToken()));
        Assertions.assertEquals(3, games.size());
    }


}
