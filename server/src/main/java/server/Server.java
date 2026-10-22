package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import io.javalin.*;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import service.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private final Services services;
    private final Javalin javalin;

    public Server() {
        services = new Services(new MemoryDAO());
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                //deletes database
                .delete("/db", this::deleteHandler)
        //registers user
                .post("/user", this::registerHandler)
        //logs in
                .post("/session", this::logInHandler)
        //logs out
                .delete("/session", this::logOutHandler)
        //lists the games currently available
                .get("/game", this::showGamesHandler)
        //creates a game
                .post("/game", this::createGameHandler)
        //joins a game
                .put("/game", this::joinHandler)
        //exceptions
                .exception(DataAccessException.class, this::exceptionHandler);
    }

    //handlers here

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }

    private void exceptionHandler(DataAccessException exception, Context context) {
        context.status(exception.messageToCode());
        context.result(exception.toJson());
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
        context.result(new Gson().toJson(new HashMap<>()));
    }

    private void createGameHandler(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        service.CreateGameInput input = new Gson().fromJson(context.body(), CreateGameInput.class);
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, input.gameName());
        CreateGameResponse gameCreated = services.createNewGame(createGameRequest);
        context.result(new Gson().toJson(gameCreated));
    }

    private void showGamesHandler(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        Collection<ShowGamesResponse> response = services.viewAllGames(authToken);
        Map<String, Object> gameList = new HashMap<>();
        gameList.put("games", response);
        context.result(new Gson().toJson(gameList));
    }

    private void joinHandler(Context context) throws DataAccessException {
        String authToken = context.header("authorization");
        JoinRequest input = new Gson().fromJson(context.body(), JoinRequest.class);
        services.joinCurrGame(authToken, input);
        context.result(new Gson().toJson(new HashMap<>()));
    }

    private void deleteHandler(Context context) throws DataAccessException {
        services.deleteDatabases();
    }
}
