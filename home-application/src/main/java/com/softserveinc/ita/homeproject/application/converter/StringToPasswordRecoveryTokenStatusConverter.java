package com.softserveinc.ita.homeproject.application.converter;

import javax.ws.rs.ext.Provider;

import com.softserveinc.ita.homeproject.homedata.user.password.enums.PasswordRecoveryTokenStatus;
import org.springframework.core.convert.converter.Converter;


@Provider
public class StringToPasswordRecoveryTokenStatusConverter implements Converter<String, PasswordRecoveryTokenStatus> {

    @Override
    public PasswordRecoveryTokenStatus convert(final String source) {
        PasswordRecoveryTokenStatus result = null;

        if (source != null && !source.isEmpty()) {
            for (PasswordRecoveryTokenStatus constant : PasswordRecoveryTokenStatus.values()) {
                if (constant.toString().equals(source)) {
                    result = constant;
                }
            }
        }

        return result;
    }
}
