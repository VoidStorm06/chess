package client;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collection;
import java.util.Map;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void deleteAll() throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/db"))
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String temp = response.body();
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    public AuthData registerUser(UserData userData) throws Exception {
        if (userData.password() == null || userData.username() == null || userData.email() == null) {
            throw new IllegalArgumentException("Error: Incorrect number of arguments");
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/user"))
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(userData)))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!codeIsGood(response.statusCode())) {
                throw new RuntimeException("Couldn't register user, try again" + String.valueOf(response.statusCode()));
            }
            return new Gson().fromJson(response.body(), AuthData.class);
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    public AuthData loginUser(LoginRequest req) throws Exception {
        if (req.password() == null || req.username() == null) {
            throw new IllegalArgumentException("Error: Incorrect number of arguments");
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session"))
                .method("POST", HttpRequest.BodyPublishers.ofString(new Gson().toJson(req)))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!codeIsGood(response.statusCode())) {
                throw new RuntimeException("Couldn't login user, try again");
            }
            return new Gson().fromJson(response.body(), AuthData.class);
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    public void logoutUser(LogoutRequest req) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/session/"))
                .method("DELETE", HttpRequest.BodyPublishers.noBody())
                .header("authorization", req.authToken())
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!codeIsGood(response.statusCode())) {
                throw new RuntimeException("Couldn't logout user, try again");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    public CreateGameResponse createGame(CreateGameRequest req) throws Exception {
        if (req.gameName() == null) {
            throw new IllegalArgumentException("Error: Incorrect number of arguments");
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("authorization", req.authToken())
                .method("POST", HttpRequest.BodyPublishers
                        .ofString(new Gson().toJson(new CreateGameInput(req.gameName()))))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!codeIsGood(response.statusCode())) {
                throw new RuntimeException("Couldn't create game, try again");
            }
            return new Gson().fromJson(response.body(), CreateGameResponse.class);
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    public Collection<ShowGamesResponse> listGames(String authToken) throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("authorization", authToken)
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            Type type = new TypeToken<Map<String, Collection<ShowGamesResponse>>>() {}.getType();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!codeIsGood(response.statusCode())) {
                throw new RuntimeException("Couldn't access list of games, please try again");
            }
            Map<String, Collection<ShowGamesResponse>> games = new Gson().fromJson(response.body(), type);
            return games.get("games");
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    public void joinGame(String authToken, int gameId, String playerColor) throws  Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + "/game"))
                .header("authorization", authToken)
                .method("PUT", HttpRequest.BodyPublishers
                        .ofString(new Gson().toJson(new JoinRequest(playerColor, gameId))))
                .setHeader("Content-Type", "application/json")
                .build();
        try {
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (!codeIsGood(response.statusCode())) {
                throw new RuntimeException("Couldn't join/observe game, please try again");
            }
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }

    private boolean codeIsGood(int code) {
        return code/100 == 2;

    }
}
