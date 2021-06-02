package com.softserveinc.ita.homeproject.application.converter;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import org.springframework.core.convert.converter.Converter;

@Provider
public class StringToPollStatusConverter implements Converter<String, PollStatus> {
    @Override
    public PollStatus convert(final String source) {

        PollStatus result = null;

        if (source != null && !source.isEmpty()) {
            for (PollStatus constant : PollStatus.values()) {
                if (constant.toString().equals(source)) {
                    result = constant;
                }
            }
        }

        return result;
    }
}
