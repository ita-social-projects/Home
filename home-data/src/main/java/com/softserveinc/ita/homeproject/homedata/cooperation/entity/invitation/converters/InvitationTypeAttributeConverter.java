package com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.converters;

import javax.persistence.AttributeConverter;

import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.InvitationType;

public class InvitationTypeAttributeConverter implements AttributeConverter<InvitationType, String> {

    @Override
    public String convertToDatabaseColumn(InvitationType attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case COOPERATION:
                return "cooperation";
            case APARTMENT:
                return "apartment";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public InvitationType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "cooperation":
                return InvitationType.COOPERATION;
            case "apartment":
                return InvitationType.APARTMENT;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
