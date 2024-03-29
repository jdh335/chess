package handler;

import com.google.gson.Gson;
import dataAccess.exception.AlreadyTakenException;
import dataAccess.exception.BadRequestException;
import dataAccess.exception.DataAccessException;
import dataAccess.exception.UnauthorizedException;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.GameDao;
import dataAccess.interfaceDao.UserDao;
import model.GameData;
import service.GameService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class GameHandler {

    private final GameService gameService;

    public GameHandler(UserDao userDao, AuthDao authDao, GameDao gameDao) {
        this.gameService = new GameService(userDao, authDao, gameDao);
    }

    public Object listGames(Request req, Response res) throws DataAccessException, UnauthorizedException {
        String authToken = req.headers("Authorization");

        record games(GameData[] games) {}
        Collection<GameData> gameData = gameService.listGames(authToken);

        res.status(200);
        return new Gson().toJson(new games(gameData.toArray(new GameData[0])));
    }

    public Object createGame(Request req, Response res) throws DataAccessException, UnauthorizedException {
        String authToken = req.headers("Authorization");
        record GameName (String gameName) {}
        GameName gameName = new Gson().fromJson(req.body(), GameName.class);
        record GameID(Integer gameID) {}
        Integer gameID = gameService.createGame(authToken, gameName.gameName());

        res.status(200);
        return new Gson().toJson(new GameID(gameID));
    }

    public Object joinGame(Request req, Response res) throws DataAccessException, UnauthorizedException, BadRequestException, AlreadyTakenException {
        String authToken = req.headers("Authorization");
        record PlayerData(String playerColor, Integer gameID) {}
        PlayerData playerData = new Gson().fromJson(req.body(), PlayerData.class);

        gameService.joinGame(authToken, playerData.playerColor().toUpperCase(), playerData.gameID());

        res.status(200);
        return "";
    }


}

