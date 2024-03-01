package server;

import chess.ChessGame;
import chess.ChessPiece;
import com.google.gson.Gson;
import dataAccess.memoryDao.MemoryAuthDao;
import dataAccess.memoryDao.MemoryGameDao;
import dataAccess.memoryDao.MemoryUserDao;
import dataAccess.interfaceDao.AuthDao;
import dataAccess.interfaceDao.GameDao;
import dataAccess.interfaceDao.UserDao;
import handler.GameHandler;
import handler.SystemHandler;
import handler.UserHandler;
import spark.*;

public class Server {

    private final AuthDao authDao = new MemoryAuthDao();
    private final UserDao userDao = new MemoryUserDao();
    private final GameDao gameDao = new MemoryGameDao();

    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);
        Spark.staticFiles.location("web");
        Spark.init();

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
