package com.softserveinc.ita.homeproject.homedata.entity;

public enum ContactType {
  
    CONTACTEMAIL("contactEmail"),

    CONTACTPHONE("contactPhone");

    private String value;

    ContactType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
