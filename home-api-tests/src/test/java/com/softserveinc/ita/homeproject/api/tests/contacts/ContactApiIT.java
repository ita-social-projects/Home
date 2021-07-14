package com.softserveinc.ita.homeproject.api.tests.contacts;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.ContactApi;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.ContactQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiMailHogUtil;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.ApiUsageFacade;
import com.softserveinc.ita.homeproject.api.tests.utils.mail.mock.dto.MailHogApiResponse;
import com.softserveinc.ita.homeproject.model.*;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ContactApiIT {

    private final ContactApi contactApi = new ContactApi(ApiClientUtil.getClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    @SneakyThrows
    private ReadUser createTestUserViaInvitation() {
        CreateCooperation createCoop = createBaseCooperation();
        cooperationApi.createCooperation(createCoop);

        TimeUnit.MILLISECONDS.sleep(5000);

        ApiUsageFacade api = new ApiUsageFacade();
        MailHogApiResponse mailResponse = api.getMessages(new ApiMailHogUtil(), MailHogApiResponse.class);

        CreateUser expectedUser = createBaseUser();
        expectedUser.setRegistrationToken(getToken(getDecodedLastMessage(mailResponse)));
        expectedUser.setEmail(createCoop.getAdminEmail());
        return userApi.createUser(expectedUser);
    }

    @Test
    void updateEmailTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("updatedEmailContact@example.com")
            .main(false)
            .type(ContactType.EMAIL);

        ApiResponse<ReadContact> updateEmailResponse = contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
            savedEmailContact.getId(), updateEmailContact);

        assertEquals(Response.Status.OK.getStatusCode(),
            updateEmailResponse.getStatusCode());
        assertEmailContact(savedEmailContact, updateEmailContact, updateEmailResponse.getData());
    }

    @Test
    void updatePhoneTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("+380567456595")
            .main(false)
            .type(ContactType.PHONE);

        ApiResponse<ReadContact> updatePhoneResponse = contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
            savedPhoneContact.getId(), updatePhoneContact);

        assertEquals(Response.Status.OK.getStatusCode(),
            updatePhoneResponse.getStatusCode());
        assertPhoneContact(savedPhoneContact, updatePhoneContact, updatePhoneResponse.getData());
    }

    @Test
    void updateContactEmailWithNullParameterTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email(null)
            .main(false)
            .type(ContactType.EMAIL);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                savedEmailContact.getId(), updateEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must not be null.");
    }

    @Test
    void updateContactEmailWithEmptyParameterTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("")
            .main(false)
            .type(ContactType.EMAIL);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                savedEmailContact.getId(), updateEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.")
            .withMessageContaining("Parameter `email` is invalid - size must be between 5 and 320 signs.");
    }

    @Test
    void updateContactEmailWithInvalidParameterTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("this.email.is.not.valid.for.this.example.write." +
                "now.that.is.why.we.have.bad.request.answer@gmail.com")
            .main(false)
            .type(ContactType.EMAIL);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                savedEmailContact.getId(), updateEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void updateContactPhoneWithNullParameterTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone(null)
            .main(false)
            .type(ContactType.PHONE);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                savedPhoneContact.getId(), updatePhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - must not be null.");
    }

    @Test
    void updateContactPhoneWithEmptyParameterTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("")
            .main(false)
            .type(ContactType.PHONE);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                savedPhoneContact.getId(), updatePhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - size must be between 9 and 13 signs.")
            .withMessageContaining("Parameter `phone` is invalid - must meet the rule.");
    }

    @Test
    void updateContactPhoneWithInvalidParameterTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("+380632121212133")
            .main(false)
            .type(ContactType.PHONE);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
                savedPhoneContact.getId(), updatePhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - must meet the rule.")
            .withMessageContaining("Parameter `phone` is invalid - size must be between 9 and 13 signs.");
    }

    @Test
    void deletingEmailContactTest() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact());

        ApiResponse<Void> removeEmailResponse = contactApi
            .deleteContactOnUserWithHttpInfo(expectedUser.getId(),savedEmailContact.getId());

        List<ReadContact> queryContactsResponse = new ContactQuery
            .Builder(contactApi)
            .userId(expectedUser.getId())
            .pageNumber(1)
            .pageSize(10)
            .build().perform();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removeEmailResponse.getStatusCode());
        assertFalse(queryContactsResponse.contains(savedEmailContact));
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.getContactOnUser(expectedUser.getId(), savedEmailContact.getId()));
    }

    @Test
    void deletingPhoneContactTest() throws ApiException {
        ReadUser expectedUser = createTestUserViaInvitation();

        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact());

        ApiResponse<Void> removePhoneResponse = contactApi
                .deleteContactOnUserWithHttpInfo(expectedUser.getId(),savedPhoneContact.getId());

        List<ReadContact> queryContactsResponse = new ContactQuery
                .Builder(contactApi)
                .userId(expectedUser.getId())
                .pageNumber(1)
                .pageSize(10)
                .build().perform();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removePhoneResponse.getStatusCode());
        assertFalse(queryContactsResponse.contains(savedPhoneContact));
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> contactApi.getContactOnUser(expectedUser.getId(), savedPhoneContact.getId()));
    }

    @Test
    void deletingNonExistentAnyContactTest() {
        ReadUser expectedUser = createTestUserViaInvitation();
        Long wrongId = 10000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> contactApi
                        .deleteContactOnUserWithHttpInfo(expectedUser.getId(), wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Can't find contact with given ID:" + wrongId);
    }

    @Test
    void passNullIdContactWhenDeleteAnyContactTest() {
        ReadUser expectedUser = createTestUserViaInvitation();

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> contactApi
                        .deleteContactOnUserWithHttpInfo(expectedUser.getId(), null))
                .withMessageContaining("Missing the required parameter 'id' when calling deleteContactOnUser");
    }

    @Test
    void passNullUserIdWhenDeleteAnyContactTest() {
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> contactApi
                        .deleteContactOnUserWithHttpInfo(null, 11L))
                .withMessageContaining("Missing the required parameter 'userId' when calling deleteContactOnUser");
    }

    private String getDecodedLastMessage(MailHogApiResponse response) {
        String body = response.getItems().get(0).getMime().getParts().get(0).getMime().getParts().get(0).getBody();
        return new String(Base64.getMimeDecoder().decode(body), StandardCharsets.UTF_8);
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

    private CreateContact createEmailContact(){
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

    private CreateCooperation createBaseCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail(RandomStringUtils.randomAlphabetic(12).concat("@gmail.com"))
                .address(createAddress())
                .contacts(createContactList());
    }

    private CreateUser createBaseUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email("test.receive.apartment@gmail.com");
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

    private CreateUser createTestUser() {
        return new CreateUser()
                .firstName("firstName")
                .lastName("lastName")
                .password("password")
                .email(RandomStringUtils.randomAlphabetic(5).concat("@example.com"))
                .contacts(createContactList());
    }

    private void assertEmailContact(CreateContact expected, ReadContact actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertTrue(expected.getType().equals(ContactType.EMAIL) && actual.getType().equals(ContactType.EMAIL));
        assertEquals(((CreateEmailContact)expected).getEmail(), ((ReadEmailContact)actual).getEmail());
    }

    private void assertEmailContact(ReadContact saved, UpdateContact update, ReadContact updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertTrue(saved.getType().equals(ContactType.EMAIL)
                && update.getType().equals(ContactType.EMAIL)
                && updated.getType().equals(ContactType.EMAIL));
        assertEquals(((UpdateEmailContact)update).getEmail(), ((ReadEmailContact)updated).getEmail());
    }

    private void assertPhoneContact(CreateContact expected, ReadContact actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertTrue(expected.getType().equals(ContactType.PHONE) && actual.getType().equals(ContactType.PHONE));
        assertEquals(((CreatePhoneContact)expected).getPhone(), ((ReadPhoneContact)actual).getPhone());
    }

    private void assertPhoneContact(ReadContact saved, UpdateContact update, ReadContact updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertTrue(saved.getType().equals(ContactType.PHONE)
                && update.getType().equals(ContactType.PHONE)
                && updated.getType().equals(ContactType.PHONE));
        assertEquals(((UpdatePhoneContact)update).getPhone(), ((ReadPhoneContact)updated).getPhone());
    }
}
