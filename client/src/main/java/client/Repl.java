package client;

import client.ServerException;
import serverFacade.ServerFacade;
import ui.ClientUI;
import ui.PostLoginUI;
import ui.PreLoginUI;

import java.util.Scanner;

import static ui.EscapeSequences.*;


public class Repl {

    private final PreLoginUI preLoginUI;

    private final PostLoginUI postLoginUI;

    public static boolean SIGNED_IN = false;


    public Repl(String serverUrl) {
        ServerFacade serverFacade = new ServerFacade(serverUrl);
        preLoginUI = new PreLoginUI(serverFacade, this);
        postLoginUI = new PostLoginUI(serverFacade, this);
    }

    public void run() {
        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            ClientUI currentUI = (SIGNED_IN) ? postLoginUI : preLoginUI;
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = currentUI.eval(line);
                System.out.print(SET_TEXT_COLOR_MAGENTA + result);
            } catch (ServerException e) {
                System.out.print(SET_TEXT_COLOR_RED + "  > " + e.getMessage() + "\n");
            }
        }
        System.out.println();
    }

//    public void notify(Notification notification) {
//        System.out.println(RED + notification.message());
//        printPrompt();
//    }

    private void printPrompt() {
        String color = (SIGNED_IN) ? SET_TEXT_COLOR_GREEN : SET_TEXT_COLOR_WHITE;
        System.out.print(color + ">>> ");
    }

}