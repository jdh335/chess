package dataAccess.memoryDao;

import dataAccess.exception.DataAccessException;
import dataAccess.interfaceDao.UserDao;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDao implements UserDao {

    Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        users.put(user.getUsername(), user);
    }

//    @Override
//    public void updateUser(UserData user) throws DataAccessException  {
//        users.put(user.getUsername(), user);
//    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return users.get(username);
    }
}








