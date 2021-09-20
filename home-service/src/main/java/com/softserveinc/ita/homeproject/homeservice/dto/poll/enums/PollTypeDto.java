package com.softserveinc.ita.homeproject.homeservice.dto.poll.enums;

public enum PollTypeDto {
    SIMPLE("simple");

    private final String value;

    PollTypeDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
