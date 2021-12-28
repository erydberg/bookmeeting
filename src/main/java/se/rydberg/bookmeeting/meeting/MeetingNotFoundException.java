package se.rydberg.bookmeeting.meeting;

public class MeetingNotFoundException extends Exception {
    private final String message;

    public MeetingNotFoundException(String msg) {
        this.message = msg;
    }

    public String getMessage(){
        return message;
    }
}
