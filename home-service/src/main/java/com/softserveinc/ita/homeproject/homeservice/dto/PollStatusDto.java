package com.softserveinc.ita.homeproject.homeservice.dto;

public enum PollStatusDto {
    DRAFT("draft"),

    ACTIVE("active"),

    COMPLETED("completed"),

    SUSPENDED("suspended");

    private final String value;

    PollStatusDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
