package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class PollTypeAttributeConverter implements AttributeConverter<PollType, String> {
    @Override
    public String convertToDatabaseColumn(PollType attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case SIMPLE:
                return "simple";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public PollType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "simple":
                return PollType.SIMPLE;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
