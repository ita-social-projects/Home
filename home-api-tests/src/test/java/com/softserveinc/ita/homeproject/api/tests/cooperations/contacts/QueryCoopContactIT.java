package com.softserveinc.ita.homeproject.api.tests.cooperations.contacts;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.softserveinc.ita.homeproject.api.tests.query.CooperationContactQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationContactApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreatePhoneContact;
import com.softserveinc.ita.homeproject.client.model.ReadContact;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadEmailContact;
import com.softserveinc.ita.homeproject.client.model.ReadPhoneContact;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class QueryCoopContactIT {

    private final CooperationContactApi cooperationContactApi = new CooperationContactApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    @Test
    void getAllContactsAscSort() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(ReadContact::getId));
        assertEquals(Objects.requireNonNull(expectedCooperation.getContacts()).size(), queryContactsResponse.size());
    }

    @Test
    void getAllContactsFromNotExistingCooperation() {
        Long wrongCooperationId = 999999999L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> new CooperationContactQuery
                        .Builder(cooperationContactApi)
                        .cooperationId(wrongCooperationId)
                        .pageNumber(1)
                        .pageSize(10)
                        .build().perform())
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Cooperation with 'id: " + wrongCooperationId + "' is not found");
    }

    @Test
    void getAllContactsDescSort() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .sort("id,desc")
            .build().perform();

        assertThat(queryContactsResponse).isSortedAccordingTo(Comparator.comparing(ReadContact::getId).reversed());
        assertEquals(Objects.requireNonNull(expectedCooperation.getContacts()).size(), queryContactsResponse.size());
    }

    @Test
    void getAllContactsFilteredBy() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
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
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());
        ReadContact savedContact = cooperationContactApi
            .createContactOnCooperation(expectedCooperation.getId(), createEmailContact());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .id(savedContact.getId())
            .build().perform();

        assertTrue(queryContactsResponse.contains(savedContact));
    }

    @Test
    void getAllContactsByPhone() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPhoneContact savedContact =
            (ReadPhoneContact) cooperationContactApi
                .createContactOnCooperation(expectedCooperation.getId(), createPhoneContact());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .phone(savedContact.getPhone())
            .build().perform();

        assertTrue(queryContactsResponse.contains(savedContact));
    }

    @Test
    void getAllContactsByEmail() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());
        ReadEmailContact savedContact =
            (ReadEmailContact) cooperationContactApi
                .createContactOnCooperation(expectedCooperation.getId(), createEmailContact());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .email(savedContact.getEmail())
            .build().perform();

        assertTrue(queryContactsResponse.contains(savedContact));
    }

    @Test
    void getAllContactsByMain() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .main("true")
            .build().perform();

        queryContactsResponse.forEach(contact -> assertThat(contact.getMain()).isTrue());
    }

    @Test
    void getAllContactsByTypeEmail() throws ApiException {
        ReadCooperation expectedCooperation = cooperationApi.createCooperation(createCooperation());

        List<ReadContact> queryContactsResponse = new CooperationContactQuery
            .Builder(cooperationContactApi)
            .cooperationId(expectedCooperation.getId())
            .pageNumber(1)
            .pageSize(10)
            .type(ContactType.EMAIL)
            .build().perform();

        queryContactsResponse.forEach(contact -> assertThat(contact.getType()).isEqualTo(ContactType.EMAIL));
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

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name(RandomStringUtils.randomAlphabetic(5).concat(" Cooperation"))
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
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
