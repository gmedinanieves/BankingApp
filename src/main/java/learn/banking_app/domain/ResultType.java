package learn.banking_app.domain;

public enum ResultType {

    SUCCESS(0),
    INVALID(1),
    NOT_FOUND(2);

    private final int priority;

    ResultType(int priority) {
        this.priority = priority;
    }

    public int getPriority() {
        return priority;
    }

}
