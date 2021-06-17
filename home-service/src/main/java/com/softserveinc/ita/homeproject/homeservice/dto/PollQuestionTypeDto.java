package com.softserveinc.ita.homeproject.homeservice.dto;

public enum PollQuestionTypeDto {

    ADVICE("advice"),

    MULTIPLE_CHOICE("multiple_choice");

    private String value;

    PollQuestionTypeDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
