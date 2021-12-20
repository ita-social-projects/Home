package com.softserveinc.ita.homeproject.application.converter;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationType;
import org.springframework.core.convert.converter.Converter;

@Provider
public class StringToInvitationTypeConverter implements Converter<String, InvitationType> {
    @Override
    public InvitationType convert(final String source) {

        InvitationType result = null;

        if (source != null && !source.isEmpty()) {
            for (InvitationType constant : InvitationType.values()) {
                if (constant.toString().equals(source)) {
                    result = constant;
                }
            }
        }

        return result;
    }
}
