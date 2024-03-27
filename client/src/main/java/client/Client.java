package client;

import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;
import client.Repl;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Scanner;


public class Client {

    private static AuthData authData;

    private static URI uri;

    private static final String preHelpMessage = "register <username> <password> <email> - to create an account" +
                                        "\nlogin <username> <password> - to play chess" + "\nquit - playing chess" +
                                        "\nhelp - with possible commands";
    private static final String postHelpMessage = "create <name> - to create a game" + "\nlist - games" +
                                        "\njoin <id> [white|black|null] - a game" + "\nobserve <id> - a game" +
                                        "\nlogout - when you are done" +"\nhelp - with possible commands";

    public static void main(String[] args) throws Exception {
        uri = new URI("http://localhost:8080");
        new Repl(uri.toString()).run();
//        new ServerFacade(uri.toURL().toString()).clearData();
    }

    private static void preLoginCommands() throws Exception {
        boolean promptUser = true;
        while(promptUser) {

            System.out.print("[LOGGED_OUT] >>> ");
            String[] userInput = new Scanner(System.in).nextLine().split(" ");

            switch(userInput[0].toLowerCase()) {
                case "help": //
                    System.out.println(preHelpMessage);
                    break;
                case "quit":
                    System.out.println("thanks for playing");
                    promptUser = false;
                    break;
                case "login":
                    if (userInput.length < 3) {
                        System.out.println("Invalid Login : login <username> <password>");
                        break;
                    }


                    String loginRequest = new Gson().toJson(new UserData(userInput[1], userInput[2], null));
                    HttpURLConnection loginHttp = sendRequest("/session", "POST", loginRequest, null);

                    if (loginHttp.getResponseCode() != 200) {
                        System.out.println(loginHttp.getResponseMessage());
                        break;
                    }

                    authData = readResponseBody(loginHttp, AuthData.class);
                    postLoginCommands(uri);

                    break;
                case "register":
                    if (userInput.length < 4) {
                        System.out.println("Invalid Register: register <username> <password> <email>");
                        break;
                    }

                    String registerRequest = new Gson().toJson(new UserData(userInput[1], userInput[2], userInput[3]));
                    HttpURLConnection registerHttp = sendRequest("/user", "POST", registerRequest, null);

                    if (registerHttp.getResponseCode() != 200) {
                        System.out.println(registerHttp.getResponseMessage());
                        break;
                    }

                    authData = readResponseBody(registerHttp, AuthData.class);
                    postLoginCommands(uri);

                    break;
                default:
                    System.out.println("Invalid Command \"" + userInput.toString() +"\" : Try again");
            }
        }
    }

    private static void postLoginCommands(URI uri) throws Exception {
        boolean promptUser = true;
        while(promptUser) {

            System.out.print("[LOGGED_IN] >>> ");
            String[] userInput = new Scanner(System.in).nextLine().split(" ");

            switch(userInput[0].toLowerCase()) {
                case "help":
                    System.out.println(postHelpMessage);
                    break;
                case "logout":

                    HttpURLConnection logoutHttp = sendRequest("/session", "DELETE", null, authData.getAuthToken());

                    if (logoutHttp.getResponseCode() != 200) {
                        System.out.println(logoutHttp.getResponseMessage());
                        break;
                    }

                    preLoginCommands();

                    break;
                case "create":
                    if (userInput.length < 2) {
                        System.out.println("Invalid Create: create <gameName>");
                        break;
                    }
                    String createRequest = new Gson().toJson(new GameName(userInput[1]));
                    HttpURLConnection createHttp = sendRequest("/game", "POST", createRequest, authData.getAuthToken());

                    if (createHttp.getResponseCode() != 200) {
                        System.out.println(createHttp.getResponseMessage());
                        break;
                    }
                    GameID gameID = readResponseBody(createHttp, GameID.class);
                    System.out.println("Game ID : " + gameID.gameID());
                    break;
                case "list":
                    HttpURLConnection listHttp = sendRequest("/game", "GET", null, authData.getAuthToken());

                    if (listHttp.getResponseCode() != 200) {
                        System.out.println(listHttp.getResponseMessage());
                        break;
                    }
                    Games listGames = readResponseBody(listHttp, Games.class);
                    StringBuilder gameBuilder = new StringBuilder();
                    for (GameData game : listGames.games()) {
                        gameBuilder.append("\tGame " + game.getGameID() + " : " + game.getGameName());
                        gameBuilder.append("\n\t\tWhite-user = " + game.getWhiteUsername());
                        gameBuilder.append(" & Black-user = " + game.getBlackUsername() + "\n");
                    }
                    System.out.println(gameBuilder);
                    break;
                case "join":
                    if (userInput.length < 3) {
                        System.out.println("Invalid Join: join <color> <gameID>");
                        break;
                    }
                    String joinRequest = new Gson().toJson(new PlayerData(userInput[1], Integer.parseInt(userInput[2])));
                    HttpURLConnection joinHttp = sendRequest("/game", "PUT", joinRequest, authData.getAuthToken());

                    if (joinHttp.getResponseCode() != 200) {
                        System.out.println(joinHttp.getResponseMessage());
                        break;
                    }
                    break;
                case "observe":
                    break;

            }
        }

    }

    private static HttpURLConnection sendRequest(String path, String method, String body, String header) throws Exception {
        HttpURLConnection http = (HttpURLConnection) URI.create(uri + path).toURL().openConnection();
        http.setRequestMethod(method);
        http.setRequestProperty("Authorization", header);
        if (body != null) { writeRequestBody(body, http); }
        http.connect();
        return http;
    }

    private static void writeRequestBody(String body, HttpURLConnection http) throws IOException {
        if (!body.isEmpty()) {
            http.setDoOutput(true);
            try (var outputStream = http.getOutputStream()) {
                outputStream.write(body.getBytes());
            }
        }
    }

    private static <T> T readResponseBody(HttpURLConnection http, Class<T> klass) throws IOException {
        T responseBody;
        try (InputStream respBody = http.getInputStream()) {
            InputStreamReader inputStreamReader = new InputStreamReader(respBody);
            responseBody = new Gson().fromJson(inputStreamReader, klass);
        }
        return responseBody;
    }

    record PlayerData(String playerColor, Integer gameID) {}
    record GameID(Integer gameID) {}
    record GameName (String gameName) {}
    record Games(GameData[] games) {}
}

