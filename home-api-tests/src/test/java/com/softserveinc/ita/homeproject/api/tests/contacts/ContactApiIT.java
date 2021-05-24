package com.softserveinc.ita.homeproject.api.tests.contacts;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.ContactApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.ContactQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadEmailContact;
import com.softserveinc.ita.homeproject.model.ReadPhoneContact;
import com.softserveinc.ita.homeproject.model.ReadUser;
import com.softserveinc.ita.homeproject.model.UpdateContact;
import com.softserveinc.ita.homeproject.model.UpdateEmailContact;
import com.softserveinc.ita.homeproject.model.UpdatePhoneContact;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class ContactApiIT {

    private final ContactApi contactApi = new ContactApi(ApiClientUtil.getClient());
    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    @Test
    void createContactTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());

        ApiResponse<ReadContact> createEmailResponse = contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
            createEmailContact);
        ApiResponse<ReadContact> createPhoneResponse = contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
            createPhoneContact);

        assertEquals(Response.Status.CREATED.getStatusCode(),
            createEmailResponse.getStatusCode());
        assertEquals(Response.Status.CREATED.getStatusCode(),
            createPhoneResponse.getStatusCode());
        assertEmailContact(createEmailContact, createEmailResponse.getData());
        assertPhoneContact(createPhoneContact, createPhoneResponse.getData());
    }

    @Test
    void createContactEmailWithNullParameterTest() throws ApiException {
        CreateContact createEmailContact = new CreateEmailContact()
            .email(null)
            .main(false)
            .type(ContactType.EMAIL);
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
                createEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must not be null.");
    }

    @Test
    void creatingContactWithEmptyEmailTest() throws ApiException {
        CreateContact createEmailContact = new CreateEmailContact()
            .email("")
            .main(false)
            .type(ContactType.EMAIL);
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
                createEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void creatingContactWithInvalidEmailPatternTest() throws ApiException {
        CreateContact createEmailContact = new CreateEmailContact()
            .email("this.email.is.not.valid.for.this.example.write." +
                "now.that.is.why.we.have.bad.request.answer@gmail.com")
            .main(false)
            .type(ContactType.EMAIL);
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
                createEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void createPhoneWithNullParameterTest() throws ApiException {
        CreateContact createPhoneContact = new CreatePhoneContact()
            .phone(null)
            .main(false)
            .type(ContactType.PHONE);
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
                createPhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - must not be null.");
    }

    @Test
    void creatingContactWithEmptyPhoneTest() throws ApiException {
        CreateContact createPhoneContact = new CreatePhoneContact()
            .phone("")
            .main(false)
            .type(ContactType.PHONE);
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
                createPhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - must meet the rule.")
            .withMessageContaining("Parameter `phone` is invalid - size must be between 9 and 13 signs.");
    }

    @Test
    void creatingContactWithInvalidPhonePatternTest() throws ApiException {
        CreateContact createPhoneContact = new CreatePhoneContact()
            .phone("+380632121212133")
            .main(false)
            .type(ContactType.PHONE);
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.createContactOnUserWithHttpInfo(expectedUser.getId(),
                createPhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - size must be between 9 and 13 signs.")
            .withMessageContaining("Parameter `phone` is invalid - must meet the rule.");
    }

    @Test
    void getEmailTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);

        ApiResponse<ReadContact> getEmailResponse = contactApi
            .getContactOnUserWithHttpInfo(expectedUser.getId(), savedEmailContact.getId());

        assertEquals(Response.Status.OK.getStatusCode(),
            getEmailResponse.getStatusCode());
        assertEmailContact(createEmailContact, getEmailResponse.getData());
    }

    @Test
    void getPhoneTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        ApiResponse<ReadContact> getPhoneResponse = contactApi
            .getContactOnUserWithHttpInfo(expectedUser.getId(), savedPhoneContact.getId());

        assertEquals(Response.Status.OK.getStatusCode(),
            getPhoneResponse.getStatusCode());
        assertPhoneContact(createPhoneContact, getPhoneResponse.getData());
    }

    @Test
    void getNonExistentContact() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());
        Long wrongId = 20000L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi
                .getContactOnUserWithHttpInfo(expectedUser.getId(), wrongId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(
                "Contact with 'id: " + wrongId + "' is not found");
    }

    @Test
    void passNullWhenGetContactTest() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi
                .getContactOnUserWithHttpInfo(expectedUser.getId(), null))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Missing the required parameter 'id' when calling getContactOnUser");
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
            .withMessageContaining("Parameter `email` is invalid - size must be between 9 and 320 signs.");
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
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
    void deletingNonExistentAnyContactTest() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());
        Long wrongId = 10000L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi
                .deleteContactOnUserWithHttpInfo(expectedUser.getId(), wrongId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Can't find contact with given ID:" + wrongId);
    }

    @Test
    void passNullIdContactWhenDeleteAnyContactTest() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
