package dataaccess;

import model.AuthData;
import model.GameData;
import model.UserData;

import java.util.Collection;

public interface DAO {
    void clearAllDatabases();
    AuthData createUser(String username, String password, String email);
    UserData getUser(String username);
    int createNewGame();
    Collection<GameData> listAllGames();
    void updateGame();
    void joinGame();
    AuthData createAuth();
    AuthData getAuth(String username);
    void deleteAuth(AuthData authData);

}
