package service;

import chess.ChessGame;

public record ShowGamesResponse(int gameID, String whiteUsername, String blackUsername,
                                String gameName) {
}
