package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class InvitationStatusAttributeConverter implements AttributeConverter<InvitationStatus, String> {
    @Override
    public String convertToDatabaseColumn(InvitationStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public InvitationStatus convertToEntityAttribute(String dbData) {
        return dbData == null ? null : InvitationStatus.getEnum(dbData);
    }
}
