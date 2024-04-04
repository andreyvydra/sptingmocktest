package ru.antara.mock.data;

public enum Status {
    ERROR("Error"),
    SUCCESS("Success");

    private final String type;

    Status(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return type;
    }
}
