package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.converters;

import javax.persistence.AttributeConverter;

import com.softserveinc.ita.homeproject.homedata.user.password.enums.PasswordRecoveryTokenStatus;


public class PasswordRecoveryTokenStatusAttributeConverter
    implements AttributeConverter<PasswordRecoveryTokenStatus, String> {

    @Override
    public String convertToDatabaseColumn(PasswordRecoveryTokenStatus attribute) {
        return attribute == null ? null : attribute.getValue();
    }

    @Override
    public PasswordRecoveryTokenStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        } else {
            try {
                return PasswordRecoveryTokenStatus.getEnum(dbData);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException(dbData + " not supported.");
            }
        }
    }
}
