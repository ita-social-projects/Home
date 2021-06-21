package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.AttributeConverter;

public class AdviceChoiceAnswerAttributeConverter implements AttributeConverter<AdviceChoiceAnswerVariant, String> {

    @Override
    public String convertToDatabaseColumn(AdviceChoiceAnswerVariant attribute) {
        if (attribute == null) {
            return null;
        }
        switch (attribute) {
            case FOR:
                return "for";
            case AGAINST:
                return "against";
            case ABSTAINED:
                return "abstained";
            case FREE_ANSWER:
                return "free_answer";
            default:
                throw new IllegalArgumentException(attribute + " not supported.");
        }
    }

    @Override
    public AdviceChoiceAnswerVariant convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case "for":
                return AdviceChoiceAnswerVariant.FOR;
            case "against":
                return AdviceChoiceAnswerVariant.AGAINST;
            case "abstained":
                return AdviceChoiceAnswerVariant.ABSTAINED;
            case "free_answer":
                return AdviceChoiceAnswerVariant.FREE_ANSWER;
            default:
                throw new IllegalArgumentException(dbData + " not supported.");
        }
    }
}
