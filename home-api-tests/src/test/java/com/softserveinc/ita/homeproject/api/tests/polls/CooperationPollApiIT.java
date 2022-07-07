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

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.api.PollApi;
import com.softserveinc.ita.homeproject.client.api.PolledHouseApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateHouse;
import com.softserveinc.ita.homeproject.client.model.CreatePoll;
import com.softserveinc.ita.homeproject.client.model.HouseLookup;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadHouse;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.UpdatePoll;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

@SuppressWarnings("ConstantConditions")
class CooperationPollApiIT {

    static final Long COOPERATION_ID;

    static final Long SECOND_COOPERATION_ID;

    static final Long HOUSE_ONE_ID;

    static final Long HOUSE_TWO_ID;

    static final Long HOUSE_ID_FROM_NON_RELATED_COOPERATION;

    static final Long NOT_ADDED_HOUSE_ON_POLL_ID;

    static final Long NONEXISTENT_HOUSE_ID = 10000003L;

    static final Long NONEXISTENT_POLL_ID = 10000003L;

    static final Long NONEXISTENT_COOP_ID = 999999999L;

    final static CooperationPollApi COOPERATION_POLL_API =
        new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    private final PolledHouseApi POLLED_HOUSE_API = new PolledHouseApi(ApiClientUtil.getCooperationAdminClient());

    final static PollApi POLL_API = new PollApi(ApiClientUtil.getCooperationAdminClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;

    private final static Long MAX_POLL_DURATION_IN_DAYS = 15L;

    static final String WRONG_DATA_MESSAGE = "Can't add or remove house, invalid poll_id or house_id";

    static final String POLL_NOT_FOUND = "Poll with 'id: %d' is not found";

    static final String COOPERATION_NOT_FOUND = "Cooperation with 'id: %d' is not found";


    static {
        ReadCooperation cooperationOne = null;
        ReadCooperation cooperationTwo = null;
        try {
            cooperationOne = new CooperationApi(ApiClientUtil.getCooperationAdminClient())
                .createCooperationWithHttpInfo(createCooperation()).getData();
            cooperationTwo = new CooperationApi(ApiClientUtil.getCooperationAdminClient())
                .createCooperationWithHttpInfo(createCooperation()).getData();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        COOPERATION_ID = cooperationOne.getId();
        SECOND_COOPERATION_ID = cooperationTwo.getId();
        HOUSE_ONE_ID = cooperationOne.getHouses().get(0).getId();
        HOUSE_TWO_ID = cooperationOne.getHouses().get(1).getId();
        NOT_ADDED_HOUSE_ON_POLL_ID = cooperationOne.getHouses().get(2).getId();
        HOUSE_ID_FROM_NON_RELATED_COOPERATION = cooperationTwo.getHouses().get(0).getId();
    }

    static CreatePoll createPoll() {
        LocalDateTime completionDate = LocalDateTime.now()
            .truncatedTo(ChronoUnit.DAYS)
            .plusDays(MAX_POLL_DURATION_IN_DAYS);
        return new CreatePoll()
            .header("Poll for our houses")
            .type(PollType.SIMPLE)
            .creationDate(LocalDateTime.now()
                .truncatedTo(ChronoUnit.DAYS))
            .completionDate(completionDate)
            .addHousesItem(new HouseLookup().id(HOUSE_ONE_ID))
            .addHousesItem(new HouseLookup().id(HOUSE_TWO_ID))
            .description("Description");
    }

    static CreatePoll createPollWithCompletionDate(LocalDateTime completionDate) {
        return new CreatePoll()
            .header("Poll for our houses")
            .type(PollType.SIMPLE)
            .creationDate(completionDate)
            .completionDate(completionDate)
            .addHousesItem(new HouseLookup().id(HOUSE_ONE_ID))
            .addHousesItem(new HouseLookup().id(HOUSE_TWO_ID))
            .description("Description");
    }

    static CreatePoll createPollWithCreationDate(LocalDateTime creationDate) {
        return new CreatePoll()
            .header("Poll for our houses")
            .type(PollType.SIMPLE)
            .creationDate(creationDate)
            .completionDate(creationDate.plusDays(15))
            .addHousesItem(new HouseLookup().id(HOUSE_ONE_ID))
            .addHousesItem(new HouseLookup().id(HOUSE_TWO_ID))
            .description("Description");
    }

    private static CreatePoll createPollWithNonExistingHouse() {
        return createPoll()
            .addHousesItem(new HouseLookup().id(NONEXISTENT_HOUSE_ID));
    }

    private static CreatePoll createPollWithCreationDateInPast(){
        return createPoll().creationDate(LocalDateTime.now().minusDays(2));
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
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail(RandomStringUtils.randomAlphabetic(10).concat("@gmail.com"))
            .address(createAddress())
            .addHousesItem(createHouse())
            .addHousesItem(createHouse())
            .addHousesItem(createHouse());
    }

    static UpdatePoll updatePoll() {
        return new UpdatePoll()
            .header("header")
            .description("updated description")
            .creationDate(LocalDateTime.now().plusDays(1L).truncatedTo(ChronoUnit.DAYS))
            .status(PollStatus.DRAFT);
    }

    static UpdatePoll updatePollWithCreationDate(LocalDateTime creationDate) {
        return new UpdatePoll()
            .header("header")
            .description("updated description")
            .creationDate(creationDate)
            .status(PollStatus.DRAFT);
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
    void addPolledHouseTest() throws ApiException {
        HouseLookup houseLookup = new HouseLookup().id(NOT_ADDED_HOUSE_ON_POLL_ID);
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        ApiResponse<Void> addHouseResponse = POLLED_HOUSE_API
            .createPolledHouseWithHttpInfo(poll.getId(), houseLookup);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), addHouseResponse.getStatusCode());
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
    void addNonExistingPolledHouseTest() throws ApiException {
        HouseLookup houseLookup = new HouseLookup().id(NONEXISTENT_HOUSE_ID);
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLLED_HOUSE_API
                .createPolledHouseWithHttpInfo(poll.getId(), houseLookup))
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
    void createCooperationPollWithCreationDateInPast() {
        CreatePoll createPoll = createPollWithCreationDateInPast();
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> COOPERATION_POLL_API
                        .createCooperationPollWithHttpInfo(COOPERATION_ID, createPoll))
                .matches(exception -> exception.getCode() == BAD_REQUEST)
                .withMessageContaining("Poll can`t be created in the past");
    }

    @Test
    void addExistingPolledHouseFromNonRelatedCooperationTest() throws ApiException {
        HouseLookup houseLookup = new HouseLookup().id(HOUSE_ID_FROM_NON_RELATED_COOPERATION);
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLLED_HOUSE_API
                .createPolledHouseWithHttpInfo(poll.getId(), houseLookup))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(WRONG_DATA_MESSAGE);
    }

    @Test
    void addAlreadyAddedPolledHouseTest() throws ApiException {
        HouseLookup houseLookup = new HouseLookup().id(HOUSE_ONE_ID);
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLLED_HOUSE_API
                .createPolledHouseWithHttpInfo(poll.getId(), houseLookup))
            .matches((actual) -> actual.getCode() == BAD_REQUEST)
            .withMessageContaining(
                String.format("House with id:%s already exists in poll with id:%s", HOUSE_ONE_ID, poll.getId()));
    }

    @Disabled("Specification requires exactly 15 days from start date")
    void createCooperationPollWithCompletionDateLessThanTwoDaysTest() {
        CreatePoll createPoll = createPollWithCompletionDateLessThanTwoDaysTest();
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .createCooperationPollWithHttpInfo(COOPERATION_ID, createPoll))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining("Completion date of the poll has not to be less than 2 days after creation");
    }

    @Test
    void getPollFromCooperationTest() throws ApiException {
        CreatePoll createPoll = createPoll();
        ReadPoll expectedPoll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll);
        ApiResponse<ReadPoll> response = COOPERATION_POLL_API
            .getCooperationPollWithHttpInfo(COOPERATION_ID, expectedPoll.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertPoll(createPoll, response.getData());
    }

    @Test
    void getPolledHouseTest() throws ApiException {
        CreatePoll createPoll = createPoll();
        ReadPoll expectedPoll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll);

        ApiResponse<ReadHouse> response = POLLED_HOUSE_API
            .getPolledHouseWithHttpInfo(expectedPoll.getId(), HOUSE_ONE_ID);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertEquals(expectedPoll.getPolledHouses().get(0), response.getData());
    }

    @Test
    void getNonExistingPollFromCooperationTest() {
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
        LocalDateTime creationDate = LocalDateTime.now().truncatedTo(ChronoUnit.DAYS).plusDays(1L);
        ReadPoll pollToUpdate =
            COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPollWithCreationDate(creationDate));
        UpdatePoll updatePoll =
            updatePollWithCreationDate(creationDate.truncatedTo(ChronoUnit.DAYS));
        ApiResponse<ReadPoll> response = COOPERATION_POLL_API
            .updateCooperationPollWithHttpInfo(COOPERATION_ID, pollToUpdate.getId(), updatePoll);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertUpdatedPoll(updatePoll, response.getData());
    }

    @Test
    void removePolledHouseTest() throws ApiException {
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        ApiResponse<Void> response = POLLED_HOUSE_API.deletePolledHouseWithHttpInfo(poll.getId(), HOUSE_TWO_ID);

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLLED_HOUSE_API
                .getPolledHouseWithHttpInfo(poll.getId(), HOUSE_TWO_ID))
            .matches((actual) -> actual.getCode() == NOT_FOUND)
            .withMessageContaining("House with 'id: " + HOUSE_TWO_ID + "' is not found");
    }

    @Test
    void removeNotExistingPolledHouseTest() throws ApiException {
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLLED_HOUSE_API
                .deletePolledHouseWithHttpInfo(poll.getId(), NONEXISTENT_HOUSE_ID))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("House with 'id: " + NONEXISTENT_HOUSE_ID + "' is not found");
    }

    @Test
    void removeExistingPolledHouseFromNonRelatedCooperationTest() throws ApiException {
        CreatePoll createPoll = createPoll();

        ReadPoll poll = COOPERATION_POLL_API
            .createCooperationPoll(COOPERATION_ID, createPoll);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLLED_HOUSE_API
                .deletePolledHouseWithHttpInfo(poll.getId(), HOUSE_ID_FROM_NON_RELATED_COOPERATION))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(WRONG_DATA_MESSAGE);
    }

    @Test
    void getPollTest() throws ApiException {
        CreatePoll createPoll = createPoll();
        ReadPoll expectedPoll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll);
        ApiResponse<ReadPoll> response = POLL_API.getPollWithHttpInfo(expectedPoll.getId());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertPoll(createPoll, response.getData());
    }

    @Test
    void getNonExistingPollTest() {
        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> POLL_API
                .getPollWithHttpInfo(NONEXISTENT_POLL_ID))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining("Poll with 'id: " + NONEXISTENT_POLL_ID + "' is not found");
    }

    @Disabled("Should be exception. Created task#284.")
    @Test
    void deleteCooperationPollFromNotExistingCooperationTest() throws ApiException {
        ReadPoll poll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .deleteCooperationPoll(NONEXISTENT_COOP_ID, poll.getId()))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(String.format(POLL_NOT_FOUND, poll.getId()));
    }

    @Disabled("Should be exception. Created task#284.")
    @Test
    void deleteCooperationPollFromNotRelatedCooperationTest() throws ApiException {
        ReadPoll poll = COOPERATION_POLL_API.createCooperationPoll(COOPERATION_ID, createPoll());

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> COOPERATION_POLL_API
                .deleteCooperationPoll(SECOND_COOPERATION_ID, poll.getId()))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(String.format(POLL_NOT_FOUND, poll.getId()));
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
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getCreationDate(), actual.getCreationDate());
    }

    private void assertUpdatedPoll(UpdatePoll expected, ReadPoll actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getHeader(), actual.getHeader());
        assertEquals(expected.getCreationDate(), actual.getCreationDate());
        assertEquals(expected.getHeader(), actual.getHeader());
        assertEquals(expected.getDescription(), actual.getDescription());
    }
}
