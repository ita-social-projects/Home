package com.softserveinc.ita.homeproject.api.tests.polls;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.BAD_REQUEST;
import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateHouse;
import com.softserveinc.ita.homeproject.model.CreatePoll;
import com.softserveinc.ita.homeproject.model.HouseLookup;
import com.softserveinc.ita.homeproject.model.PollStatus;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import com.softserveinc.ita.homeproject.model.UpdatePoll;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ConstantConditions")
class CooperationPollApiIT {

    static final Long COOPERATION_ID;

    static final Long SECOND_COOPERATION_ID;

    static final Long HOUSE_ONE_ID;

    static final Long HOUSE_TWO_ID;

    static final Long HOUSE_ID_FROM_NON_RELATED_COOPERATION;

    static final Long NONEXISTENT_HOUSE_ID = 10000003L;

    static final Long NONEXISTENT_POLL_ID = 10000003L;

    final static CooperationPollApi COOPERATION_POLL_API = new CooperationPollApi(ApiClientUtil.getClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;

    static {
        ReadCooperation cooperationOne = null;
        ReadCooperation cooperationTwo = null;
        try {
            cooperationOne = new CooperationApi(ApiClientUtil.getClient())
                .createCooperationWithHttpInfo(createCooperation()).getData();
            cooperationTwo = new CooperationApi(ApiClientUtil.getClient())
                .createCooperationWithHttpInfo(createCooperation()).getData();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        COOPERATION_ID = cooperationOne.getId();
        SECOND_COOPERATION_ID = cooperationTwo.getId();
        HOUSE_ONE_ID = cooperationOne.getHouses().get(0).getId();
        HOUSE_TWO_ID = cooperationOne.getHouses().get(1).getId();
        HOUSE_ID_FROM_NON_RELATED_COOPERATION = cooperationTwo.getHouses().get(0).getId();
    }

    static CreatePoll createPoll() {
        LocalDateTime completionDate = LocalDateTime.now()
            .truncatedTo(ChronoUnit.MINUTES)
            .plusDays(MIN_POLL_DURATION_IN_DAYS)
            .plusMinutes(1L);
        return new CreatePoll()
            .header("Poll for our houses")
            .type(PollType.SIMPLE)
            .completionDate(completionDate)
            .addHousesItem(new HouseLookup().id(HOUSE_ONE_ID))
            .addHousesItem(new HouseLookup().id(HOUSE_TWO_ID));
    }

    private static CreatePoll createPollWithNonExistingHouse() {
        return createPoll()
            .addHousesItem(new HouseLookup().id(NONEXISTENT_HOUSE_ID));
    }

    private static CreatePoll createPollWithCompletionDateLessThanTwoDaysTest() {
        return createPoll()
            .completionDate(LocalDateTime.now().plusDays(1L));
    }

    private static CreatePoll createPollWithHouseFromNonRelatedCooperation() {
        return createPoll()
            .addHousesItem(new HouseLookup().id(HOUSE_ID_FROM_NON_RELATED_COOPERATION));
    }

    private static Address createAddress() {
        return new Address().city("Dnepr")
            .district("District")
            .houseBlock("block")
            .houseNumber(RandomStringUtils.randomNumeric(3))
            .region("Dnipro")
            .street("street")
            .zipCode("zipCode");
    }

    private static CreateHouse createHouse() {
        return new CreateHouse()
            .quantityFlat(96)
            .houseArea(BigDecimal.valueOf(4348.8))
            .adjoiningArea(400)
            .address(createAddress());
    }

    static CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name("newCooperationTest")
            .usreo(RandomStringUtils.randomAlphabetic(10))
            .iban(RandomStringUtils.randomAlphabetic(20))
            .adminEmail("G.Y.Andreevich@gmail.com")
            .address(createAddress())
            .addHousesItem(createHouse())
            .addHousesItem(createHouse());
    }

    static UpdatePoll updatePoll() {
        LocalDateTime completionDate = LocalDateTime.now()
            .truncatedTo(ChronoUnit.MINUTES)
            .plusDays(MIN_POLL_DURATION_IN_DAYS)
            .plusMinutes(1L);
        return new UpdatePoll()
            .header("Updated poll for our houses")
            .completionDate(completionDate)
            .status(PollStatus.SUSPENDED);
    }

    private static UpdatePoll updatePollWithWrongCompletionDate() {
        LocalDateTime completionDate = LocalDateTime.now()
            .truncatedTo(ChronoUnit.MINUTES)
            .plusDays(MIN_POLL_DURATION_IN_DAYS - 1);
        return updatePoll().completionDate(completionDate);
    }

    private static UpdatePoll updatePollWithNotAllowedStatus() {
        return updatePoll().status(PollStatus.COMPLETED);
    }

    @Test
    void createCooperationPollTest() throws ApiException {
        CreatePoll createPoll = createPoll();
        ApiResponse<ReadPoll> response = COOPERATION_POLL_API
            .createCooperationPollWithHttpInfo(COOPERATION_ID, createPoll);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertPoll(createPoll, response.getData());
    }

    @Test
    void createCooperationPollWithNonExistingHouseTest() {
        CreatePoll createPoll = createPollWithNonExistingHouse();
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .createCooperationPollWithHttpInfo(COOPERATION_ID, createPoll))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("House with 'id: " + NONEXISTENT_HOUSE_ID + "' is not found");
    }

    @Test
    void createCooperationPollWithHouseFromNonRelatedCooperationTest() {
        CreatePoll createPoll = createPollWithHouseFromNonRelatedCooperation();
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .createCooperationPollWithHttpInfo(COOPERATION_ID, createPoll))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("House with 'id: " + HOUSE_ID_FROM_NON_RELATED_COOPERATION + "' is not found");
    }

    @Test
    void createCooperationPollWithCompletionDateLessThanTwoDaysTest() {
        CreatePoll createPoll = createPollWithCompletionDateLessThanTwoDaysTest();
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .createCooperationPollWithHttpInfo(COOPERATION_ID, createPoll))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Completion date of the poll has not to be less than 2 days after creation");
    }

    @Test
    void getPollTest() throws ApiException {
        CreatePoll createPoll = createPoll();
        ReadPoll expectedPoll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll);
        ApiResponse<ReadPoll> response = COOPERATION_POLL_API
            .getCooperationPollWithHttpInfo(COOPERATION_ID, expectedPoll.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertPoll(createPoll, response.getData());
    }

    @Test
    void getNonExistingPollTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .getCooperationPollWithHttpInfo(COOPERATION_ID, NONEXISTENT_POLL_ID))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Poll with 'id: " + NONEXISTENT_POLL_ID + "' is not found");
    }

    @Test
    void getPollFromNonRelatedCooperation() throws ApiException {
        CreatePoll createPoll = createPoll();
        ReadPoll expectedPoll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll);
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .getCooperationPollWithHttpInfo(SECOND_COOPERATION_ID, expectedPoll.getId()))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Poll with 'id: " + expectedPoll.getId() + "' is not found");
    }

    @Test
    void deletePollTest() throws ApiException {
        ReadPoll deletedPoll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll());
        ApiResponse<Void> response = COOPERATION_POLL_API
            .deleteCooperationPollWithHttpInfo(COOPERATION_ID, deletedPoll.getId());
        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API.getCooperationPoll(COOPERATION_ID, deletedPoll.getId()))
            .matches((actual) -> actual.getCode() == NOT_FOUND);
    }

    @Test
    void updatePollTest() throws ApiException {
        ReadPoll pollToUpdate = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll());
        UpdatePoll updatePoll = updatePoll();
        ApiResponse<ReadPoll> response = COOPERATION_POLL_API
            .updateCooperationPollWithHttpInfo(COOPERATION_ID, pollToUpdate.getId(), updatePoll);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertUpdatedPoll(updatePoll, response.getData());
    }

    @Test
    void updatePollWithWrongCompletionDateTest() throws ApiException {
        ReadPoll pollToUpdate = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll());
        UpdatePoll updatePoll = updatePollWithWrongCompletionDate();
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .updateCooperationPollWithHttpInfo(COOPERATION_ID, pollToUpdate.getId(), updatePoll))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Completion date of the poll has not to be less than 2 days after creation");
    }

    @Test
    void updatePollWithNotAllowedStatusTest() throws ApiException {
        ReadPoll pollToUpdate = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll());
        UpdatePoll updatePoll = updatePollWithNotAllowedStatus();
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .updateCooperationPollWithHttpInfo(COOPERATION_ID, pollToUpdate.getId(), updatePoll))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Poll status can't be changed to 'completed'");
    }

    private void assertPoll(CreatePoll expected, ReadPoll actual) {
        List<Long> expectedHouseIdList = expected.getHouses()
            .stream().map(HouseLookup::getId).sorted().collect(Collectors.toList());
        List<Long> actualHouseIdList = actual.getPolledHouses()
            .stream().map(ReadHouse::getId).sorted().collect(Collectors.toList());
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getHeader(), actual.getHeader());
        assertEquals(expected.getCompletionDate(), actual.getCompletionDate());
        assertEquals(expectedHouseIdList, actualHouseIdList);
    }

    private void assertUpdatedPoll(UpdatePoll expected, ReadPoll actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getHeader(), actual.getHeader());
        assertEquals(expected.getStatus(), actual.getStatus());
        assertEquals(expected.getCompletionDate(), actual.getCompletionDate());
    }
}
