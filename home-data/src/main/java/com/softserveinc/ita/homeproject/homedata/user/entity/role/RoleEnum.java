package com.softserveinc.ita.homeproject.homedata.user.entity.role;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum RoleEnum {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner"),
    COOPERATION_ADMIN("cooperation_admin");


    private static final Map<String, RoleEnum> ROLES = Stream.of(RoleEnum.values())
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
