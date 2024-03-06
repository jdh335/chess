package service;

import chess.ChessGame;
import dataAccess.exception.AlreadyTakenException;
import dataAccess.exception.BadRequestException;
import dataAccess.exception.DataAccessException;
import dataAccess.exception.UnauthorizedException;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.GameDao;
import dataAccess.interfaceDao.UserDao;
import model.AuthData;
import model.GameData;

import java.util.Collection;

public class GameService {

    UserDao userDao;

    AuthDao authDao;

    GameDao gameDao;

    public GameService(UserDao userDao, AuthDao authDao, GameDao gameDao) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }

    public Collection<GameData> listGames(String authToken) throws DataAccessException, UnauthorizedException {
        verifySession(authToken);
        return gameDao.getAllGames();
    }

    public Integer createGame(String authToken, String gameName) throws DataAccessException, UnauthorizedException {
        verifySession(authToken);

        Integer gameID = gameDao.nextGameID();
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());

        gameDao.createGame(gameData);
        return gameID;
    }

    public void joinGame(String authToken, String playerColor, Integer gameID) throws DataAccessException, BadRequestException, AlreadyTakenException, UnauthorizedException {
        AuthData authData = verifySession(authToken);
        GameData oldGame = gameDao.getGame(gameID);
        if (oldGame == null) throw new BadRequestException();
        if (playerColor == null) return;

        String whiteUsername = oldGame.getWhiteUsername();
        String blackUsername = oldGame.getBlackUsername();

        if (playerColor.equals("WHITE") ) {
            if (whiteUsername == null) {
                whiteUsername = authData.getUsername();
            } else {
                throw new AlreadyTakenException();
            }

        } else if (playerColor.equals("BLACK")){
            if (blackUsername == null) {
                blackUsername = authData.getUsername();
            } else {
                throw new AlreadyTakenException();
            }
        }
        GameData gameData = new GameData(gameID, whiteUsername, blackUsername, oldGame.getGameName(), oldGame.getGame());
        gameDao.updateGame(gameData);
    }

    public AuthData verifySession(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDao.getAuth(authToken);
        if (authData == null) throw new UnauthorizedException();
        return authData;
    }

}
