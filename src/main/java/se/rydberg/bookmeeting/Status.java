package se.rydberg.bookmeeting;

public enum Status {
    ACTIVE("aktiv"),
    INACTIVE("inte aktiv");

    private final String displayValue;

    Status(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
