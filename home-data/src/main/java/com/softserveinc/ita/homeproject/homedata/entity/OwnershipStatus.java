package com.softserveinc.ita.homeproject.homedata.entity;

public enum OwnershipStatus {
    ACTIVE("active"),
    INACTIVE("inactive");

    private String value;

    OwnershipStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
