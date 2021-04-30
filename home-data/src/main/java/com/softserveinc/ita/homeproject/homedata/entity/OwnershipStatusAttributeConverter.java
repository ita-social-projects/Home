package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class OwnershipStatusAttributeConverter  implements AttributeConverter<OwnershipStatus, String> {
    @Override
    public String convertToDatabaseColumn(OwnershipStatus attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case ACTIVE:
                return "active";
            case INACTIVE:
                return "inactive";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public OwnershipStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "active":
                return OwnershipStatus.ACTIVE;
            case "inactive":
                return OwnershipStatus.INACTIVE;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
