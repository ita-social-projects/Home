package com.softserveinc.ita.homeproject.homeservice.dto.user.role;

public enum RoleDto {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner"),
    COOPERATION_ADMIN("cooperation_admin");

    private final String name;

    RoleDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
