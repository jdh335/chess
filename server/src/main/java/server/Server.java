package server;

import com.google.gson.Gson;
import dataAccess.exception.DataAccessException;
import dataAccess.SQLDao.SQLAuthDao;
import dataAccess.SQLDao.SQLGameDao;
import dataAccess.SQLDao.SQLUserDao;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.GameDao;
import dataAccess.interfaceDao.UserDao;
import handler.GameHandler;
import handler.SystemHandler;
import handler.UserHandler;
import spark.*;

public class Server {


    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }

            var serverPort = new Server().run(port);
            System.out.printf("Server started on port %d%n", serverPort);
            return;
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        System.out.println("""
                Pet Server:
                java ServerMain <port> [<dburl> <dbuser> <dbpassword> <dbname>]
                """);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.init();

        AuthDao authDao = null;
        UserDao userDao = null;
        GameDao gameDao = null;
        try {
            authDao = new SQLAuthDao();
            userDao = new SQLUserDao();
            gameDao = new SQLGameDao();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }

        UserHandler userHandler = new UserHandler(userDao, authDao);
        GameHandler gameHandler = new GameHandler(userDao, authDao, gameDao);
        SystemHandler systemHandler = new SystemHandler(userDao, authDao, gameDao);

        Spark.post("/user", userHandler::registerUser);
        Spark.post("/session", userHandler::loginUser);
        Spark.delete("/session", userHandler::logoutUser);

        Spark.get("/game", gameHandler::listGames);
        Spark.post("/game", gameHandler::createGame);
        Spark.put("/game", gameHandler::joinGame);

        Spark.delete("/db", systemHandler::clearApplication);
        Spark.exception(Exception.class, this::exceptionHandler);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
    }

    private void exceptionHandler(Exception e, Request req, Response res) {

        switch (e.getClass().getSimpleName()) {
            case "BadRequestException":
                res.status(400);
                break;
            case "UnauthorizedException":
                res.status(401);
                break;
            case "AlreadyTakenException":
                res.status(403);
                break;
            default:
                res.status(500);
        }
        res.body(new Gson().toJson(new ExceptionMessage("Error: " + e.getMessage())));
    }

    record ExceptionMessage(String message) {}

}
