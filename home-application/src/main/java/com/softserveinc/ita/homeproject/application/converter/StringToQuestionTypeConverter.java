package com.softserveinc.ita.homeproject.application.converter;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.entity.PollQuestionType;
import org.springframework.core.convert.converter.Converter;

@Provider
public class StringToQuestionTypeConverter implements Converter<String, PollQuestionType> {

    @Override
    public PollQuestionType convert(final String source) {

        PollQuestionType result = null;

        if (source != null && !source.isEmpty()) {
            for (PollQuestionType constant : PollQuestionType.values()) {
                if (constant.toString().equals(source)) {
                    result = constant;
                }
            }
        }

        return result;
    }
}
