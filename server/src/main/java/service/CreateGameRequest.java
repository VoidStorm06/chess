package service;

import model.AuthData;

public record CreateGameRequest(String authToken, String gameName) {
}
