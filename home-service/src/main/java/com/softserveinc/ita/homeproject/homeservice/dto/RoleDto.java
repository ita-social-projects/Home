package com.softserveinc.ita.homeproject.homeservice.dto;

public enum RoleDto {

    ADMIN("admin"),
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
