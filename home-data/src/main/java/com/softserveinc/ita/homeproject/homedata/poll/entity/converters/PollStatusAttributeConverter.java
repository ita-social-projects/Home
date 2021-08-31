package com.softserveinc.ita.homeproject.homedata.poll.entity.converters;

import javax.persistence.AttributeConverter;

import com.softserveinc.ita.homeproject.homedata.poll.entity.PollStatus;

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
