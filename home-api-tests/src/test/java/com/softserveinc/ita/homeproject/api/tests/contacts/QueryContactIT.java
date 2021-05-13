package com.softserveinc.ita.homeproject.api.tests.contacts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.api.ContactApi;
import com.softserveinc.ita.homeproject.api.UserApi;
import com.softserveinc.ita.homeproject.api.tests.query.ContactQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.BaseReadView;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.model.CreateUser;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadEmailContact;
import com.softserveinc.ita.homeproject.model.ReadPhoneContact;
import com.softserveinc.ita.homeproject.model.ReadUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class QueryContactIT {

    private final ContactApi contactApi = new ContactApi(ApiClientUtil.getClient());

    private final UserApi userApi = new UserApi(ApiClientUtil.getClient());

    @Test
    void getAllContactsAscSort() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

        List<ReadContact> queryContactsResponse = new ContactQuery
            .Builder(contactApi)
            .userId(expectedUser.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
        assertEquals(expectedUser.getContacts().size(), queryContactsResponse.size());
    }

    @Test
    void getAllContactsDescSort() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

        List<ReadContact> queryContactsResponse = new ContactQuery
            .Builder(contactApi)
            .userId(expectedUser.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,desc")
            .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId).reversed());
        assertEquals(expectedUser.getContacts().size(), queryContactsResponse.size());
    }

    @Test
    void getAllContactsFilteredBy() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());


        List<ReadContact> queryContactsResponse = new ContactQuery
            .Builder(contactApi)
            .userId(expectedUser.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .filter("phone=like=+38067")
            .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(BaseReadView::getId));
        queryContactsResponse
            .forEach(contact -> assertTrue(((ReadPhoneContact)contact).getPhone().contains("+38067")));
    }

    @Test
    void getAllContactsById() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());
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
        ReadUser expectedUser = userApi.createUser(createTestUser());
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
        ReadUser expectedUser = userApi.createUser(createTestUser());
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
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
    void getAllContactsByInvalidType() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
    void getAllContactsByEmptyType() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

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
    void getAllContactsByNullType() throws ApiException {
        ReadUser expectedUser = userApi.createUser(createTestUser());

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

}
