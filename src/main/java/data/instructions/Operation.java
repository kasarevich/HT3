package data.instructions;

public class Operation {
    private TypesOfOperation type;
    private String value;
    private int timeout;

    public Operation() {
    }

    public Operation(TypesOfOperation type, String value) {
        this.type = type;
        this.value = value;
        this.timeout = -1;
    }

    public Operation(TypesOfOperation type, String value, int timeout) {
        this.type = type;
        this.value = value;
        this.timeout = timeout;
    }

    public TypesOfOperation getType() {
        return type;
    }

    public void setType(TypesOfOperation type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}
