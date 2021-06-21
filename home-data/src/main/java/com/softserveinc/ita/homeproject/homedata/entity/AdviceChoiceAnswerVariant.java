package com.softserveinc.ita.homeproject.homedata.entity;

public enum AdviceChoiceAnswerVariant {

    FOR("for"),

    AGAINST("against"),

    ABSTAINED("abstained"),

    FREE_ANSWER("free_answer");

    private final String value;

    AdviceChoiceAnswerVariant(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
