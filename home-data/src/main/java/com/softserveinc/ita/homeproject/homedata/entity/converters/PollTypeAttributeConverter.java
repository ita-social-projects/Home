package com.softserveinc.ita.homeproject.homedata.entity.converters;

import javax.persistence.AttributeConverter;

import com.softserveinc.ita.homeproject.homedata.entity.poll.PollType;

public class PollTypeAttributeConverter implements AttributeConverter<PollType, String> {
    @Override
    public String convertToDatabaseColumn(PollType attribute) {
        if (attribute == null) {
            return null;
        } else {
            return attribute.name();
        }
    }

    @Override
    public PollType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try {
                return PollType.valueOf(dbData);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(dbData + " not supported.");
            }
        }
    }
}
