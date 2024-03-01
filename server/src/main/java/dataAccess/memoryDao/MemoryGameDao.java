package dataAccess.memoryDao;

import dataAccess.interfaceDao.GameDao;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDao implements GameDao {

    Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void createGame(GameData gameData) {
        games.put(gameData.getGameID(), gameData);
    }

    @Override
    public GameData getGame(Integer gameID) {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> getAllGames() {
        return games.values();
    }

    @Override
    public void updateGame(GameData gameData) {
        games.put(gameData.getGameID(), gameData);
    }

    @Override
    public void deleteGame(GameData gameData) {
        games.remove(gameData.getGameID());
    }

    @Override
    public void clear() {
        games.clear();
    }

    @Override
    public Integer nextGameID() {
        var gameIDs = games.keySet().toArray();
        if (gameIDs.length == 0) return 1;
        return (Integer) gameIDs[gameIDs.length - 1] + 1;
    }

}
