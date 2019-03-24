package eg.org.taman.data;

import eg.org.taman.data.type.Type;

import java.util.Objects;

public class Message {

    private String message;
    private Type type;

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        if(Objects.isNull(message) || message.isEmpty())
            throw new IllegalArgumentException("Invalid content; message should not be Null, or empty!");
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
}
