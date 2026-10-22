package service;

import chess.ChessGame;

public record JoinRequest(String playerColor, Integer gameID) {
}
