package ui;

import client.Repl;
import client.RequestException;
import client.ServerException;
import model.UserData;
import serverFacade.ServerFacade;

import java.util.Arrays;

import static ui.EscapeSequences.SET_TEXT_COLOR_YELLOW;

public class PreLoginUI implements ClientUI {

    private final ServerFacade server;

    public PreLoginUI(ServerFacade serverFacade, Repl repl) {
        this.server = serverFacade;
    }

    @Override
    public String eval(String input) throws ServerException {
        try {
            String[] tokens = input.split(" ");
            String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> quit();
                default -> help();
            };
        } catch (RequestException e) {
            return SET_TEXT_COLOR_YELLOW + e.getMessage();
        }
    }

    public String register(String... params) throws ServerException, RequestException {
        if (params.length < 3) { throw new RequestException(REGISTER_CMD); }
        server.registerUser(new UserData(params[0], params[1], params[2]));
        Repl.SIGNED_IN = true;
        return String.format("  > Registered as \"%s\"\n", params[0]);
    }

    public String login(String... params) throws ServerException, RequestException {
        if (params.length < 2) { throw new RequestException(LOGIN_CMD); }
        server.loginUser(new UserData(params[0], params[1], null));
        Repl.SIGNED_IN = true;
        return String.format("  > Logged in as \"%s\"\n", params[0]);

    }

    public String quit() {
        return "quit";
    }

    @Override
    public String help() {
        return  REGISTER_CMD + LOGIN_CMD + QUIT_CMD + HELP_CMD;
    }

}
