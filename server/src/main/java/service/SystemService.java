package service;

import dataAccess.exception.DataAccessException;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.GameDao;
import dataAccess.interfaceDao.UserDao;

public class SystemService {

    UserDao userDao;

    AuthDao authDao;

    GameDao gameDao;

    public SystemService(UserDao userDao, AuthDao authDao, GameDao gameDao) {
        this.userDao = userDao;
        this.authDao = authDao;
        this.gameDao = gameDao;
    }
    public void clearData() throws DataAccessException {
        userDao.clear();
        authDao.clear();
        gameDao.clear();
    }


}
