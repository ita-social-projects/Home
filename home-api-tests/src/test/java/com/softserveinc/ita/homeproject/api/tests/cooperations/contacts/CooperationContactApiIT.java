package com.softserveinc.ita.homeproject.api.tests.cooperations.contacts;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.CooperationContactApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.ContactType;
import com.softserveinc.ita.homeproject.model.CreateContact;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.model.ReadContact;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadEmailContact;
import com.softserveinc.ita.homeproject.model.ReadPhoneContact;
import com.softserveinc.ita.homeproject.model.UpdateContact;
import com.softserveinc.ita.homeproject.model.UpdateEmailContact;
import com.softserveinc.ita.homeproject.model.UpdatePhoneContact;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class CooperationContactApiIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());
    private final CooperationContactApi cooperationContactApi = new CooperationContactApi(ApiClientUtil.getClient());

    @Test
    void createCooperationContactTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        CreateContact createPhoneContact = createPhoneContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ApiResponse<ReadContact> createEmailResponse = cooperationContactApi
            .createContactOnCooperationWithHttpInfo(cooperation.getId(), createEmailContact);
        ApiResponse<ReadContact> createPhoneResponse = cooperationContactApi
            .createContactOnCooperationWithHttpInfo(cooperation.getId(), createPhoneContact);

        assertEquals(Response.Status.CREATED.getStatusCode(), createEmailResponse.getStatusCode());
        assertEquals(Response.Status.CREATED.getStatusCode(), createPhoneResponse.getStatusCode());
        assertEmailContact(createEmailContact, createEmailResponse.getData());
        assertPhoneContact(createPhoneContact, createPhoneResponse.getData());
    }

    @Test
    void getCooperationEmailContactTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());
        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createEmailContact);

        ApiResponse<ReadContact> getEmailResponse = cooperationContactApi
            .getContactOnCooperationWithHttpInfo(cooperation.getId(), savedEmailContact.getId());

        assertEquals(Response.Status.OK.getStatusCode(), getEmailResponse.getStatusCode());
        assertEmailContact(createEmailContact, getEmailResponse.getData());
    }

    @Test
    void getCooperationPhoneContactTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());
        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createPhoneContact);

        ApiResponse<ReadContact> getEmailResponse = cooperationContactApi
            .getContactOnCooperationWithHttpInfo(cooperation.getId(), savedEmailContact.getId());

        assertEquals(Response.Status.OK.getStatusCode(), getEmailResponse.getStatusCode());
        assertPhoneContact(createPhoneContact, getEmailResponse.getData());
    }

    @Test
    void getNonExistentContact() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());
        Long wrongId = 20000L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .getContactOnCooperation(expectedCooperation.getId(), wrongId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Contact with 'id: " + wrongId + "' is not found");
    }

    @Test
    void passNullCooperationIdWhenGetContactTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .getContactOnCooperation(null, 1155L))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Missing the required parameter 'cooperationId' when calling getContactOnCooperation");
    }

    @Test
    void passNullWhenGetContactTest() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .getContactOnCooperation(expectedCooperation.getId(), null))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Missing the required parameter 'id' when calling getContactOnCooperation");
    }

    @Test
    void updateCooperationEmailContactTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());
        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("updatedEmailContact@example.com")
            .main(false)
            .type(ContactType.EMAIL);

        ApiResponse<ReadContact> updateEmailResponse = cooperationContactApi
            .updateContactOnCooperationWithHttpInfo(cooperation.getId(), savedEmailContact.getId(), updateEmailContact);

        assertEquals(Response.Status.OK.getStatusCode(), updateEmailResponse.getStatusCode());
        assertEmailContact(savedEmailContact, updateEmailContact, updateEmailResponse.getData());
    }

    @Test
    void updateCooperationPhoneContactTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());
        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("+380567456595")
            .main(false)
            .type(ContactType.PHONE);

        ApiResponse<ReadContact> updateEmailResponse = cooperationContactApi
            .updateContactOnCooperationWithHttpInfo(cooperation.getId(), savedEmailContact.getId(), updatePhoneContact);

        assertEquals(Response.Status.OK.getStatusCode(), updateEmailResponse.getStatusCode());
        assertPhoneContact(savedEmailContact, updatePhoneContact, updateEmailResponse.getData());
    }

    @Test
    void updateCooperationEmailContactWithNullParameterTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email(null)
            .main(false)
            .type(ContactType.EMAIL);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi.updateContactOnCooperationWithHttpInfo(cooperation.getId(),
                savedEmailContact.getId(), updateEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must not be null.");
    }

    @Test
    void updateCooperationPhoneContactWithNullParameterTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedPhoneContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone(null)
            .main(false)
            .type(ContactType.PHONE);
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi.updateContactOnCooperationWithHttpInfo(cooperation.getId(),
                savedPhoneContact.getId(), updatePhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - must not be null.");
    }

    @Test
    void updateCooperationEmailWithEmptyParameterTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("")
            .main(false)
            .type(ContactType.EMAIL);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi.updateContactOnCooperationWithHttpInfo(cooperation.getId(),
                savedEmailContact.getId(), updateEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void updateCooperationPhoneWithEmptyParameterTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedPhoneContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("")
            .main(false)
            .type(ContactType.PHONE);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi.updateContactOnCooperationWithHttpInfo(cooperation.getId(),
                savedPhoneContact.getId(), updatePhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - must meet the rule.");
    }

    @Test
    void updateContactEmailWithInvalidParameterTest() throws ApiException {
        CreateContact createEmailContact = createEmailContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createEmailContact);

        UpdateContact updateEmailContact = new UpdateEmailContact()
            .email("this.email.is.not.valid.for.this.example.write." +
                "now.that.is.why.we.have.bad.request.answer@gmail.com")
            .main(false)
            .type(ContactType.EMAIL);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi.updateContactOnCooperationWithHttpInfo(cooperation.getId(),
                savedEmailContact.getId(), updateEmailContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `email` is invalid - must meet the rule.");
    }

    @Test
    void updateContactPhoneWithInvalidParameterTest() throws ApiException {
        CreateContact createPhoneContact = createPhoneContact();
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedPhoneContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createPhoneContact);

        UpdateContact updatePhoneContact = new UpdatePhoneContact()
            .phone("+380632121212133")
            .main(false)
            .type(ContactType.PHONE);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi.updateContactOnCooperationWithHttpInfo(cooperation.getId(),
                savedPhoneContact.getId(), updatePhoneContact))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `phone` is invalid - size must be between 9 and 13 signs.")
            .withMessageContaining("Parameter `phone` is invalid - must meet the rule.");
    }

    @Test
    void deleteCooperationEmailContactTest() throws ApiException {
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedEmailContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createEmailContact());

        ApiResponse<Void> removeEmailResponse = cooperationContactApi
            .deleteContactOnCooperationWithHttpInfo(cooperation.getId(), savedEmailContact.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removeEmailResponse.getStatusCode());
        assertThatExceptionOfType(ApiException.class).isThrownBy(() -> cooperationContactApi
            .getContactOnCooperationWithHttpInfo(cooperation.getId(), savedEmailContact.getId()))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Contact with 'id: " + savedEmailContact.getId() + "' is not found");
    }

    @Test
    void deleteCooperationPhoneContactTest() throws ApiException {
        ReadCooperation cooperation = cooperationApi.createCooperation(createCooperation());

        ReadContact savedPhoneContact = cooperationContactApi
            .createContactOnCooperation(cooperation.getId(), createPhoneContact());

        ApiResponse<Void> removeEmailResponse = cooperationContactApi
            .deleteContactOnCooperationWithHttpInfo(cooperation.getId(), savedPhoneContact.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), removeEmailResponse.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .getContactOnCooperationWithHttpInfo(cooperation.getId(), savedPhoneContact.getId()))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Contact with 'id: " + savedPhoneContact.getId() + "' is not found");
    }

    @Test
    void deletingNonExistentAnyContactTest() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());
        Long wrongId = 10000L;

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .deleteContactOnCooperation(expectedCooperation.getId(), wrongId))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Can't find contact with given ID:" + wrongId);
    }

    @Test
    void passNullIdWhenDeleteAnyContactTest() throws ApiException {
        ReadCooperation expectedUser = cooperationApi.createCooperation(createCooperation());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .deleteContactOnCooperation(expectedUser.getId(), null))
            .withMessageContaining("Missing the required parameter 'id'" +
                " when calling deleteContactOnCooperation");
    }

    @Test
    void passNullUserIdWhenDeleteAnyContactTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationContactApi
                .deleteContactOnCooperation(null, 11L))
            .withMessageContaining("Missing the required parameter 'cooperationId'" +
                " when calling deleteContactOnCooperation");
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name("newCooperationTest")
            .usreo(RandomStringUtils.randomAlphabetic(10))
            .iban(RandomStringUtils.randomAlphabetic(20))
            .adminEmail("G.Y.Andreevich@gmail.com")
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

        createContact.add(new CreatePhoneContact()
            .phone("+380639999999")
            .type(ContactType.PHONE)
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

    private CreateContact createEmailContact(){
        return new CreateEmailContact()
            .email("newEmailContact@example.com")
            .main(false)
            .type(ContactType.EMAIL);
    }

    private CreateContact createPhoneContact() {
        return new CreatePhoneContact()
            .phone("+380639999995")
            .main(false)
            .type(ContactType.PHONE);
    }

    private void assertEmailContact(CreateContact expected, ReadContact actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertTrue(expected.getType().equals(ContactType.EMAIL) && actual.getType().equals(ContactType.EMAIL));
        assertEquals(((CreateEmailContact)expected).getEmail(), ((ReadEmailContact)actual).getEmail());
    }

    private void assertPhoneContact(CreateContact expected, ReadContact actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertTrue(expected.getType().equals(ContactType.PHONE) && actual.getType().equals(ContactType.PHONE));
        assertEquals(((CreatePhoneContact)expected).getPhone(), ((ReadPhoneContact)actual).getPhone());
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
