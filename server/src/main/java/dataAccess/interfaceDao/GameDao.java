package dataAccess.interfaceDao;

import dataAccess.exception.DataAccessException;
import model.GameData;

import java.util.Collection;

public interface GameDao {

    void createGame(GameData gameData) throws DataAccessException;

    GameData getGame(Integer integer) throws DataAccessException;
    Collection<GameData> getAllGames() throws DataAccessException;

    void updateGame(GameData gameData) throws DataAccessException;

    void deleteGame(GameData gameData);
    void clear() throws DataAccessException;

    Integer nextGameID() throws DataAccessException;


}
