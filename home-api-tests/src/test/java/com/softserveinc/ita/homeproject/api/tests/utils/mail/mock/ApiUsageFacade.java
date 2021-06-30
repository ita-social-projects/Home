package com.softserveinc.ita.homeproject.api.tests.utils.mail.mock;

import java.io.IOException;

public class ApiUsageFacade {

    public <T> T getMessages(ApiMailUtil apiMailUtil, Class <T> clazz) throws IOException, InterruptedException {

        String apiUri = apiMailUtil.getApiURI();
        String endpoint = apiMailUtil.getEndpointName();

        String response = apiMailUtil.getReceivedMessages(apiUri, endpoint);

        ApiResponseConverter converter = new ApiResponseConverter();

        return converter.convert(response, clazz);
    }
}
