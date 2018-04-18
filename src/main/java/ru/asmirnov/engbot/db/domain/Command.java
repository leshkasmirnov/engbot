package ru.asmirnov.engbot.db.domain;

import java.util.stream.Stream;

public enum Command {

    ADD("/add"),
    START("/start");

    private String cmd;

    Command (String cmd) {
        this.cmd = cmd;
    }

    @Override
    public String toString() {
        return cmd;
    }

    public static Command recognize(String cmd) {
        return Stream.of(values()).filter(c -> c.toString().equals(cmd)).findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown command: " + cmd));
    }

}
