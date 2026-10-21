package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.Services;

public class Server {
    private final Services services;
    private final Javalin javalin;

    public Server() {
        services = new Services(new MemoryDAO());
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                //deletes database
                .delete("/db", context -> context.result("hello"))
        //registers user
                .post("/user", this::registerHandler)
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
        //exceptions
    }

    //handlers here

    private void registerHandler(Context context) throws DataAccessException {
        UserData userData = new Gson().fromJson(context.body(), UserData.class);
        AuthData authToken = services.registerUser(userData);
        context.result(new Gson().toJson(authToken));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
