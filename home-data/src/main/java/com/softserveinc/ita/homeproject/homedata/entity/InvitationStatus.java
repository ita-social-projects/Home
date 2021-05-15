package com.softserveinc.ita.homeproject.homedata.entity;

public enum InvitationStatus {

    PENDING("pending"),

    PROCESSING("processing"),

    ACCEPTED("accepted"),

    DECLINED("declined"),

    ERROR("error");

    private String value;

    InvitationStatus(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
