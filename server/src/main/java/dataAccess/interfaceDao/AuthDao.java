package dataAccess.interfaceDao;

import model.AuthData;

public interface AuthDao {

    void clear();

    AuthData createAuth(String username);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);


}

