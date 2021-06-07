package com.softserveinc.ita.homeproject.homedata.entity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RoleEnum {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner");

    private final static Map<String, RoleEnum> ROLES = Stream.of(RoleEnum.values())
            .collect(Collectors.toMap(RoleEnum::getName, Function.identity()));

    private final String name;

    RoleEnum(String name) {
        this.name = name;

    }

    public String getName() {
        return name;
    }

    public static RoleEnum getEnum(String value) {
        return ROLES.get(value);
    }

}
