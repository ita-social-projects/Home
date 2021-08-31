package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation;

public enum InvitationStatusDto {

    PENDING("pending"),

    PROCESSING("processing"),

    ACCEPTED("accepted"),

    DECLINED("declined"),

    DEACTIVATED("deactivated"),

    ERROR("error");


    private final String value;

    InvitationStatusDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
