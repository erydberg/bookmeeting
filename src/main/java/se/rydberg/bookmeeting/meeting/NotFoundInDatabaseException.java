package se.rydberg.bookmeeting.meeting;

public class NotFoundInDatabaseException extends Exception {
    private final String message;

    public NotFoundInDatabaseException(String msg) {
        this.message = msg;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
