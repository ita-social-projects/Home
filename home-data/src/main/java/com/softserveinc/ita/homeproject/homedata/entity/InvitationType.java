package com.softserveinc.ita.homeproject.homedata.entity;

public enum InvitationType {

    APARTMENT("apartment"),

    COOPERATION("cooperation");

    private String value;

    InvitationType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
