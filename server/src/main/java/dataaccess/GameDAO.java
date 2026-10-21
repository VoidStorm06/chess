package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    GameData makeGame();
    Collection<GameData> readGames();
    GameData updateGame();
    void deleteGame();
}
