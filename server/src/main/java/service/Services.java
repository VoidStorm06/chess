package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import model.AuthData;
import model.UserData;

public class Services {

    final private MemoryDAO DAO;

    public Services(MemoryDAO DAO) {
        this.DAO = DAO;
    }

    public AuthData registerUser(UserData user) throws DataAccessException {
        if (DAO.getUser(user) != null)  {
            throw new DataAccessException("Already Taken");
        }
        DAO.createUser(user);
        return DAO.createAuth(user);
    }

    public AuthData logIn(LoginRequest loginRequest) throws DataAccessException {
        UserData existingUser = DAO.getUser(new UserData(loginRequest.username(),
                loginRequest.password(), ""));
        if (existingUser == null || !existingUser.password().equals(loginRequest.password()))  {
            throw new DataAccessException("unauthorized");
        }
        return DAO.createAuth(existingUser);
    }

    public void logOut(LogoutRequest logoutRequest)  throws DataAccessException {
        AuthData authToken = DAO.getAuth(new AuthData(logoutRequest.authToken(), ""));
        if (authToken == null) {
            throw new DataAccessException("unauthorized");
        }
        DAO.deleteAuth(authToken);
    }

    public void createNewGame(ChessGame game)  throws DataAccessException {

    }

    public void viewAllGames()  throws DataAccessException {

    }

    public void joinCurrGame(int gameID)  throws DataAccessException {

    }
    public void deleteDatabases()  throws DataAccessException {

    }
}


