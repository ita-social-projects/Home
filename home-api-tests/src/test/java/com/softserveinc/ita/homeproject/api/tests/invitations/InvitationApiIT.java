package com.softserveinc.ita.homeproject.api.tests.invitations;

import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InvitationApiIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    @Disabled("NotValid test")
    @Test
    void isEmailSentTest() throws Exception {
        CreateCooperation createCoop = createCooperation();
        ApiResponse<ReadCooperation> cooperationWithHttpInfo = cooperationApi.createCooperationWithHttpInfo(createCoop);

        TimeUnit.MILLISECONDS.sleep(60000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse response = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        assertTrue(getLastMessageEmailTo(response).contains(createCoop.getAdminEmail()));
        assertTrue(response.getCount() > 0);
        assertTrue(getLastMessageSubject(response).contains("invitation to cooperation"));
    }

    private String getLastMessageEmailTo(MailHogApiResponse response){
        return String.valueOf(response.getItems().get(0).getContent().getHeaders().getTo());
    }

    private String getLastMessageSubject(MailHogApiResponse response){
        return String.valueOf(response.getItems().get(0).getContent().getHeaders().getSubject());
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail("test.receive.messages@gmail.com")
                .address(createAddress())
                .houses(createHouseList())
                .contacts(createContactList());
    }

    private List<CreateHouse> createHouseList() {
        List<CreateHouse> createHouses = new ArrayList<>();
        createHouses.add(new CreateHouse()
                .quantityFlat(96)
                .houseArea(BigDecimal.valueOf(4348.8))
                .adjoiningArea(400)
                .address(createAddress()));

        createHouses.add(new CreateHouse()
                .quantityFlat(150)
                .houseArea(BigDecimal.valueOf(7260))
                .adjoiningArea(600)
                .address(createAddress()));

        return createHouses;
    }

    private List<CreateContact> createContactList() {
        List<CreateContact> createContact = new ArrayList<>();
        createContact.add(new CreateEmailContact()
                .email("primaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(true));

        createContact.add(new CreateEmailContact()
                .email("secondaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(false));
        return createContact;
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
