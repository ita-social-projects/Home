package com.softserveinc.ita.homeproject.api.tests.poll_questions;

import static com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.softserveinc.ita.homeproject.api.tests.query.PollQuestionQuery;
import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.api.PollQuestionApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.CreateAdviceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreatePoll;
import com.softserveinc.ita.homeproject.client.model.CreateQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateUpdateAnswerVariant;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.QuestionType;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.ReadQuestion;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class QueryQuestionIT {

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationPollApi cooperationPollApi = new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    private final PollQuestionApi pollQuestionApi = new PollQuestionApi(ApiClientUtil.getCooperationAdminClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;

    @Test
    void getAllQuestionsAscSort() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadPoll readPoll = cooperationPollApi.createCooperationPoll(readCoop.getId(), createPoll());

        pollQuestionApi.createQuestion(readPoll.getId(), createAdviceQuestion());
        pollQuestionApi.createQuestion(readPoll.getId(), createMultipleChoiceQuestion());


        List<ReadQuestion> queryResponse = new PollQuestionQuery.Builder(pollQuestionApi)
                .pollId(readPoll.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,asc")
                .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadQuestion::getId));
    }

    @Test
    void getAllQuestionsDescSort() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadPoll readPoll = cooperationPollApi.createCooperationPoll(readCoop.getId(), createPoll());

        pollQuestionApi.createQuestion(readPoll.getId(), createAdviceQuestion());
        pollQuestionApi.createQuestion(readPoll.getId(), createMultipleChoiceQuestion());


        List<ReadQuestion> queryResponse = new PollQuestionQuery.Builder(pollQuestionApi)
                .pollId(readPoll.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .build().perform();

        assertThat(queryResponse).isSortedAccordingTo(Comparator.comparing(ReadQuestion::getId).reversed());
    }


    @Test
    void getAllQuestionFromNotExistingPoll() {
        Long wrongPollId = 999999999L;

        assertThatExceptionOfType(ApiException.class)
                .isThrownBy(() -> new PollQuestionQuery
                        .Builder(pollQuestionApi)
                        .pollId(wrongPollId)
                        .pageNumber(1)
                        .pageSize(10)
                        .build().perform())
                .matches(exception -> exception.getCode() == NOT_FOUND)
                .withMessageContaining("Poll with 'id: " + wrongPollId + "' is not found");
    }

    @Test
    void getAllQuestionsByQuestionId() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadPoll readPoll = cooperationPollApi.createCooperationPoll(readCoop.getId(), createPoll());

        ReadQuestion readQuestion = pollQuestionApi.createQuestion(readPoll.getId(), createAdviceQuestion());

        Long questionId = Objects.requireNonNull(readQuestion.getId());

        List<ReadQuestion> queryResponse = new PollQuestionQuery.Builder(pollQuestionApi)
                .pollId(readPoll.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .id(questionId)
                .build().perform();
        queryResponse.forEach(element -> assertEquals(element.getId(), readQuestion.getId()));
    }

    @Test
    void getAllQuestionByQuestionType() throws ApiException {
        ReadCooperation readCoop = cooperationApi.createCooperation(createCooperation());
        ReadPoll readPoll = cooperationPollApi.createCooperationPoll(readCoop.getId(), createPoll());

        ReadQuestion readQuestion = pollQuestionApi.createQuestion(readPoll.getId(), createAdviceQuestion());


        List<ReadQuestion> queryResponse = new PollQuestionQuery.Builder(pollQuestionApi)
                .pollId(readPoll.getId())
                .pageNumber(1)
                .pageSize(10)
                .sort("id,desc")
                .type(QuestionType.ADVICE)
                .build().perform();

        assertTrue(queryResponse.size() > 0);
        queryResponse.forEach(question -> assertEquals(QuestionType.ADVICE, question.getType()));
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

    private static CreatePoll createPoll() {
        LocalDateTime completionDate = LocalDateTime.now()
                .truncatedTo(ChronoUnit.MINUTES)
                .plusDays(MIN_POLL_DURATION_IN_DAYS)
                .plusMinutes(5L);
        return new CreatePoll()
                .header("Poll for our houses")
                .type(PollType.SIMPLE)
                .completionDate(completionDate)
                .creationDate(LocalDateTime.now())
                .description("Description");
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
}