package com.softserveinc.ita.homeproject.api.tests.invitations;

import static org.assertj.core.api.Assertions.assertThat;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import com.softserveinc.ita.homeproject.api.tests.query.InvitationQuery;
import com.softserveinc.ita.homeproject.api.tests.query.UserQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.ApartmentApi;
import com.softserveinc.ita.homeproject.client.api.ApartmentInvitationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.HouseApi;
import com.softserveinc.ita.homeproject.client.api.InvitationsApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateApartment;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreateInvitation;
import com.softserveinc.ita.homeproject.client.model.InvitationType;
import com.softserveinc.ita.homeproject.client.model.ReadApartment;
import com.softserveinc.ita.homeproject.client.model.ReadApartmentInvitation;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.ReadInvitationWithApartment;
import com.softserveinc.ita.homeproject.client.model.ReadUser;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class QueryInvitationIT {

    private final InvitationsApi invitationsApi = new InvitationsApi(ApiClientUtil.getCooperationAdminClient());

    private final HouseApi houseApi = new HouseApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentApi apartmentApi = new ApartmentApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final ApartmentInvitationApi apartmentInvitationApi = new ApartmentInvitationApi(ApiClientUtil.getCooperationAdminClient());

    @BeforeAll
    void createInvitation() throws ApiException {
        CreateApartment createApartment = createApartment(3);
        List<CreateInvitation> apartmentInvitation = createApartment.getInvitations();

//        ReadApartmentInvitation invitation = apartmentInvitationApi.
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadHouse createdHouse = houseApi.createHouse(createdCooperation.getId(), createHouse());
        ReadApartment createdApartment = apartmentApi.createApartment(createdHouse.getId(), createApartment);


    }

    @Test
    void getAllInvitations() throws ApiException {

        List<ReadInvitationWithApartment> actualListInvitations = new InvitationQuery.Builder(invitationsApi)
            .pageNumber(1)
            .pageSize(10)
            .build()
            .perfom();

        assertThat(actualListInvitations).isNotEmpty();
    }

    private CreateApartment createApartment(int numberOfApartamentInvitations) {
        return new CreateApartment()
            .area(BigDecimal.valueOf(72.5))
            .number("15")
            .invitations(createApartmentInvitation(numberOfApartamentInvitations));
    }

    private List<CreateInvitation> createApartmentInvitation(int numberOfInvitations) {

        return  Stream.generate(CreateInvitation::new)
            .map(x -> x.email(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com")).type(InvitationType.APARTMENT))
            .limit(numberOfInvitations)
            .collect(Collectors.toList());
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

}
