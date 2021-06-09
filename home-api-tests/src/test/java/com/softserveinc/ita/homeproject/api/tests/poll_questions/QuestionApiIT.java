package com.softserveinc.ita.homeproject.api.tests.poll_questions;

import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import com.softserveinc.ita.homeproject.ApiException;
import com.softserveinc.ita.homeproject.ApiResponse;
import com.softserveinc.ita.homeproject.api.CooperationApi;
import com.softserveinc.ita.homeproject.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.api.PollQuestionApi;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.model.Address;
import com.softserveinc.ita.homeproject.model.CreateAdviceQuestion;
import com.softserveinc.ita.homeproject.model.CreateCooperation;
import com.softserveinc.ita.homeproject.model.CreateMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.CreatePoll;
import com.softserveinc.ita.homeproject.model.CreateQuestion;
import com.softserveinc.ita.homeproject.model.CreateUpdateAnswerVariant;
import com.softserveinc.ita.homeproject.model.PollType;
import com.softserveinc.ita.homeproject.model.QuestionType;
import com.softserveinc.ita.homeproject.model.ReadCooperation;
import com.softserveinc.ita.homeproject.model.ReadHouse;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.ReadPoll;
import com.softserveinc.ita.homeproject.model.ReadQuestion;
import com.softserveinc.ita.homeproject.model.UpdateApartment;
import com.softserveinc.ita.homeproject.model.UpdateQuestion;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class QuestionApiIT {

    private final PollQuestionApi pollQuestionApi = new PollQuestionApi(ApiClientUtil.getClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getClient());

    private final CooperationPollApi cooperationPollApi = new CooperationPollApi(ApiClientUtil.getClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;

    @Test
    void createMultipleChoiceQuestionTest() throws ApiException {
        CreateMultipleChoiceQuestion createQuestion = (CreateMultipleChoiceQuestion) createMultipleChoiceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());

        ApiResponse<ReadQuestion> response = pollQuestionApi.createQuestionWithHttpInfo(createdPoll.getId(), createQuestion);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());

        assertQuestion(createQuestion, (ReadMultipleChoiceQuestion) response.getData());

    }

    @Test
    void createQuestionTest() throws ApiException {
        CreateQuestion createQuestion = createAdviceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());

        ApiResponse<ReadQuestion> response = pollQuestionApi.createQuestionWithHttpInfo(createdPoll.getId(), createQuestion);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertQuestion(createQuestion, response.getData());
    }

    @Test
    void createQuestionWithNonExistentTest() throws ApiException {
        CreateQuestion createQuestion = createAdviceQuestion();

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> pollQuestionApi
                        .createQuestionWithHttpInfo(wrongId, createQuestion))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Poll with 'id: " + wrongId + "' is not found");
    }

    @Test
    void getQuestionTest() throws ApiException {
        CreateQuestion createQuestion = createAdviceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll expectedPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadQuestion expectedQuestion = pollQuestionApi.createQuestion(expectedPoll.getId(), createQuestion);

        ApiResponse<ReadQuestion> response = pollQuestionApi.getQuestionWithHttpInfo(expectedPoll.getId(), expectedQuestion.getId());

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertQuestion(createQuestion, response.getData());
    }

    @Test
    void getNonExistentQuestionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll expectedPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());


        Long wrongId = 10000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> pollQuestionApi
                        .getQuestionWithHttpInfo(expectedPoll.getId(), wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("PollQuestion with 'id: " + wrongId + "' is not found");
    }

    @Test
    void updateQuestionTest() throws ApiException {
        CreateQuestion createQuestion = createAdviceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadQuestion createdQuestion = pollQuestionApi.createQuestion(createdPoll.getId(), createQuestion);

        UpdateQuestion updateQuestion = new UpdateQuestion()
                .question("Do you like updated question?")
                .type(QuestionType.ADVICE);

        ApiResponse<ReadQuestion> response = pollQuestionApi.updateQuestionWithHttpInfo(createdPoll.getId(), createdQuestion.getId(), updateQuestion);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());
        assertQuestion(createdQuestion, updateQuestion, response.getData());
    }

    @Test
    void updateNonExistentQuestion() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());

        UpdateQuestion updateQuestion = new UpdateQuestion()
                .question("Do you like updated question?")
                .type(QuestionType.ADVICE);

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> pollQuestionApi
                        .updateQuestionWithHttpInfo(createdPoll.getId(), wrongId, updateQuestion))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Question with 'id: " + wrongId +"' is not found");
    }


    static CreateQuestion createAdviceQuestion() {
        return new CreateAdviceQuestion()
                .question("Your proposal for your house?")
                .type(QuestionType.ADVICE);
    }

    static CreateQuestion createMultipleChoiceQuestion() {
        return new CreateMultipleChoiceQuestion()
                .maxAnswerCount(2)
                .answerVariants(createAnswerVariants())
                .type(QuestionType.MULTIPLE_CHOICE)
                .question("What color should we paint the door?");
    }

    static List<CreateUpdateAnswerVariant> createAnswerVariants() {
        List<CreateUpdateAnswerVariant> answerVariants = new ArrayList<>();
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("black"));
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("green"));
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("pink"));

        return answerVariants;
    }

    static CreatePoll createPoll() {
        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(MIN_POLL_DURATION_IN_DAYS)
                .plusMinutes(1L);
        return new CreatePoll()
                .header("Poll for our houses")
                .type(PollType.SIMPLE)
                .completionDate(completionDate);
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

    static CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name("newCooperationTest")
                .usreo(RandomStringUtils.randomAlphabetic(10))
                .iban(RandomStringUtils.randomAlphabetic(20))
                .adminEmail("G.Y.Andreevich@gmail.com")
                .address(createAddress());
    }

    private void assertQuestion(CreateQuestion expected, ReadQuestion actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getQuestion(), actual.getQuestion());
        assertEquals(expected.getType(), actual.getType());
    }

    private void assertQuestion(CreateMultipleChoiceQuestion expected, ReadMultipleChoiceQuestion actual) {
        assertNotNull(expected);
        assertNotNull(actual);
        assertEquals(expected.getQuestion(), actual.getQuestion());
        assertEquals(expected.getType(), actual.getType());
        assertEquals(expected.getMaxAnswerCount(), actual.getMaxAnswerCount());
    }

    private void assertQuestion(ReadQuestion saved, UpdateQuestion update, ReadQuestion updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getQuestion(), updated.getQuestion());
    }
}
