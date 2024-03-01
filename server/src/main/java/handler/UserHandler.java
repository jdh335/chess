package handler;

import com.google.gson.Gson;
import dataAccess.exception.AlreadyTakenException;
import dataAccess.exception.BadRequestException;
import dataAccess.exception.UnauthorizedException;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.exception.DataAccessException;
import dataAccess.interfaceDao.UserDao;
import model.AuthData;
import model.UserData;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {

    private final UserService userService;

    public UserHandler(UserDao userDao, AuthDao authDao) {
        this.userService = new UserService(userDao, authDao);
    }

    public Object registerUser(Request req, Response res) throws DataAccessException, BadRequestException, AlreadyTakenException {
        UserData request = new Gson().fromJson(req.body(), UserData.class);

        AuthData authData = userService.register(request);

        res.status(200);
        return new Gson().toJson(authData);
    }

    public Object loginUser(Request req, Response res) throws DataAccessException, UnauthorizedException {
        UserData request = new Gson().fromJson(req.body(), UserData.class);

        AuthData authData = userService.login(request);

        res.status(200);
        return new Gson().toJson(authData);
    }

    public Object logoutUser(Request req, Response res) throws DataAccessException, UnauthorizedException {
        String authToken = req.headers("Authorization");

        userService.logout(authToken);

        res.status(200);
        return "";
    }
}
