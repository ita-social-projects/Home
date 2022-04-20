package com.softserveinc.ita.homeproject.api.tests.poll_questions;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.api.PollQuestionApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateAdviceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateDoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreatePoll;
import com.softserveinc.ita.homeproject.client.model.CreateQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateUpdateAnswerVariant;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.QuestionType;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadDoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.ReadQuestion;
import com.softserveinc.ita.homeproject.client.model.UpdateDoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.UpdateMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.UpdateQuestion;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class QuestionApiIT {

    private final PollQuestionApi pollQuestionApi = new PollQuestionApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationPollApi cooperationPollApi = new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

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
    void createDoubleChoiceQuestionTest() throws ApiException{
        CreateDoubleChoiceQuestion createQuestion = (CreateDoubleChoiceQuestion) createDoubleChoiceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());

        ApiResponse<ReadQuestion> response = pollQuestionApi.createQuestionWithHttpInfo(createdPoll.getId(), createQuestion);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());

        assertQuestion(createQuestion, (ReadDoubleChoiceQuestion) response.getData());
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
    void createQuestionWithNonExistentTest() {
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
    void updateMultipleChoiceQuestionTest() throws ApiException {
        CreateMultipleChoiceQuestion createQuestion = (CreateMultipleChoiceQuestion) createMultipleChoiceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadQuestion createdQuestion = pollQuestionApi.createQuestion(createdPoll.getId(), createQuestion);

        UpdateQuestion updateQuestion = new UpdateMultipleChoiceQuestion()
                .maxAnswerCount(2)
                .answerVariants(updateAnswerVariants())
                .type(QuestionType.MULTIPLE_CHOICE)
                .question("Do you like our cooperation?");


        ApiResponse<ReadQuestion> response = pollQuestionApi.updateQuestionWithHttpInfo(createdPoll.getId(), createdQuestion.getId(), updateQuestion);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertMultipleQuestion(createdQuestion,(UpdateMultipleChoiceQuestion) updateQuestion, (ReadMultipleChoiceQuestion) response.getData());
    }

    @Test
    void updateDoubleChoiceQuestionTest() throws ApiException {
        CreateDoubleChoiceQuestion createQuestion = (CreateDoubleChoiceQuestion) createDoubleChoiceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadQuestion createdQuestion = pollQuestionApi.createQuestion(createdPoll.getId(), createQuestion);

        UpdateQuestion updateQuestion = new UpdateDoubleChoiceQuestion()
            .type(QuestionType.DOUBLE_CHOICE)
            .question("Do we need this poll");

        ApiResponse<ReadQuestion> response = pollQuestionApi.updateQuestionWithHttpInfo(createdPoll.getId(), createdQuestion.getId(), updateQuestion);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatusCode());

        assertDoubleQuestion(createdQuestion, (UpdateDoubleChoiceQuestion) updateQuestion, (ReadDoubleChoiceQuestion) response.getData());
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

    @Test
    void deleteQuestionTest() throws ApiException {
        CreateQuestion createQuestion = createMultipleChoiceQuestion();

        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadQuestion createdQuestion = pollQuestionApi.createQuestion(createdPoll.getId(), createQuestion);

        ApiResponse<Void> response = pollQuestionApi.deleteQuestionWithHttpInfo(createdPoll.getId(), createdQuestion.getId());

        assertEquals(Response.Status.NO_CONTENT.getStatusCode(), response.getStatusCode());
        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> pollQuestionApi.getQuestion(createdPoll.getId(), createdQuestion.getId()));
    }

    @Test
    void deleteNonExistentQuestion() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());

        Long wrongId = 1000000L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> pollQuestionApi
                        .deleteQuestionWithHttpInfo(createdPoll.getId(), wrongId))
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Question with 'id: " + wrongId +"' is not found");
    }

    private static CreateQuestion createAdviceQuestion() {
        return new CreateAdviceQuestion()
                .question("Your proposal for your house?")
                .type(QuestionType.ADVICE);
    }

    private static CreateQuestion createMultipleChoiceQuestion() {
        return new CreateMultipleChoiceQuestion()
                .maxAnswerCount(2)
                .answerVariants(createAnswerVariants())
                .type(QuestionType.MULTIPLE_CHOICE)
                .question("What color should we paint the door?");
    }

    private static CreateQuestion createDoubleChoiceQuestion() {
        return new CreateDoubleChoiceQuestion()
            .type(QuestionType.DOUBLE_CHOICE)
            .question("Should we paint the wall?");
    }

    private static List<CreateUpdateAnswerVariant> createAnswerVariants() {
        List<CreateUpdateAnswerVariant> answerVariants = new ArrayList<>();
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("black"));
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("green"));
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("pink"));

        return answerVariants;
    }

    private static List<CreateUpdateAnswerVariant> updateAnswerVariants() {
        List<CreateUpdateAnswerVariant> answerVariants = new ArrayList<>();
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("yes"));
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("no"));
        answerVariants.add(new CreateUpdateAnswerVariant()
                .answer("idk"));

        return answerVariants;
    }

    private static CreatePoll createPoll() {
        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(MIN_POLL_DURATION_IN_DAYS)
                .plusMinutes(5L);
        return new CreatePoll()
                .header("Poll for our houses")
                .type(PollType.SIMPLE)
                .completionDate(completionDate)
                .description("Description")
                .creationDate(LocalDateTime.now());
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

    private static CreateCooperation createCooperation() {
        return new CreateCooperation()
                .name(RandomStringUtils.randomAlphabetic(12).concat(" Cooperation"))
                .usreo(RandomStringUtils.randomNumeric(8))
                .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
                .adminEmail("test.ita.emails@gmail.com")
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

    private void assertMultipleQuestion(ReadQuestion saved, UpdateMultipleChoiceQuestion update, ReadMultipleChoiceQuestion updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getQuestion(), updated.getQuestion());
        assertEquals(update.getMaxAnswerCount(), updated.getMaxAnswerCount());
        assertEquals(update.getType(), updated.getType());
        assertAnswer(update, updated);

    }

    private void assertDoubleQuestion(ReadQuestion saved, UpdateDoubleChoiceQuestion update, ReadDoubleChoiceQuestion updated) {
        assertNotNull(saved);
        assertNotNull(update);
        assertNotNull(updated);
        assertEquals(update.getQuestion(), updated.getQuestion());
        assertEquals(update.getType(), updated.getType());
    }

    private void assertAnswer(UpdateMultipleChoiceQuestion update,ReadMultipleChoiceQuestion updated) {
        for (int i=0; i<update.getAnswerVariants().size();i++){
           assertEquals(update.getAnswerVariants().get(i).getAnswer(), Objects.requireNonNull(updated.getAnswerVariants()).get(i).getAnswer());
        }
    }
}
