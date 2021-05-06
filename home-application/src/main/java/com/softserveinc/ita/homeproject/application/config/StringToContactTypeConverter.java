package com.softserveinc.ita.homeproject.application.config;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import org.springframework.core.convert.converter.Converter;




@Provider
public class StringToContactTypeConverter implements Converter<String, ContactType> {
    @Override
    public ContactType convert(final String source) {

        ContactType res = null;

        if (source == null || source.isEmpty()) {
            return null;
        }

        for (ContactType constant : ContactType.values()) {
            if (constant.toString().equals(source)) {
                res = constant;
            }
        }

        return res;
    }
}
