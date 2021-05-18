package com.softserveinc.ita.homeproject.homeservice.dto;

public enum RoleDto {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner");

    private final String nameRole;

    RoleDto(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getNameRole() {
        return nameRole;
    }

}
