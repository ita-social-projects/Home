package com.softserveinc.ita.homeproject.homeservice.dto;

public enum RoleDto {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner");

    public String nameRole;

    RoleDto(String nameRole) {
        this.nameRole = nameRole;
    }

    public String getNameRole() {
        return nameRole;
    }
}
