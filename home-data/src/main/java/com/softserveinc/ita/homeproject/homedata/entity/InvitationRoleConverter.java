package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InvitationRoleConverter implements AttributeConverter<RoleEnum, String> {

    @Override
    public String convertToDatabaseColumn(RoleEnum attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case ADMIN:
                return "admin";
            case USER:
                return "user";
            case OWNER:
                return "owner";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public RoleEnum convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "admin":
                return RoleEnum.ADMIN;
            case "user":
                return RoleEnum.USER;
            case "owner":
                return RoleEnum.OWNER;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
