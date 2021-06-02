package com.softserveinc.ita.homeproject.homedata.entity;

public enum InvitationStatus {

    PENDING("pending"),

    PROCESSING("processing"),

    ACCEPTED("accepted"),

    DECLINED("declined"),

    DEACTIVATED("deactivated"),

    ERROR("error");


    private final String value;

    InvitationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public String getValue() {
        return value;
    }

}
