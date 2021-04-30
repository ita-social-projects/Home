package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class InvitationStatusAttributeConverter implements AttributeConverter<InvitationStatus, String> {
    @Override
    public String convertToDatabaseColumn(InvitationStatus attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case PENDING:
                return "pending";
            case PROCESSING:
                return "processing";
            case ACCEPTED:
                return "accepted";
            case DECLINED:
                return "declined";
            case ERROR:
                return "error";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public InvitationStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "pending":
                return InvitationStatus.PENDING;
            case "processing":
                return InvitationStatus.PROCESSING;
            case "accepted":
                return InvitationStatus.ACCEPTED;
            case "declined":
                return InvitationStatus.DECLINED;
            case "error":
                return InvitationStatus.ERROR;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
