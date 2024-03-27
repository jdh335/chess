package serverFacade;

import client.ServerException;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.io.*;
import java.net.*;

public class ServerFacade {

    private final String serverUrl;

    private String authToken = null;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void registerUser(UserData user) throws ServerException {
        var path = "/user";
        authToken = this.makeRequest("POST", path, user, AuthData.class).getAuthToken();
    }

    public void loginUser(UserData user) throws ServerException {
        var path = "/session";
        authToken = this.makeRequest("POST", path, user, AuthData.class).getAuthToken();
    }

    public void logoutUser() throws ServerException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public GameData[] listGames() throws ServerException {
        var path = "/game";
        record listGameResponse(GameData[] games) {}
        var response = this.makeRequest("GET", path, null, listGameResponse.class);
        return response.games();
    }

    public Integer createGame(String gameName) throws ServerException {
        var path = "/game";
        record createGameResponse(Integer gameID) {}
        record createGameRequest(String gameName) {}
        return this.makeRequest("POST", path, new createGameRequest(gameName), createGameResponse.class).gameID;
    }

    public void joinGame(String playerColor, Integer gameID) throws ServerException {
        var path = "/game";
        record joinGameRequest(String playerColor, Integer gameID) {}
        this.makeRequest("PUT", path, new joinGameRequest(playerColor, gameID), null);
    }

    public void clearData() throws ServerException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ServerException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setRequestProperty("Authorization", authToken);
            http.setDoOutput(!method.equals("GET"));
            writeBody(request, http);

            http.connect();

            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ServerException(500, ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ServerException {
        var status = http.getResponseCode();
        if (status >= 400) {
            throw new ServerException(status, "failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

}