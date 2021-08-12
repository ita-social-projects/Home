package com.softserveinc.ita.homeproject.homedata.entity.polls.enums;

public enum  PollQuestionType {

    ADVICE("advice"),

    MULTIPLE_CHOICE("multiple_choice");

    private String value;

    PollQuestionType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
