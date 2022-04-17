package com.softserveinc.ita.homeproject.homeservice.dto.poll.enums;

public enum PollTypeDto {
    SIMPLE("simple"),

    MAJOR("major");

    private final String value;

    PollTypeDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
