package com.softserveinc.ita.homeproject.homeservice.dto;

public enum RoleDto {

    ADMIN("admin"),
    COOPERATION_ADMIN("cooperation_admin"),
    USER("user"),
    OWNER("owner");

    private final String name;

    RoleDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
