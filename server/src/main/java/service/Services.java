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
        if (user.password() == null || user.email()==null || user.username() == null) {
            throw new DataAccessException("bad request");
        }
        if (DAO.getUser(user) != null)  {
            throw new DataAccessException("Already taken");
        }
        DAO.createUser(user);
        return DAO.createAuth(user);
    }

    public AuthData logIn(LoginRequest loginRequest) throws DataAccessException {
        if (loginRequest.password() == null || loginRequest.username() == null) {
            throw new DataAccessException("bad request");
        }
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
        if (createGameRequest.gameName() == null) {
            throw new DataAccessException("bad request");
        }
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
        if (request.gameID() == null || request.playerColor() == null) {
            throw new DataAccessException("bad request");
        }
        AuthData authData = new AuthData("", authToken);
        validAuthToken(authData);
        GameData game = DAO.getGame(request.gameID());
        if (game == null) {
            throw new DataAccessException("bad request");
        }
        GameData updateGame;
        if (request.playerColor().equals("BLACK")) {
            if (game.blackUsername() == null) {
                updateGame = new GameData(request.gameID(), game.whiteUsername(),
                        DAO.getAuth(authData).username(), game.gameName(), game.game());
            } else {
                throw new DataAccessException("Already taken");
            }
        } else if (request.playerColor().equals("WHITE")) {
            if (game.whiteUsername() == null) {
                updateGame = new GameData(request.gameID(), DAO.getAuth(authData).username(),
                        game.blackUsername(), game.gameName(), game.game());
            } else {
                throw new DataAccessException("Already taken");
            }
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


