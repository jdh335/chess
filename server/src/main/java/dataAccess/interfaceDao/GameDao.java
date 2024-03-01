package dataAccess.interfaceDao;

import model.GameData;

import java.util.Collection;

public interface GameDao {

    void createGame(GameData gameData);

    GameData getGame(Integer integer);
    Collection<GameData> getAllGames();

    void updateGame(GameData gameData);

    void deleteGame(GameData gameData);
    void clear();

    Integer nextGameID();


}
