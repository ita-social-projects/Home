package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class InvitationStatusAttributeConverter implements AttributeConverter<InvitationStatus, String> {
    @Override
    public String convertToDatabaseColumn(InvitationStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public InvitationStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try {
                return InvitationStatus.getEnum(dbData);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(dbData + " not supported.");
            }
        }
    }
}
