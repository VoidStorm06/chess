package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                //deletes database
                .delete("/db", context -> context.result("Works"))
        //registers user
                .post("/user", context -> context.result("hello"))
        //logs in
                .post("/session", context -> context.result("Works"))
        //logs out
                .delete("/session", context -> context.result("Works"))
        //lists the games currently available
                .get("/game", context -> context.result("Works"))
        //creates a game
                .post("/game", context -> context.result("Works"))
        //joins a game
                .put("/game", context -> context.result("Works"));

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
