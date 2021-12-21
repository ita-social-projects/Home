package com.softserveinc.ita.homeproject.api.tests.invitations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import com.softserveinc.ita.homeproject.api.tests.query.InvitationQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.ApartmentInvitationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.api.InvitationsApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateApartment;
import com.softserveinc.ita.homeproject.client.model.CreateApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreateInvitation;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadApartment;
import com.softserveinc.ita.homeproject.client.model.ReadApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.ReadInvitation;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QueryInvitationIT {

    private final InvitationsApi invitationsApi = new InvitationsApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentInvitationApi apartmentInvitationApi =
        new ApartmentInvitationApi(ApiClientUtil.getCooperationAdminClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private ReadApartmentInvitation readApartmentInvitation;

    private ReadCooperation readCooperation;

    @BeforeAll
    void createInvitation() throws ApiException {
        CreateApartment createApartment = createApartmentWithoutInvitation();
        CreateCooperation createCooperation = createCooperation();

        readCooperation = cooperationApi.createCooperation(createCooperation);
        ReadHouse createdHouse = houseApi.createHouse(readCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(), createApartment);

        readApartmentInvitation = apartmentInvitationApi
            .createInvitation(createdApartment.getId(), (CreateApartmentInvitation) createInvitationApartment());
    }

    @Test
    void getAllInvitations() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .build().perform();

        assertThat(queryResponse).isNotEmpty();
    }

    @Test
    void getAllInvitationsAscSort() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,asc")
            .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadInvitation::getId));
    }

    @Test
    void getAllInvitationsDescSort() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .sort("id,dsc")
            .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadInvitation::getId).reversed());
    }

    @Test
    void getInvitationByInvitationId() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .id(readApartmentInvitation.getId())
            .build().perform();
        queryResponse.forEach(element -> assertEquals(readApartmentInvitation.getId(), element.getId()));
    }

    @Test
    void getInvitationByInvitationIdAndType() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .id(readApartmentInvitation.getId())
            .invitationType(InvitationType.APARTMENT)
            .build().perform();
        queryResponse.forEach(element -> assertEquals(readApartmentInvitation.getId(), element.getId()));
    }

    @Test
    void getInvitationByApartmentInvitationType() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .invitationType(InvitationType.APARTMENT)
            .build().perform();
        queryResponse.forEach(element -> assertEquals(InvitationType.APARTMENT, element.getType()));
    }

    @Test
    void getInvitationByCooperationInvitationType() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .invitationType(InvitationType.COOPERATION)
            .build().perform();
        queryResponse.forEach(element -> assertEquals(InvitationType.COOPERATION, element.getType()));
    }

    @Test
    void getInvitationByApartmentId() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .apartmentId(Objects.requireNonNull(readApartmentInvitation.getApartment()).getId())
            .build().perform();
        queryResponse.forEach(element -> assertEquals(readApartmentInvitation.getId(), element.getId()));
    }

    @Test
    void getInvitationByEmail() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .email(readApartmentInvitation.getEmail())
            .build().perform();
        queryResponse.forEach(element -> assertEquals(readApartmentInvitation.getId(), element.getId()));
    }

    @Test
    void getInvitationByCooperationId() throws ApiException {
        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .cooperationId(readCooperation.getId())
            .build().perform();
        assertThat(queryResponse).isNotEmpty();
        queryResponse.forEach(element -> assertEquals(InvitationType.COOPERATION, element.getType()));
    }

    @Test
    void getInvitationsFromNotExistingApartment() throws ApiException {
        Long wrongApartmentId = 999999999L;

        List<ReadInvitation> queryResponse = new InvitationQuery
            .Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .apartmentId(wrongApartmentId)
            .build().perform();
        assertThat(queryResponse).isEmpty();
    }

    private CreateInvitation createInvitationApartment() {
        return new CreateApartmentInvitation()
            .email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
            .type(InvitationType.APARTMENT);
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name("newCooperationTest")
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
            .address(createAddress());
    }

    private CreateHouse createHouse() {
        return new CreateHouse()
            .adjoiningArea(500)
            .houseArea(BigDecimal.valueOf(500.0))
            .quantityFlat(50)
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

    private CreateApartment createApartmentWithoutInvitation() {
        return new CreateApartment()
            .area(BigDecimal.valueOf(72.5))
            .number("15");
    }

}
