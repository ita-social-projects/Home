package com.softserveinc.ita.homeproject.application.converter;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.enums.InvitationStatus;
import org.springframework.core.convert.converter.Converter;

@Provider
public class StringToInvitationStatusConverter implements Converter<String, InvitationStatus> {

    @Override
    public InvitationStatus convert(final String source) {

        InvitationStatus result = null;

        if (source != null && !source.isEmpty()) {
            for (InvitationStatus constant : InvitationStatus.values()) {
                if (constant.toString().equals(source)) {
                    result = constant;
                }
            }
        }

        return result;
    }
}
