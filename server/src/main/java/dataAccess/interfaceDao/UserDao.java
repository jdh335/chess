package dataAccess.interfaceDao;

import dataAccess.exception.DataAccessException;
import model.UserData;

public interface UserDao {

    void clear();

    void createUser(UserData user) throws DataAccessException;

    UserData getUser(String username) throws DataAccessException;



}
