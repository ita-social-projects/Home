package com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.converters;

import javax.persistence.AttributeConverter;

import com.softserveinc.ita.homeproject.homedata.cooperation.entity.invitation.InvitationStatus;

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
