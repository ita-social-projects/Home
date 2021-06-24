package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

public final class ApiMailHogUtil extends ApiMailUtil {

    private static final String MAIL_API_PORT = System.getProperty("mailhog.external.port");

    @Override
    protected final String getApiURI(){
        return "http://localhost:" + MAIL_API_PORT + "/api/v2/";
    }

    @Override
    protected String getEndpointName() {
        return "messages";
    }
}
