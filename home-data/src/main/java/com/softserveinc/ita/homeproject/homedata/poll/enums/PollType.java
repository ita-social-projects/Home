package com.softserveinc.ita.homeproject.homedata.poll.enums;

public enum PollType {
    SIMPLE("simple"),

    MAJOR("major");

    private final String value;

    PollType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
