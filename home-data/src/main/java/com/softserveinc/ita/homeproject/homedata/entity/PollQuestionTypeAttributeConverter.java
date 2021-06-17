package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class PollQuestionTypeAttributeConverter implements AttributeConverter<PollQuestionType,String> {

    @Override
    public String convertToDatabaseColumn(PollQuestionType attribute) {
        if (attribute == null) {
            return null;
        }

        switch (attribute) {
            case ADVICE:
                return "advice";
            case MULTIPLE_CHOICE:
                return "multiple_choice";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public PollQuestionType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }

        switch (dbData) {
            case "advice":
                return PollQuestionType.ADVICE;
            case "multiple_choice":
                return PollQuestionType.MULTIPLE_CHOICE;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }

}
