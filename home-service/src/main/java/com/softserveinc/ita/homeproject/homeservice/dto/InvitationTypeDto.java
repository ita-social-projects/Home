package com.softserveinc.ita.homeproject.homeservice.dto;

public enum InvitationTypeDto {
    COOPERATION("cooperation"),
    APARTMENT("apartment");

    private String value;

    InvitationTypeDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(this.value);
    }

}
