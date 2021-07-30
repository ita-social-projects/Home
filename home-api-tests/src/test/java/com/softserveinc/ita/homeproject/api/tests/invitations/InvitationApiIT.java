package com.softserveinc.ita.homeproject.api.tests.invitations;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.concurrent.TimeUnit;

import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class InvitationApiIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    @Test
    void isEmailSentTest() throws Exception {
        CreateCooperation createCoop = createCooperation();
        cooperationApi.createCooperationWithHttpInfo(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse response = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        assertTrue(response.getCount() > 0);
        assertTrue(getSubjectByEmail(response, createCoop.getAdminEmail()).contains("invitation to cooperation"));
    }

    private String getSubjectByEmail(MailHogApiResponse response, String email) {
        String message = "";
        for (int i = 0; i < response.getItems().size(); i++) {
            if (response.getItems().get(i).getContent().getHeaders().getTo().contains(email)) {
                message = String.valueOf(response.getItems().get(0).getContent().getHeaders().getSubject());
                break;
            }
        }
        return message;
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail("test.receive.subject@gmail.com")
                .address(createAddress());
    }

    private Address createAddress() {
        return new Address().city("Dnepr")
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode");
    }
}
