package com.softserveinc.ita.homeproject.homedata.entity.polls.enums;

public enum PollType {
    SIMPLE("simple");

    private final String value;

    PollType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
