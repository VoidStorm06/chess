package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.LoginRequest;
import model.UserData;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
            client.send(request, HttpResponse.BodyHandlers.ofString());
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
            return new Gson().fromJson(response.body(), AuthData.class);
        } catch (Exception ex) {
            throw new RuntimeException("Server Error: " + ex.getMessage());
        }
    }
}
