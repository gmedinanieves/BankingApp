package learn.banking_app.domain;

import java.util.ArrayList;
import java.util.List;

public class Result<T> {

    private final ArrayList<String> messages = new ArrayList<>();
    private ResultType type = ResultType.SUCCESS;
    private T payload;

    public boolean isSuccess() {
        return type == ResultType.SUCCESS;
    }

    public ResultType getType() {
        return type;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }

    public List<String> getMessages() {
        return new ArrayList<>(messages);
    }

    public void addMessage(String message, ResultType type) {
        messages.add(message);
        // Multiple errors can occur, but the API must return a single HTTP response.
        // Priority allows us to consistently choose the most severe error.
        if (this.type == null || type.getPriority() > this.type.getPriority()) {
            this.type = type;
        }
    }

}
