package server;

public class Command {
    public String getType() {
        return type.toLowerCase();
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    private String type;
    private String key;
    private String value;
}
