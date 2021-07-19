package com.softserveinc.ita.homeproject.api.tests.contacts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ContactApi;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.ContactQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadEmailContact;
import com.softserveinc.ita.homeproject.model.ReadPhoneContact;
import com.softserveinc.ita.homeproject.model.ReadUser;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class QueryContactIT {

    private final ContactApi contactApi = new ContactApi(ApiClientUtil.getClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    @Test
    void getAllContactsAscSort() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(ReadContact::getId));
        assertEquals(Objects.requireNonNull(expectedUser.getContacts()).size(), queryContactsResponse.size());
    }

    @Test
    void getAllContactsDescSort() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(ReadContact::getId).reversed());
        assertEquals(Objects.requireNonNull(expectedUser.getContacts()).size(), queryContactsResponse.size());
    }

    @Test
    void getAllContactsFilteredBy() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .filter("phone=like=+38067")
                .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(ReadContact::getId));
        queryContactsResponse
                .forEach(contact -> assertTrue(Objects.requireNonNull(((ReadPhoneContact) contact).getPhone()).contains("+38067")));
    }

    @Test
    void getAllContactsById() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();
        ReadContact savedContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact());

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .id(savedContact.getId())
                .build().perform();

        assertTrue(queryContactsResponse.contains(savedContact));
    }

    @Test
    void getAllContactsByPhone() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        ReadPhoneContact savedContact =
                (ReadPhoneContact) contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact());

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .phone(savedContact.getPhone())
                .build().perform();

        assertTrue(queryContactsResponse.contains(savedContact));
    }

    @Test
    void getAllContactsByEmail() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();
        ReadEmailContact savedContact =
                (ReadEmailContact) contactApi.createContactOnUser(expectedUser.getId(), createEmailContact());

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .email(savedContact.getEmail())
                .build().perform();

        assertTrue(queryContactsResponse.contains(savedContact));
    }

    @Test
    void getAllContactsByMain() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .main("true")
                .build().perform();

        queryContactsResponse.forEach(contact -> assertThat(contact.getMain()).isTrue());
    }

    @Test
    void getAllContactsByTypeEmail() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .type("email")
                .build().perform();

        queryContactsResponse.forEach(contact -> assertThat(contact.getType()).isEqualTo(ContactType.EMAIL));
    }

    @Test
    void getAllContactsByTypePhone() throws ApiException {
        ReadUser expectedUser = createBaseUserForTests();

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .type("phone")
                .build().perform();

        queryContactsResponse.forEach(contact -> assertThat(contact.getType()).isEqualTo(ContactType.PHONE));
    }

    @Test
    void getAllContactsByInvalidType() {
        ReadUser expectedUser = createBaseUserForTests();

        assertThatExceptionOfType(java.lang.IllegalArgumentException.class)
                .isThrownBy(() -> new ContactQuery
                        .Builder(contactApi)
                        .userId(expectedUser.getId())
                        .pageNumber(1)
                        .pageSize(10)
                        .type("EMAIL")
                        .build().perform())
                .withMessageContaining("Unexpected value 'EMAIL'");
    }

    @Test
    void getAllContactsByEmptyType() {
        ReadUser expectedUser = createBaseUserForTests();

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> new ContactQuery
                        .Builder(contactApi)
                        .userId(expectedUser.getId())
                        .pageNumber(1)
                        .pageSize(10)
                        .filter("type==")
                        .build().perform())
                .matches(exception -> exception.getCode() == 400)
                .withMessageContaining("The query argument for search is empty");
    }

    @Test
    void getAllContactsByNullType() {
        ReadUser expectedUser = createBaseUserForTests();

        assertThatExceptionOfType(java.lang.IllegalArgumentException.class)
                .isThrownBy(() -> new ContactQuery
                        .Builder(contactApi)
                        .userId(expectedUser.getId())
                        .pageNumber(1)
                        .pageSize(10)
                        .type(null)
                        .build().perform())
                .withMessageContaining("Unexpected value 'null'");
    }

    private CreateContact createEmailContact() {
        return new CreateEmailContact()
                .email("newEmailContact@example.com")
                .main(false)
                .type(ContactType.EMAIL);
    }

    private CreateContact createPhoneContact() {
        return new CreatePhoneContact()
                .phone("+380632121212")
                .main(false)
                .type(ContactType.PHONE);
    }

    private List<CreateContact> createContactList() {
        List<CreateContact> createContactList = new ArrayList<>();
        createContactList.add(new CreateEmailContact()
                .email("primaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(true));

        createContactList.add(new CreateEmailContact()
                .email("secondaryemail@example.com")
                .type(ContactType.EMAIL)
                .main(false));

        createContactList.add(new CreateEmailContact()
                .email("myemail@example.com")
                .type(ContactType.EMAIL)
                .main(false));

        createContactList.add(new CreateEmailContact()
                .email("youremail@example.com")
                .type(ContactType.EMAIL)
                .main(false));

        createContactList.add(new CreatePhoneContact()
                .phone("+380509999999")
                .type(ContactType.PHONE)
                .main(true));

        createContactList.add(new CreatePhoneContact()
                .phone("+380679999999")
                .type(ContactType.PHONE)
                .main(false));

        createContactList.add(new CreatePhoneContact()
                .phone("+380639999999")
                .type(ContactType.PHONE)
                .main(false));

        return createContactList;
    }

    private CreateUser createTestUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
                .contacts(createContactList());
    }

    private CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
                .address(createAddress());
    }

    private CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("test.receive.apartment@gmail.com")
                .contacts(createContactList());
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

    @SneakyThrows
    private ReadUser createBaseUserForTests() {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedMessageByEmail(mailResponse,createCoop.getAdminEmail())));
        expectedUser.setEmail(createCoop.getAdminEmail());
        return userApi.createUser(expectedUser);
    }
}
