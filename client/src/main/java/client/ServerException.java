package client;

public class ServerException extends Exception {
    final private int statusCode;

    public ServerException(int statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public int StatusCode() {
        return statusCode;
    }
}