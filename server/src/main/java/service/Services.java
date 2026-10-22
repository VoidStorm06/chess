package service;

import chess.ChessGame;
import dataaccess.DataAccessException;
import dataaccess.MemoryDAO;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

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
        AuthData authToken = new AuthData("", logoutRequest.authToken());
        validAuthToken(authToken);
        DAO.deleteAuth(authToken);
    }

    public CreateGameResponse createNewGame(CreateGameRequest createGameRequest) throws DataAccessException {
        AuthData authToken = new AuthData("", createGameRequest.authToken());
        validAuthToken(authToken);
        int gameID = DAO.createNewGame(createGameRequest.gameName());
        return new CreateGameResponse(gameID);

    }

    public Collection<ShowGamesResponse> viewAllGames(String request) throws DataAccessException {
        AuthData authToken = new AuthData("", request);
        validAuthToken(authToken);
        var gameList = DAO.listAllGames();
        ArrayList<ShowGamesResponse> games = new ArrayList<>();
        for (var value : gameList.values()) {
            ShowGamesResponse response = new ShowGamesResponse(value.gameID(),
                    value.whiteUsername(), value.blackUsername(), value.gameName());
            games.add(response);
        }
        return games;
    }

    public void joinCurrGame(String authToken, JoinRequest request)  throws DataAccessException {
        AuthData authData = new AuthData("", authToken);
        validAuthToken(authData);
        GameData game = DAO.getGame(request.gameID());
        if (game == null) {
            throw new DataAccessException("bad request");
        }
        GameData updateGame;
        if (request.playerColor().equals("BLACK") && game.blackUsername().isEmpty()) {
            updateGame = new GameData(request.gameID(), game.whiteUsername(),
                    DAO.getAuth(authData).username(), game.gameName(), game.game());
        } else if (request.playerColor().equals("WHITE") && game.whiteUsername().isEmpty()) {
            updateGame = new GameData(request.gameID(), DAO.getAuth(authData).username(),
                    game.blackUsername(), game.gameName(), game.game());
        } else {
            throw new DataAccessException("bad request");
        }
        DAO.updateGame(updateGame);
    }

    public void deleteDatabases()  throws DataAccessException {
        DAO.clearAllDatabases();
    }

    public void validAuthToken(AuthData authToken) throws DataAccessException {
        if (DAO.getAuth(authToken) == null) {
            throw new DataAccessException("unauthorized");
        }
    }
}


