package ui;

import client.ServerException;

public interface ClientUI {

    String REGISTER_CMD = "  > Register [username][password][email] --- create an account\n";
    String LOGIN_CMD = "  > Login [username][password] ------------- sign in as a user\n";
    String QUIT_CMD = "  > Quit ----------------------------------- leave the application\n";
    String HELP_CMD = "  > Help ----------------------------------- display help commands\n";
    String CREATE_CMD = "  > create [name] -------------------------- make a new game\n";
    String LIST_CMD = "  > list ----------------------------------- display game data\n";
    String JOIN_CMD = "  > join [id][color] ----------------------- play an existing game\n";
    String OBSERVE_CMD = "  > observe [id] --------------------------- watch an existing game\n";
    String LOGOUT_CMD = "  > logout --------------------------------- leave the the options\n";

    String eval(String line) throws ServerException;

    String help();

}

