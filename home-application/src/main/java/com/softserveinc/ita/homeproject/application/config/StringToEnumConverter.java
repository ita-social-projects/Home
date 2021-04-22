package com.softserveinc.ita.homeproject.application.config;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.entity.ContactType;
import org.springframework.core.convert.converter.Converter;




@Provider
public class StringToEnumConverter implements Converter<String, ContactType> {
    @Override
    public ContactType convert(final String source) {
        return ContactType.valueOf(source.toUpperCase());
    }
}
