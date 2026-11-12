package client;

import model.UserData;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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
        serverFacade.deleteAll();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void registerTest() throws Exception {
        UserData user = new UserData("bob", "bob", "bob");
        var temp = serverFacade.registerUser(user);
        Assertions.assertNotNull(serverFacade.registerUser(user));
        Assertions.assertEquals(user.username(), temp.username());
    }

    @Test
    public void badRegisterTest() throws Exception {
        UserData user = new UserData("bob", "bob", null);
        Assertions.assertThrows(IllegalArgumentException.class, () -> serverFacade.registerUser(user));
    }

}
