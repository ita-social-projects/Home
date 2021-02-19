package com.softserveinc.ita.homeproject.homedata.entity;

public enum ContactType {
    PHONE("phone"),
    EMAIL("email");

    private String value;

    ContactType(String value) {
        this.value = value;
    }
}
