package com.softserveinc.ita.homeproject.homeservice.dto.user.role;

public enum RoleDto {

    ADMIN("admin"),
    USER("user"),
    OWNER("owner"),
    COOPERATION_ADMIN("cooperation_admin");

    private final String value;

    RoleDto(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
