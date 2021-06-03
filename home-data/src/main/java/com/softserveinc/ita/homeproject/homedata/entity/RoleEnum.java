package com.softserveinc.ita.homeproject.homedata.entity;

import java.util.Arrays;

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

    public static RoleEnum getEnum(String value) {
        return Arrays.stream(RoleEnum.values()).filter(m -> m.name.equals(value)).findAny().orElse(null);
    }

}
