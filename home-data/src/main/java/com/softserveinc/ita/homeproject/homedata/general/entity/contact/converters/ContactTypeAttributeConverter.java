package com.softserveinc.ita.homeproject.homedata.general.entity.contact.converters;

import javax.persistence.AttributeConverter;

import com.softserveinc.ita.homeproject.homedata.general.entity.contact.ContactType;

public class ContactTypeAttributeConverter implements AttributeConverter<ContactType, String> {
    @Override
    public String convertToDatabaseColumn(ContactType attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case EMAIL:
                return "email";
            case PHONE:
                return "phone";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public ContactType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "email":
                return ContactType.EMAIL;
            case "phone":
                return ContactType.PHONE;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
