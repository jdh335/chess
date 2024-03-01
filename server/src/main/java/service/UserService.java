package service;

import dataAccess.exception.AlreadyTakenException;
import dataAccess.exception.BadRequestException;
import dataAccess.exception.DataAccessException;
import dataAccess.exception.UnauthorizedException;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.UserDao;
import model.AuthData;
import model.UserData;

public class UserService {

    private UserDao userDao;
    private AuthDao authDao;

    public UserService(UserDao userDao, AuthDao authDao) {
        this.userDao = userDao;
        this.authDao = authDao;
    }

    public AuthData register(UserData request) throws dataAccess.exception.DataAccessException, BadRequestException, AlreadyTakenException {
        if (request.getUsername() == null || request.getPassword() == null || request.getEmail() == null) {
            throw new BadRequestException();
        }

        UserData user = userDao.getUser(request.getUsername());
        if (user != null) throw new AlreadyTakenException();
        userDao.createUser(request);

        return authDao.createAuth(request.getUsername());
    }

    public AuthData login(UserData request) throws dataAccess.exception.DataAccessException, UnauthorizedException {
        UserData user = userDao.getUser(request.getUsername());
        if (user == null || !user.getPassword().equals(request.getPassword())) {
            throw new UnauthorizedException();
        }
        return authDao.createAuth(request.getUsername());
    }

    public void logout(String authToken) throws dataAccess.exception.DataAccessException, UnauthorizedException {
        verifySession(authToken);
        authDao.deleteAuth(authToken);
    }

    public AuthData verifySession(String authToken) throws DataAccessException, UnauthorizedException {
        AuthData authData = authDao.getAuth(authToken);
        if (authData == null) throw new UnauthorizedException();
        return authData;
    }

}