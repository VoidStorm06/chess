package service;

import chess.ChessGame;
import dataaccess.MemoryDAO;
import model.AuthData;
import model.UserData;

public class Services {

    final private MemoryDAO DAO;

    public Services(MemoryDAO DAO) {
        this.DAO = DAO;
    }

    public AuthData registerUser(UserData user) {
        DAO.createUser(user);
        return DAO.createAuth(user);
    }

    public void logIn() {

    }

    public void logOut() {

    }

    public void createNewGame(ChessGame game) {

    }

    public void viewAllGames() {

    }

    public void joinCurrGame(int gameID) {

    }
}


