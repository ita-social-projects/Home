package com.softserveinc.ita.homeproject.api.tests.polls;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import javax.ws.rs.core.Response;

import com.softserveinc.ita.homeproject.api.tests.utils.ApiClientUtil;
import com.softserveinc.ita.homeproject.client.ApiException;
import com.softserveinc.ita.homeproject.client.ApiResponse;
import com.softserveinc.ita.homeproject.client.api.CooperationApi;
import com.softserveinc.ita.homeproject.client.api.CooperationPollApi;
import com.softserveinc.ita.homeproject.client.api.PollQuestionApi;
import com.softserveinc.ita.homeproject.client.api.PollVoteApi;
import com.softserveinc.ita.homeproject.client.model.Address;
import com.softserveinc.ita.homeproject.client.model.AnswerVariantLookup;
import com.softserveinc.ita.homeproject.client.model.CreateAdviceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateAdviceQuestionVote;
import com.softserveinc.ita.homeproject.client.model.CreateCooperation;
import com.softserveinc.ita.homeproject.client.model.CreateMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.CreateMultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.client.model.CreatePoll;
import com.softserveinc.ita.homeproject.client.model.CreateUpdateAnswerVariant;
import com.softserveinc.ita.homeproject.client.model.CreateVote;
import com.softserveinc.ita.homeproject.client.model.PollStatus;
import com.softserveinc.ita.homeproject.client.model.PollType;
import com.softserveinc.ita.homeproject.client.model.QuestionLookup;
import com.softserveinc.ita.homeproject.client.model.QuestionType;
import com.softserveinc.ita.homeproject.client.model.ReadAdviceQuestion;
import com.softserveinc.ita.homeproject.client.model.ReadAdviceQuestionVote;
import com.softserveinc.ita.homeproject.client.model.ReadCooperation;
import com.softserveinc.ita.homeproject.client.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.client.model.ReadMultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.client.model.ReadPoll;
import com.softserveinc.ita.homeproject.client.model.ReadVote;
import com.softserveinc.ita.homeproject.client.model.UpdatePoll;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;

class PollVoteApiIT {

    public static final int BAD_REQUEST = Response.Status.BAD_REQUEST.getStatusCode();

    public static final int NOT_FOUND = Response.Status.NOT_FOUND.getStatusCode();

    private static final String POLL_NOT_FOUND_MESSAGE = "Poll with 'id: %s' is not found";

    private static final String POLL_STATUS_VALIDATION_MESSAGE = "Can't create vote on poll with status: '%s'";

    private static final String TRYING_TO_REVOTE_MESSAGE =
        "You are trying to re-vote on a poll with 'id: %d' that you have already voted";

    private static final String WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE =
        "The number of voted questions does not equal the number of questions in the poll with 'id: %d'";

    private static final String WRONG_QUESTIONS_FOR_POLL_MESSAGE =
        "Some of the questions you are trying to vote do not match the poll with 'id: %d'";

    private static final String WRONG_ANSWER_COUNT_VALIDATION_MESSAGE =
        "Wrong count of selected answers to poll question with 'id: %d' (there is should be min 1, max %d)";

    private static final String ANSWER_DOES_NOT_MATCH_QUESTION_VALIDATION_MESSAGE =
        "The answer variant with 'id: %d' cannot be chosen when voting on the question with 'id: %d'";

    private static final String QUESTION_NOT_FOUND_MESSAGE = "Poll question with 'id: %d' is not found";

    private static final String POLL_STATUS_DRAFT = "Can't create vote on poll with status: 'draft'";

    private static final PollStatus[] POLL_STATUSES_WITHOUT_ACTIVE_AND_COMPLETED =
        {PollStatus.DRAFT, PollStatus.SUSPENDED};

    private final CooperationApi cooperationApi = new CooperationApi(ApiClientUtil.getCooperationAdminClient());

    private final CooperationPollApi cooperationPollApi =
        new CooperationPollApi(ApiClientUtil.getCooperationAdminClient());

    private final PollQuestionApi pollQuestionApi = new PollQuestionApi(ApiClientUtil.getCooperationAdminClient());

    private final PollVoteApi pollVoteApi = new PollVoteApi(ApiClientUtil.getOwnerClient());

    private final static Long MIN_POLL_DURATION_IN_DAYS = 2L;

    private final static Long MAX_POLL_DURATION_IN_DAYS = 15L;

    private final static Random random = new Random();

    private static int cooperationNumber;

    private static int pollNumber;

    private static int adviceQuestionNumber;

    private static int multipleChoiceQuestionNumber;

    private static int answerVariantForMultipleChoiceQuestionNumber;

    @Test
    void createVoteSuccessTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(
            createdCooperation.getId(), createPoll());
        CreateAdviceQuestion createdAdviceQuestion = createAdviceQuestion();
        ReadAdviceQuestion addedAdviceQuestion = (ReadAdviceQuestion)
            pollQuestionApi.createQuestion(createdPoll.getId(), createdAdviceQuestion);
        CreateMultipleChoiceQuestion createdMultipleQuestion = createMultipleChoiceQuestion();
        ReadMultipleChoiceQuestion addedMultipleChoiceQuestion =
            (ReadMultipleChoiceQuestion) pollQuestionApi.createQuestion(createdPoll.getId(), createdMultipleQuestion);
        ReadPoll updatedPoll = cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), createdPoll.getId(),
            updatePoll(createdPoll));
        CreateVote createdVote = new CreateVote().addQuestionVotesItem(createAdviceQuestionVote(addedAdviceQuestion))
            .addQuestionVotesItem(
                createMultipleChoiceQuestionVote(addedMultipleChoiceQuestion));
        ApiResponse<ReadVote> response = pollVoteApi.createVoteWithHttpInfo(updatedPoll.getId(), createdVote);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatusCode());
        assertVote(updatedPoll.getId(), createdVote, response.getData());
    }

    @Test
    void votingOnDisabledPollThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        CreateAdviceQuestion createdAdviceQuestion = createAdviceQuestion();
        ReadAdviceQuestion addedAdviceQuestion = (ReadAdviceQuestion)
            pollQuestionApi.createQuestion(createdPoll.getId(), createdAdviceQuestion);

        cooperationPollApi.deleteCooperationPoll(createdCooperation.getId(), createdPoll.getId());

        CreateVote createdVote = new CreateVote().addQuestionVotesItem(createAdviceQuestionVote(addedAdviceQuestion));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(createdPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(String.format(POLL_NOT_FOUND_MESSAGE, createdPoll.getId()));
    }

    @Test
    void votingOnNonActivePollThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        CreateAdviceQuestion createdAdviceQuestion = createAdviceQuestion();
        ReadAdviceQuestion addedAdviceQuestion = (ReadAdviceQuestion)
            pollQuestionApi.createQuestion(createdPoll.getId(), createdAdviceQuestion);

        CreateVote createdVote = new CreateVote().addQuestionVotesItem(createAdviceQuestionVote(addedAdviceQuestion));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(createdPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining(POLL_STATUS_DRAFT, createdPoll.getStatus());
    }

    @Test
    void reVotingThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        CreateAdviceQuestion createdAdviceQuestion = createAdviceQuestion();
        ReadAdviceQuestion addedAdviceQuestion = (ReadAdviceQuestion)
            pollQuestionApi.createQuestion(createdPoll.getId(), createdAdviceQuestion);

        createdPoll.setStatus(PollStatus.ACTIVE);
        cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), createdPoll.getId(),
            updatePoll(createdPoll));

        CreateVote createdVote = new CreateVote().addQuestionVotesItem(createAdviceQuestionVote(addedAdviceQuestion));
        pollVoteApi.createVoteWithHttpInfo(createdPoll.getId(), createdVote);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(createdPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining(String.format(TRYING_TO_REVOTE_MESSAGE, createdPoll.getId()));
    }

    @Test
    void votingOnIncorrectNumberOfQuestionsThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll ourPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        List<ReadAdviceQuestion> questionList = new ArrayList<>();
        int questionsNumber = random.nextInt(3);
        saveSomeAdviceQuestionsAndAddToList(questionList, questionsNumber, ourPoll.getId());

        ReadPoll otherPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        saveSomeAdviceQuestionsAndAddToList(questionList, random.nextInt(3), otherPoll.getId());

        ourPoll.setStatus(PollStatus.ACTIVE);
        cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), ourPoll.getId(),
            updatePoll(ourPoll));

        int questionVotesNumber = random.nextInt(questionList.size());
        while (questionVotesNumber == questionsNumber) {
            questionVotesNumber = random.nextInt(questionList.size());
        }
        CreateVote createdVote = new CreateVote();
        for (int i = 0; i <= questionVotesNumber; i++) {
            createdVote.addQuestionVotesItem(createAdviceQuestionVote(questionList.get(i)));
        }

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(ourPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining(String.format(WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE, ourPoll.getId()));
    }

    @Test
    void votingOnDeletedQuestionThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());

        List<ReadAdviceQuestion> questionList = new ArrayList<>();
        int questionsNumber = random.nextInt(3);
        saveSomeAdviceQuestionsAndAddToList(questionList, questionsNumber, createdPoll.getId());

        ReadAdviceQuestion deletedQuestion = questionList.get(random.nextInt(questionList.size()));
        pollQuestionApi.deleteQuestion(createdPoll.getId(), deletedQuestion.getId());

        createdPoll.setStatus(PollStatus.ACTIVE);
        cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), createdPoll.getId(),
            updatePoll(createdPoll));

        CreateVote createdVote = new CreateVote();
        questionList.forEach(q -> createdVote.addQuestionVotesItem(createAdviceQuestionVote(q)));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(createdPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(String.format(QUESTION_NOT_FOUND_MESSAGE, deletedQuestion.getId()));
    }

    @Test
    void votingOnQuestionsDoNotMatchPollThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll ourPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        List<ReadAdviceQuestion> questionList = new ArrayList<>();
        int correctQuestionsNumber = random.nextInt(3);
        saveSomeAdviceQuestionsAndAddToList(questionList, correctQuestionsNumber, ourPoll.getId());

        ReadPoll otherPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        saveSomeAdviceQuestionsAndAddToList(questionList, correctQuestionsNumber, otherPoll.getId());

        ourPoll.setStatus(PollStatus.ACTIVE);
        ReadPoll updatedPoll = cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), ourPoll.getId(),
            updatePoll(ourPoll));
        CreateVote createdVote = createVoteWithNotMatchingQuestions(questionList, correctQuestionsNumber);

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(updatedPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining(String.format(WRONG_QUESTIONS_FOR_POLL_MESSAGE, updatedPoll.getId()));
    }

    @Test
    void votingOnMultipleChoiceQuestionWithExceedingNumberOfAnswersThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        CreateMultipleChoiceQuestion createdMultipleQuestion = createMultipleChoiceQuestion();
        ReadMultipleChoiceQuestion addedMultipleChoiceQuestion =
            (ReadMultipleChoiceQuestion) pollQuestionApi.createQuestion(createdPoll.getId(), createdMultipleQuestion);

        createdPoll.setStatus(PollStatus.ACTIVE);
        ReadPoll updatedPoll = cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), createdPoll.getId(),
            updatePoll(createdPoll));

        int maxAnswersCount = Objects.requireNonNull(addedMultipleChoiceQuestion.getMaxAnswerCount());
        CreateVote createdVote = new CreateVote().addQuestionVotesItem(
            createExceedingAnswerCountMultipleChoiceQuestionVote(addedMultipleChoiceQuestion));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(updatedPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining(
                String.format(WRONG_ANSWER_COUNT_VALIDATION_MESSAGE, addedMultipleChoiceQuestion.getId(),
                    maxAnswersCount));
    }

    @Test
    void votingOnMultipleChoiceQuestionWithNotMatchingAnswersThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll ourPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadMultipleChoiceQuestion ourMultipleChoiceQuestion =
            (ReadMultipleChoiceQuestion) pollQuestionApi.createQuestion(ourPoll.getId(),
                createMultipleChoiceQuestion());

        ReadPoll otherPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        ReadMultipleChoiceQuestion otherMultipleChoiceQuestion =
            (ReadMultipleChoiceQuestion) pollQuestionApi.createQuestion(otherPoll.getId(),
                createMultipleChoiceQuestion());

        ourPoll.setStatus(PollStatus.ACTIVE);
        ReadPoll updatedPoll = cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), ourPoll.getId(),
            updatePoll(ourPoll));

        CreateVote createdVote = new CreateVote().addQuestionVotesItem(
            createMultipleChoiceQuestionVoteWithNotMatchingAnswers(ourMultipleChoiceQuestion,
                otherMultipleChoiceQuestion));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(updatedPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == BAD_REQUEST)
            .withMessageContaining(String.format(ANSWER_DOES_NOT_MATCH_QUESTION_VALIDATION_MESSAGE,
                ((CreateMultipleChoiceQuestionVote) createdVote.getQuestionVotes().get(0)).getAnswers().get(0).getId(),
                ourMultipleChoiceQuestion.getId()));
    }

    @Test
    void votingOnMissingQuestionThrowsExceptionTest() throws ApiException {
        ReadCooperation createdCooperation = cooperationApi.createCooperation(createCooperation());
        ReadPoll createdPoll = cooperationPollApi.createCooperationPoll(createdCooperation.getId(), createPoll());
        CreateAdviceQuestion createdAdviceQuestion = createAdviceQuestion();
        pollQuestionApi.createQuestion(createdPoll.getId(), createdAdviceQuestion);
        createdPoll.setStatus(PollStatus.ACTIVE);
        ReadPoll updatedPoll = cooperationPollApi.updateCooperationPoll(createdCooperation.getId(), createdPoll.getId(),
            updatePoll(createdPoll));
        ReadAdviceQuestion missingQuestion = createMissingAdviceQuestion();
        CreateVote createdVote = new CreateVote().addQuestionVotesItem(createAdviceQuestionVote(missingQuestion));

        assertThatExceptionOfType(ApiException.class)
            .isThrownBy(() -> pollVoteApi.createVoteWithHttpInfo(updatedPoll.getId(), createdVote))
            .matches(exception -> exception.getCode() == NOT_FOUND)
            .withMessageContaining(String.format(QUESTION_NOT_FOUND_MESSAGE, missingQuestion.getId()));
    }

    private void assertVote(Long pollId, CreateVote expected, ReadVote actual) throws ApiException {
        assertNotNull(expected);
        assertNotNull(actual);
        assertNotNull(actual.getQuestionVotes());
        assertFalse(actual.getQuestionVotes().contains(null));
        assertEquals(expected.getQuestionVotes().size(), actual.getQuestionVotes().size());

        for (int i = 1; i < expected.getQuestionVotes().size(); i++) {
            assertEquals(expected.getQuestionVotes().get(i).getQuestion().getId(),
                Objects.requireNonNull(actual.getQuestionVotes().get(i).getQuestion()).getId());
        }

        CreateAdviceQuestionVote expectedAdviceQuestionVote =
            (CreateAdviceQuestionVote) expected.getQuestionVotes().get(0);
        ReadAdviceQuestionVote actualAdviceQuestionVote = (ReadAdviceQuestionVote) actual.getQuestionVotes().get(0);
        assertEquals(expectedAdviceQuestionVote.getAnswer().getAnswer().trim(),
            actualAdviceQuestionVote.getAnswer().getAnswer());

        CreateMultipleChoiceQuestionVote expectedMultipleChoiceQuestionVote =
            (CreateMultipleChoiceQuestionVote) expected.getQuestionVotes().get(1);
        ReadMultipleChoiceQuestionVote actualMultipleChoiceQuestionVote =
            (ReadMultipleChoiceQuestionVote) actual.getQuestionVotes().get(1);
        assertEquals(expectedMultipleChoiceQuestionVote.getAnswers().stream().map(AnswerVariantLookup::getId)
                .collect(Collectors.toList()),
            actualMultipleChoiceQuestionVote.getAnswers().stream().map(var -> Objects.requireNonNull(
                    var.getAnswerVariant()).getId())
                .collect(Collectors.toList()));

        assertEquals(
            pollQuestionApi.getQuestion(pollId,
                Objects.requireNonNull(expectedAdviceQuestionVote.getQuestion()).getId()),
            Objects.requireNonNull(actualAdviceQuestionVote.getQuestion()));
        assertEquals(
            pollQuestionApi.getQuestion(pollId, Objects.requireNonNull(expectedMultipleChoiceQuestionVote.getQuestion())
                .getId()),
            Objects.requireNonNull(actualMultipleChoiceQuestionVote.getQuestion()));
    }

    private CreateCooperation createCooperation() {
        return new CreateCooperation()
            .name(String.format("Cooperation #%d for vote test", ++cooperationNumber))
            .usreo(RandomStringUtils.randomNumeric(8))
            .iban("UA".concat(RandomStringUtils.randomNumeric(27)))
            .adminEmail(RandomStringUtils.randomAlphabetic(12).concat("@gmail.com"))
            .address(createAddress());
    }

    private Address createAddress() {
        return new Address().city("Dnipro")
            .district("TestDistrict")
            .houseBlock("Block")
            .houseNumber(RandomStringUtils.randomNumeric(3))
            .region("Dnipropetrovska")
            .street("SomeStreet")
            .zipCode("ZipCode");
    }

    private CreatePoll createPoll() {
        return new CreatePoll()
            .header(String.format("Poll #%d for vote test", ++pollNumber))
            .type(PollType.SIMPLE)
            .creationDate(LocalDateTime.now().plusDays(1)
                .truncatedTo(ChronoUnit.MINUTES))
            .description("Description");
    }

    //TODO
    private UpdatePoll updatePoll(ReadPoll readPoll) {
        return new UpdatePoll()
            .header(readPoll.getHeader())
            .description("updated description")
            .creationDate(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES))
            .status(PollStatus.ACTIVE);
    }

    private CreateAdviceQuestion createAdviceQuestion() {
        CreateAdviceQuestion newCreateAdviceQuestion = new CreateAdviceQuestion();
        newCreateAdviceQuestion.setType(QuestionType.ADVICE);
        newCreateAdviceQuestion.setQuestion(
            String.format("Advice question #%d for vote test", ++adviceQuestionNumber));
        return newCreateAdviceQuestion;
    }

    private CreateMultipleChoiceQuestion createMultipleChoiceQuestion() {
        ++multipleChoiceQuestionNumber;
        int maxAnswerCount = 1 + random.nextInt(3);
        CreateMultipleChoiceQuestion newMultipleChoiceQuestion = new CreateMultipleChoiceQuestion()
            .maxAnswerCount(maxAnswerCount)
            .answerVariants(createAnswerVariants(maxAnswerCount, multipleChoiceQuestionNumber));
        newMultipleChoiceQuestion.setType(QuestionType.MULTIPLE_CHOICE);
        newMultipleChoiceQuestion.setQuestion(
            String.format("Multiple choice question #%d for vote test", multipleChoiceQuestionNumber));
        return newMultipleChoiceQuestion;
    }

    private List<CreateUpdateAnswerVariant> createAnswerVariants(int maxAnswerCount, int questionNumber) {
        List<CreateUpdateAnswerVariant> answerVariants = new ArrayList<>();
        for (int i = 1; i <= maxAnswerCount * 2; i++) {
            answerVariants.add(new CreateUpdateAnswerVariant()
                .answer(String.format("AnswerVariant #%d for multiple choice question #%d for vote test",
                    ++answerVariantForMultipleChoiceQuestionNumber, questionNumber)));
        }
        return answerVariants;
    }

    private CreateAdviceQuestionVote createAdviceQuestionVote(ReadAdviceQuestion question) {
        CreateAdviceQuestionVote newCreateAdviceQuestionVote = new CreateAdviceQuestionVote()
            .answer(createAnswerVariant());
        return (CreateAdviceQuestionVote) newCreateAdviceQuestionVote.type(question.getType())
            .question(new QuestionLookup().id(question.getId()));
    }

    private CreateUpdateAnswerVariant createAnswerVariant() {
        return new CreateUpdateAnswerVariant().answer("AnswerVariant for advice question for vote test");
    }

    private CreateMultipleChoiceQuestionVote createMultipleChoiceQuestionVote(ReadMultipleChoiceQuestion question) {
        CreateMultipleChoiceQuestionVote newCreateMultipleChoiceQuestionVote = new CreateMultipleChoiceQuestionVote()
            .answers(getChosenAnswerVariantLookups(question));
        return (CreateMultipleChoiceQuestionVote) newCreateMultipleChoiceQuestionVote.type(question.getType())
            .question(new QuestionLookup().id(question.getId()));
    }

    private List<AnswerVariantLookup> getChosenAnswerVariantLookups(
        ReadMultipleChoiceQuestion question) {
        int answerCount = 1 + random.nextInt(Objects.requireNonNull(question.getMaxAnswerCount()));
        return Objects.requireNonNull(question.getAnswerVariants()).stream().limit(answerCount)
            .map(av -> new AnswerVariantLookup().id(av.getId()))
            .collect(Collectors.toList());
    }

    private CreateMultipleChoiceQuestionVote createExceedingAnswerCountMultipleChoiceQuestionVote(
        ReadMultipleChoiceQuestion question) {
        CreateMultipleChoiceQuestionVote newCreateMultipleChoiceQuestionVote = new CreateMultipleChoiceQuestionVote()
            .answers(getExceedingChosenAnswerVariantLookups(question));
        newCreateMultipleChoiceQuestionVote.setType(question.getType());
        newCreateMultipleChoiceQuestionVote.setQuestion(new QuestionLookup().id(question.getId()));
        return newCreateMultipleChoiceQuestionVote;
    }

    private List<AnswerVariantLookup> getExceedingChosenAnswerVariantLookups(
        ReadMultipleChoiceQuestion question) {
        int answersNumber = 0;
        int maxAnswersNumber = Objects.requireNonNull(question.getMaxAnswerCount());
        while (answersNumber <= maxAnswersNumber) {
            answersNumber = 1 + random.nextInt(Objects.requireNonNull(question.getAnswerVariants()).size());
        }
        return Objects.requireNonNull(question.getAnswerVariants()).stream().limit(answersNumber)
            .map(av -> new AnswerVariantLookup().id(av.getId()))
            .collect(Collectors.toList());
    }

    private void saveSomeAdviceQuestionsAndAddToList(List<ReadAdviceQuestion> questionList, int questionQuantity,
                                                     Long pollId) throws ApiException {
        for (int i = 0; i <= questionQuantity; i++) {
            CreateAdviceQuestion createdAdviceQuestion = createAdviceQuestion();
            questionList.add(
                (ReadAdviceQuestion) pollQuestionApi.createQuestion(pollId, createdAdviceQuestion));
        }
    }

    private CreateVote createVoteWithNotMatchingQuestions(List<ReadAdviceQuestion> questionList, int questionQuantity) {
        CreateVote createdVote = new CreateVote();
        int questionVotesNumber = 0;
        int i = 0;
        while (questionVotesNumber <= questionQuantity) {
            createdVote.addQuestionVotesItem(
                createAdviceQuestionVote(questionList.get(i + questionQuantity + 1)));
            questionVotesNumber++;
            if (questionVotesNumber <= questionQuantity) {
                createdVote.addQuestionVotesItem(createAdviceQuestionVote(questionList.get(i)));
                questionVotesNumber++;
            } else {
                break;
            }
            i++;
        }
        return createdVote;
    }

    private CreateMultipleChoiceQuestionVote createMultipleChoiceQuestionVoteWithNotMatchingAnswers(
        ReadMultipleChoiceQuestion votedQuestion, ReadMultipleChoiceQuestion otherQuestion) {
        CreateMultipleChoiceQuestionVote questionVote = new CreateMultipleChoiceQuestionVote();
        questionVote.setType(QuestionType.MULTIPLE_CHOICE);
        questionVote.setQuestion(new QuestionLookup().id(votedQuestion.getId()));
        int maxAnswerCount = Objects.requireNonNull(votedQuestion.getMaxAnswerCount());
        List<AnswerVariantLookup> generalSourceAnswerList =
            Objects.requireNonNull(votedQuestion.getAnswerVariants()).stream()
                .map(av -> new AnswerVariantLookup().id(av.getId())).collect(Collectors.toList());
        int answerCountOfVotedQuestion = generalSourceAnswerList.size();
        List<AnswerVariantLookup> otherQuestionAnswerList =
            Objects.requireNonNull(otherQuestion.getAnswerVariants()).stream()
                .map(av -> new AnswerVariantLookup().id(av.getId())).collect(Collectors.toList());
        generalSourceAnswerList.addAll(otherQuestionAnswerList);
        List<AnswerVariantLookup> questionVoteAnswerList = new ArrayList<>();
        int answersNumber = 0;
        questionVoteAnswerList.add(generalSourceAnswerList.get(answersNumber + answerCountOfVotedQuestion));
        answersNumber++;
        if (answersNumber < maxAnswerCount) {
            questionVoteAnswerList.add(generalSourceAnswerList.get(0));
        }
        return questionVote.answers(questionVoteAnswerList);
    }

    private ReadAdviceQuestion createMissingAdviceQuestion() {
        ReadAdviceQuestion missingQuestion = new ReadAdviceQuestion();
        missingQuestion.setId(-100L);
        missingQuestion.setType(QuestionType.ADVICE);
        return missingQuestion;
    }
}
