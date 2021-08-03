package com.softserveinc.ita.homeproject.api.tests.invitations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.*;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class InvitationApiIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getClient());

    private final InvitationsApi invitationApi = new InvitationsApi(ApiClientUtil.getClient());

    @Test
    void isEmailSentTest() throws Exception {
        CreateCooperation createCoop = createCooperation();
        cooperationApi.createCooperationWithHttpInfo(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse response = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        assertTrue(response.getCount() > 0);
    }

    @Test
    void isApartmentTokenAccepted() throws Exception {
        String userEmail = RandomStringUtils.randomAlphabetic(10).concat("@gmail.com");
        ReadCooperation readCooperation = createTestCooperationAndUserViaInvitationWithUserEmail(userEmail);

        ReadHouse createdHouse = houseApi.createHouse(readCooperation.getId(), createHouse());
        apartmentApi.createApartment(createdHouse.getId(), createApartmentWithEmail(userEmail));

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        String apartmentInvitationToken = getToken(getDecodedMessageByEmail(mailResponse, userEmail));

        ReadInvitation readInvitation = invitationApi.approveInvitation(buildInvitationPayload(apartmentInvitationToken));

        assertEquals(readInvitation.getEmail(), userEmail);
        assertEquals(readInvitation.getType(), InvitationType.APARTMENT);
        assertEquals(readInvitation.getStatus(),InvitationStatus.ACCEPTED);
    }

    @Test
    void isCooperationTokenAccepted() throws Exception {
        String userEmail = RandomStringUtils.randomAlphabetic(10).concat("@gmail.com");
        createTestCooperationAndUserViaInvitationWithUserEmail(userEmail);
        createTestCooperationWithUserEmail(userEmail);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        String cooperationInvitationToken = getToken(getDecodedMessageByEmail(mailResponse, userEmail));

        ReadInvitation readInvitation = invitationApi.approveInvitation(buildInvitationPayload(cooperationInvitationToken));

        assertEquals(readInvitation.getEmail(), userEmail);
        assertEquals(readInvitation.getType(), InvitationType.COOPERATION);
        assertEquals(readInvitation.getStatus(),InvitationStatus.ACCEPTED);
    }

    public InvitationToken buildInvitationPayload(String invitationToken){
        InvitationToken token = new InvitationToken();
        token.setInvitationToken(invitationToken);
        return token;
    }

    @SneakyThrows
    private ReadCooperation createTestCooperationWithUserEmail(String userEmail) {
        CreateCooperation createCoop = createCooperationWithUserEmail(userEmail);
        ReadCooperation readCooperation = cooperationApi.createCooperation(createCoop);
        return readCooperation;
    }

    @SneakyThrows
    private ReadCooperation createTestCooperationAndUserViaInvitationWithUserEmail(String userEmail) {
        ReadCooperation readCooperation = createTestCooperationWithUserEmail(userEmail);
        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,userEmail)));
        expectedUser.setEmail(userEmail);
        userApi.createUser(expectedUser);
        return readCooperation;
    }

    private CreateUser createBaseUser() {
        return new CreateUser()
                   .firstName("firstName")
                   .lastName("lastName")
                   .password("password")
                   .email( RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"));
    }

    private String getDecodedMessageByEmail(MailHogApiResponse response, String email) {
        String message="";
        for (int i=0; i<response.getItems().size(); i++){
            if(response.getItems().get(i).getContent().getHeaders().getTo().contains(email)){
                message = response.getItems().get(i).getMime().getParts().get(0).getMime().getParts().get(0).getBody();
                break;
            }
        }
        return new String(Base64.getMimeDecoder().decode(message), StandardCharsets.UTF_8);
    }

    private String getToken(String str) {
        Pattern pattern = Pattern.compile("(?<=:) .* (?=<)");
        Matcher matcher = pattern.matcher(str);

        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        }

        return result.trim();
    }

    private CreateCooperation createCooperationWithUserEmail(String email) {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(10).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(email)
                .address(createAddress());
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(10).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(createBaseUser().getEmail())
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

    private CreateHouse createHouse() {
        return new CreateHouse()
                .adjoiningArea(500)
                .houseArea(BigDecimal.valueOf(500.0))
                .quantityFlat(50)
                .address(createAddress());
    }

    private CreateApartment createApartmentWithEmail(String userEmail) {
        return new CreateApartment()
                .area(BigDecimal.valueOf(72.5))
                .number("15")
                .invitations(createApartmentInvitationWithEmail(userEmail));
    }

    private List<CreateInvitation> createApartmentInvitationWithEmail(String userEmail) {
        List<CreateInvitation> createInvitations = new ArrayList<>();
        createInvitations.add(new CreateApartmentInvitation()
                .ownershipPart(BigDecimal.valueOf(0.3))
                .email( userEmail)
                .type(InvitationType.APARTMENT));
        return createInvitations;
    }
}
