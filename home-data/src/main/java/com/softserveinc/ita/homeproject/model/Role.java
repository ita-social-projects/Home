package com.softserveinc.ita.homeproject.model;

import lombok.Getter;
import lombok.Setter;

public enum Role {

    USER("user"),

    OWNER("owner"),

    ADMIN("admin"),

    EXECUTOR("executor");

    @Setter
    @Getter
    private String value;

    Role(String value) {
        this.value = value;
    }

}