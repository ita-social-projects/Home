package com.softserveinc.ita.homeproject.application.converter;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.entity.PollType;
import org.springframework.core.convert.converter.Converter;

@Provider
public class StringToPollTypeConverter implements Converter<String, PollType> {
    @Override
    public PollType convert(final String source) {

        PollType result = null;

        if (source != null && !source.isEmpty()) {
            for (PollType constant : PollType.values()) {
                if (constant.toString().equals(source)) {
                    result = constant;
                }
            }
        }

        return result;
    }
}
