package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class PollStatusAttributeConverter implements AttributeConverter<PollStatus, String> {
    @Override
    public String convertToDatabaseColumn(PollStatus attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case DRAFT:
                return "draft";
            case ACTIVE:
                return "active";
            case COMPLETED:
                return "completed";
            case SUSPENDED:
                return "suspended";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public PollStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "draft":
                return PollStatus.DRAFT;
            case "active":
                return PollStatus.ACTIVE;
            case "completed":
                return PollStatus.COMPLETED;
            case "suspended":
                return PollStatus.SUSPENDED;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
