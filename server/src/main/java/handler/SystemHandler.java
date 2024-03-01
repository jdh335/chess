package handler;

import dataAccess.exception.DataAccessException;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.GameDao;
import dataAccess.interfaceDao.UserDao;
import service.SystemService;
import spark.Request;
import spark.Response;

public class SystemHandler {
    private final SystemService systemService;

    public SystemHandler(UserDao userDao, AuthDao authDao, GameDao gameDao) {
        this.systemService = new SystemService(userDao, authDao, gameDao);
    }

    public Object clearApplication(Request req, Response res) throws DataAccessException {
        systemService.clearData();

        res.status(200);
        return "";
    }
}
