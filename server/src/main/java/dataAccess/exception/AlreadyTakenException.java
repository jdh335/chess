package dataAccess.exception;

public class AlreadyTakenException extends Exception {
    public AlreadyTakenException() {
        super("already taken");
    }

}
