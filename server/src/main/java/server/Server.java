package server;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.LoginRequest;
import service.LogoutRequest;
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
                .post("/session", this::logInHandler)
        //logs out
                .delete("/session", this::logOutHandler)
        //lists the games currently available
                .get("/game", context -> context.result("Works"))
        //creates a game
                .post("/game", context -> context.result("Works"))
        //joins a game
                .put("/game", context -> context.result("Works"))
        //exceptions
                .exception(DataAccessException.class, this::exceptionHandler);
    }

    //handlers here

    private void exceptionHandler(DataAccessException exception, Context context) {

    }

    private void registerHandler(Context context) throws DataAccessException {
        UserData userData = new Gson().fromJson(context.body(), UserData.class);
        AuthData authToken = services.registerUser(userData);
        context.result(new Gson().toJson(authToken));
    }

    private void logInHandler(Context context) throws DataAccessException {
        LoginRequest loginRequest = new Gson().fromJson(context.body(), LoginRequest.class);
        AuthData authToken = services.logIn(loginRequest);
        context.result(new Gson().toJson(authToken));
    }

    private void logOutHandler(Context context) throws DataAccessException {
        LogoutRequest logoutRequest = new LogoutRequest(context.header("authorization"));
        services.logOut(logoutRequest);
        context.result(new Gson().toJson(new JsonObject()));
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
