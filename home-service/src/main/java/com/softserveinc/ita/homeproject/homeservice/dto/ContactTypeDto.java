package com.softserveinc.ita.homeproject.homeservice.dto;

public enum ContactTypeDto {
  
    EMAIL("email"),

    PHONE("phone");

    private String value;

    ContactTypeDto(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
