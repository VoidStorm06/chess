package model;

public record ShowGamesResponse(int gameID, String whiteUsername, String blackUsername,
                                String gameName) {
}
