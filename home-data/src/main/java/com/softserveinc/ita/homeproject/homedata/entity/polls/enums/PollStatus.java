package com.softserveinc.ita.homeproject.homedata.entity.polls.enums;

public enum PollStatus {

    DRAFT("draft"),

    ACTIVE("active"),

    COMPLETED("completed"),

    SUSPENDED("suspended");

    private final String value;

    PollStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
