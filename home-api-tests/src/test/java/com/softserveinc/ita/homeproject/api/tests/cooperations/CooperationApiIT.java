package com.softserveinc.ita.homeproject.api.tests.cooperations;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.ContactType;
import com.softserveinc.ita.homeproject.client.model.CreateContact;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateEmailContact;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.UpdateCooperation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

class CooperationApiIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    @Test
    void createCooperationTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        ApiResponse<ReadCooperation> response = cooperationApi.createCooperationWithHttpInfo(createCoop);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertCooperation(createCoop, response.getData());
    }

    @Test
    void getCooperationTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        ReadCooperation savedCooperation = cooperationApi.createCooperation(createCoop);
        ApiResponse<ReadCooperation> response = cooperationApi.getCooperationWithHttpInfo(savedCooperation.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertCooperation(createCoop, response.getData());
    }

    @Test
    void updateCooperationTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        ReadCooperation savedCooperation = cooperationApi.createCooperation(createCoop);

        UpdateCooperation updateCoop = new UpdateCooperation()
            .name("upd")
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .address(new Address()
                .city("upd")
                .district("upd")
                .houseBlock("upd")
                .houseNumber("upd")
                .region("upd")
                .street("upd")
                .zipCode("upd"));

        ApiResponse<ReadCooperation> response = cooperationApi
            .updateCooperationWithHttpInfo(savedCooperation.getId(), updateCoop);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertCooperation(savedCooperation, updateCoop, response.getData());
    }

    @Disabled("This test is disabled until we decide for the System Administrator Role")
    @Test
    void deleteCooperationTest() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());

        ApiResponse<Void> response = cooperationApi.deleteCooperationWithHttpInfo(readCoop.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationApi.getCooperation(readCoop.getId()))
            .matches((actual) -> actual.getCode() == NOT_FOUND);
    }

    @Test
    void createCooperationInvalidNameTest() {
        CreateCooperation createCoopInvalidName = new CreateCooperation()
            .name("Cooperation Cooperation Cooperation Cooperation Cooperation Cooperation Cooperation")
            .usreo("25252525")
            .iban("UA250820210147250819980000000")
            .adminEmail("test.receive.messages@gmail.com")
            .address(createAddress())
            .houses(createHouseList());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationApi.createCooperation(createCoopInvalidName))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `name` is invalid - size must be between 1 and 50 signs.");
    }

    @Test
    void createCooperationInvalidUsreoTest() {
        CreateCooperation createCoopInvalidUsreo = new CreateCooperation()
            .name("name")
            .usreo("123456789101112131415")
            .iban("UA250820210147250819980000000")
            .adminEmail("test.receive.messages@gmail.com")
            .address(createAddress())
            .houses(createHouseList());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationApi.createCooperation(createCoopInvalidUsreo))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `usreo` is invalid - must meet the rule.");
    }

    @Test
    void createCooperationInvalidIbanTest() {
        CreateCooperation createCoopInvalidIban = new CreateCooperation()
            .name("name")
            .usreo("25252525")
            .iban("12345678910111213141516171819202122232425")
            .adminEmail("test.receive.messages@gmail.com")
            .address(createAddress())
            .houses(createHouseList());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> cooperationApi.createCooperation(createCoopInvalidIban))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining("Parameter `iban` is invalid - must meet the rule.");
    }

    @Test
    void createCooperationWithExistingUsreoTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        cooperationApi.createCooperation(createCoop);

        CreateCooperation createCoopSameUsreo = new CreateCooperation()
                .name("name")
                .usreo(createCoop.getUsreo())
                .iban("UA213223130000026007233566011")
                .adminEmail("test.receive.messages@gmail.com")
                .address(createAddress())
                .houses(createHouseList());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> cooperationApi.createCooperation(createCoopSameUsreo))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("The cooperations must be unique. Key `usreo`=`" + createCoopSameUsreo.getUsreo() + "` already exists.");
    }

    @Test
    void createCooperationWithExistingIbanTest() throws ApiException {
        CreateCooperation createCoop = createCooperation();
        cooperationApi.createCooperation(createCoop);

        CreateCooperation createCoopSameIban = new CreateCooperation()
                .name("name")
                .usreo("26573474")
                .iban(createCoop.getIban())
                .adminEmail("test.receive.messages@gmail.com")
                .address(createAddress())
                .houses(createHouseList());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> cooperationApi.createCooperation(createCoopSameIban))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("The cooperations must be unique. Key `iban`=`" + createCoopSameIban.getIban() + "` already exists.");
    }

    @Test
    void createCooperationWithExistingUsreoAndIbanTest() {
        CreateCooperation createCoopSameUsreoAndIban = new CreateCooperation()
                .name("name")
                .usreo("11111111")
                .iban("UA111111111111111111111111111")
                .adminEmail("test.receive.messages@gmail.com")
                .address(createAddress())
                .houses(createHouseList());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> cooperationApi.createCooperation(createCoopSameUsreoAndIban))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("The cooperations must be unique. Key `usreo`=`" + createCoopSameUsreoAndIban.getUsreo() + "` already exists.");
    }

    @Test
    void createCooperationWithNullParameterTest() {
        CreateCooperation createCoopSame = new CreateCooperation()
                .name("name")
                .usreo(RandomStringUtils.randomNumeric(8))
                .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
                .adminEmail("test.receive.messages@gmail.com")
                .address(createAddressWithNull());

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> cooperationApi.createCooperation(createCoopSame))
                .matches((actual) -> actual.getCode() == BAD_REQUEST)
                .withMessageContaining("Parameter `city` is invalid - must not be null.");
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

    private Address createAddressWithNull() {
        return new Address().city(null)
                .district("District")
                .houseBlock("block")
                .houseNumber("number")
                .region("Dnipro")
                .street("street")
                .zipCode("zipCode");
    }

    private void assertCooperation(CreateCooperation expected, ReadCooperation actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getIban(), actual.getIban());
        assertEquals(expected.getUsreo(), actual.getUsreo());
        assertEquals(expected.getAddress(), actual.getAddress());
    }

    private void assertCooperation(ReadCooperation saved, UpdateCooperation update, ReadCooperation updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getName(), updated.getName());
        assertEquals(update.getIban(), updated.getIban());
        assertEquals(update.getUsreo(), updated.getUsreo());
        assertEquals(update.getAddress(), updated.getAddress());
    }

}
