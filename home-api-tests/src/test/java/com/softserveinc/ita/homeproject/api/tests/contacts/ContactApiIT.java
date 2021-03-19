package com.softserveinc.ita.homeproject.api.tests.contacts;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void getEmailTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        ApiResponse<ReadContact> getEmailResponse = contactApi
            .getContactOnUserWithHttpInfo(expectedUser.getId(), savedEmailContact.getId());
        ApiResponse<ReadContact> getPhoneResponse = contactApi
            .getContactOnUserWithHttpInfo(expectedUser.getId(), savedPhoneContact.getId());

        assertEquals(Response.Status.OK.getStatusCode(),
            getEmailResponse.getStatusCode());
        assertEquals(Response.Status.OK.getStatusCode(),
            getPhoneResponse.getStatusCode());
        assertEmailContact(createEmailContact, getEmailResponse.getData());
        assertPhoneContact(createPhoneContact, getPhoneResponse.getData());
    }

    @Test
    void updateEmailTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        CreateContact createPhoneContact = createPhoneContact();
        ReadUser expectedUser = userApi.createUser(createTestUser());
        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact);
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("updatedEmailContact@example.com")
            .main(false)
            .type(ContactType.EMAIL);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("+380567456595")
            .main(false)
            .type(ContactType.PHONE);


        ApiResponse<ReadContact> updateEmailResponse = contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
            savedEmailContact.getId(), updateEmailContact);
        ApiResponse<ReadContact> updatePhoneResponse = contactApi.updateContactOnUserWithHttpInfo(expectedUser.getId(),
            savedPhoneContact.getId(), updatePhoneContact);

        assertEquals(Response.Status.OK.getStatusCode(),
            updateEmailResponse.getStatusCode());
        assertEquals(Response.Status.OK.getStatusCode(),
            updatePhoneResponse.getStatusCode());
        assertEmailContact(savedEmailContact, updateEmailContact, updateEmailResponse.getData());
        assertPhoneContact(savedPhoneContact, updatePhoneContact, updatePhoneResponse.getData());
    }

    @Test
    void deleteEmailContact() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

        ReadContact savedEmailContact = contactApi.createContactOnUser(expectedUser.getId(), createEmailContact());
        ReadContact savedPhoneContact = contactApi.createContactOnUser(expectedUser.getId(), createPhoneContact());

        ApiResponse<Void> removeEmailResponse = contactApi
            .deleteContactOnUserWithHttpInfo(expectedUser.getId(),savedEmailContact.getId());
        ApiResponse<Void> removePhoneResponse = contactApi
            .deleteContactOnUserWithHttpInfo(expectedUser.getId(),savedPhoneContact.getId());

        List<ReadContact> queryContactsResponse = new ContactQuery
            .Builder(contactApi)
            .userId(expectedUser.getId())
            .pageNumber(1)
            .pageSize(10)
            .build().perform();

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removeEmailResponse.getStatusCode());
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removePhoneResponse.getStatusCode());
        assertFalse(queryContactsResponse.contains(savedEmailContact));
        assertFalse(queryContactsResponse.contains(savedPhoneContact));
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.getContactOnUser(expectedUser.getId(), savedEmailContact.getId()));
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> contactApi.getContactOnUser(expectedUser.getId(), savedPhoneContact.getId()));
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
