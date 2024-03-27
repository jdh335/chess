package ui;

import chess.ChessBoard;
import client.GenerateBoard;
import client.Repl;
import client.RequestException;
import client.ServerException;
import model.GameData;
import serverFacade.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;


public class PostLoginUI implements ClientUI {


    private final ServerFacade server;


    public PostLoginUI(ServerFacade serverFacade, Repl repl) {
        this.server = serverFacade;
    }

    @Override
    public String eval(String input) throws ServerException {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "create" -> create(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observer" -> join("observe");
                case "logout" -> logout();
                default -> help();
            };
        } catch (RequestException e) {
            return SET_TEXT_COLOR_YELLOW + e.getMessage();
        }
    }

    public String create(String... params) throws ServerException, RequestException {
        if (params.length < 1) { throw new RequestException(CREATE_CMD); }

        Integer gameID = server.createGame(params[0]);
        return String.format("  > New game \"%s\" [%d] created\n", params[0], gameID);
    }

    public String list() throws ServerException {
        StringBuilder allGames = new StringBuilder();
        GameData[] games = server.listGames();
        for (GameData game : games) {
            allGames.append(String.format("  > Game %d: ", game.getGameID()));
            allGames.append(game.getGameName());
            allGames.append(String.format(" White: %s ", (game.getWhiteUsername() == null) ? "[EMPTY]" : game.getWhiteUsername()));
            allGames.append(String.format("Black: %s ", (game.getBlackUsername() == null) ? "[EMPTY]" : game.getBlackUsername()));
            allGames.append("\n");
        }
        return allGames.toString();
        //throw new ResponseException(400, LIST_CMD);
    }

    public String join(String... params) throws ServerException, RequestException {
        if (params.length < 2) { throw new RequestException(JOIN_CMD); }

        server.joinGame(params[1], Integer.parseInt(params[0]));
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        GenerateBoard.drawChessBoard(board, "WHITE");

        return String.format("  > You joined Game:%d as %s Team\n", Integer.parseInt(params[0]), params[1].toUpperCase());

    }

    public String logout(String... params) throws ServerException, RequestException {

        server.logoutUser();
        Repl.SIGNED_IN = false;
//        return String.format("You rescued %s. Assigned ID: %d", pet.name(), pet.id());
//        throw new RequestException(LOGOUT_CMD);
        return "";
    }

    public String quit() throws ServerException {

        return "quit";
    }

    @Override
    public String help() {
        return  CREATE_CMD + LIST_CMD + JOIN_CMD + OBSERVE_CMD + LOGOUT_CMD + HELP_CMD;
    }

}
