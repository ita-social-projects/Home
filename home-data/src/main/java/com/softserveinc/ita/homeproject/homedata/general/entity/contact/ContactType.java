package com.softserveinc.ita.homeproject.homedata.general.entity.contact;

public enum ContactType {
  
    EMAIL("email"),

    PHONE("phone");

    private String value;

    ContactType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
