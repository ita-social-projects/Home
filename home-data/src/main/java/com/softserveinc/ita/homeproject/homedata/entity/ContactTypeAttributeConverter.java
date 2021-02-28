package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class ContactTypeAttributeConverter implements AttributeConverter<ContactType, String> {
    @Override
    public String convertToDatabaseColumn(ContactType attribute) {
        if (attribute == null)
            return null;

        switch (attribute) {
            case CONTACTEMAIL:
                return "contactEmail";
            case CONTACTPHONE:
                return "contactPhone";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public ContactType convertToEntityAttribute(String dbData) {
        if (dbData == null)
            return null;

        switch (dbData) {
            case "contactEmail":
                return ContactType.CONTACTEMAIL;
            case "contactPhone":
                return ContactType.CONTACTPHONE;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
