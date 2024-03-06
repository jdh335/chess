package dataAccess.interfaceDao;

import dataAccess.exception.DataAccessException;
import model.AuthData;

public interface AuthDao {

    void clear() throws DataAccessException;

    AuthData createAuth(String username) throws DataAccessException;

    AuthData getAuth(String authToken) throws DataAccessException;

    void deleteAuth(String authToken) throws DataAccessException;


}

