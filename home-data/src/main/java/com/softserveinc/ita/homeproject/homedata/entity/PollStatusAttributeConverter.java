package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class PollStatusAttributeConverter implements AttributeConverter<PollStatus, String> {
    @Override
    public String convertToDatabaseColumn(PollStatus attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute.name();
        }
    }

    @Override
    public PollStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try {
                return PollStatus.valueOf(dbData);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(dbData + " not supported.");
            }
        }
    }
}
