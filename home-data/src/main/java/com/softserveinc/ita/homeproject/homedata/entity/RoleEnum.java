package com.softserveinc.ita.homeproject.homedata.entity;

public enum RoleEnum {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner");

    private final String name;

    RoleEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
