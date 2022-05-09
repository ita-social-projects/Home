package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.Apartment;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollType;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.poll.question.DoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.results.ResultQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.poll.votes.Vote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteAnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteRepository;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.ownership.Ownership;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.service.poll.PollServiceImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class PollServiceImplTest {
    private static Cooperation cooperation;

    @Mock
    private static PollRepository pollRepository;

    @Mock
    private static CooperationRepository cooperationRepository;

    @Mock
    private static VoteRepository voteRepository;

    @Mock
    private static AnswerVariantRepository answerVariantRepository;

    @Mock
    private static ResultQuestionRepository resultQuestionRepository;

    @InjectMocks
    private PollServiceImpl pollService;

    @BeforeAll
    static void getConstants() {
        cooperation = new Cooperation();
        cooperation.setId(1L);
        cooperation.setEnabled(true);
        House house = new House();
        house.setEnabled(false);
        cooperation.setHouses(Collections.singletonList(house));
    }

    @Test
    void updateTestWhenPollStatusIsNotDraftTest() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.ACTIVE);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.SUSPENDED);
        when(pollRepository.findById(anyLong())).thenReturn(Optional.of(poll));
        assertThrows(BadRequestHomeException.class, () -> pollService.update(1L, 1L, pollDto));
    }

    @Test
    void updateTestWhenPollStatusChangedToCompleteTest() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.DRAFT);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.COMPLETED);
        when(pollRepository.findById(anyLong())).thenReturn(Optional.of(poll));
        assertThrows(BadRequestHomeException.class, () -> pollService.update(1L, 1L, pollDto));
    }

    @Test
    void createPollWithDeletedHouseTest() {
        Poll poll = new Poll();
        poll.setStatus(PollStatus.DRAFT);
        poll.setCooperation(cooperation);
        poll.setEnabled(true);
        HouseDto houseDto = new HouseDto();
        houseDto.setId(1L);
        PollDto pollDto = new PollDto();
        pollDto.setStatus(PollStatusDto.DRAFT);
        pollDto.setPolledHouses(Collections.singletonList(houseDto));
        when(cooperationRepository.findById(anyLong())).thenReturn(Optional.of(cooperation));
        assertThrows(NotFoundHomeException.class, () -> pollService.create(1L, pollDto));
    }

    @Test
    void calculateCompletedPollsResultsSavesMajorPollResultCalculatedByOwnershipArea() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<AnswerVariant> answers = createDoubleChoiceAnswerVariants();
        List<Vote> votes = createDoubleChoiceVotes(actualPoll, answers, 4, 1);
        List<ResultQuestion> resultQuestions = initializeResultQuestions(4, 1, actualPoll);
        actualPoll.setPolledHouses(List.of(createHouse(answers.size())));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.MAJOR);
        question.setPoll(actualPoll);
        when(answerVariantRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(answers);
        when(voteRepository.findAllByPoll(actualPoll)).thenReturn(votes);

        pollService.calculateCompletedPollsResults(List.of(actualPoll), new ArrayList<>());

        ArgumentCaptor<List<ResultQuestion>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(resultQuestionRepository).saveAll(argumentCaptor.capture());
        assertEquals(resultQuestions.size(), argumentCaptor.getValue().size());
        for (int i = 0; i < argumentCaptor.getValue().size(); i++) {
            argumentCaptor.getValue().sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));
            resultQuestions.sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));

            assertThat(argumentCaptor.getValue().get(i)).usingRecursiveComparison()
                .isEqualTo(resultQuestions.get(i));
        }
    }

    @Test
    void calculateCompletedPollsResultsSavesMajorPollResultCalculatedByVotesQuantity() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<AnswerVariant> answers = createDoubleChoiceAnswerVariants();
        List<Vote> votes = createDoubleChoiceVotes(actualPoll, answers, 6, 1);
        List<ResultQuestion> resultQuestions = initializeResultQuestions(6, 1, actualPoll);
        actualPoll.setPolledHouses(List.of(createHouse(answers.size())));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.MAJOR);
        question.setPoll(actualPoll);
        when(answerVariantRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(answers);
        when(voteRepository.findAllByPoll(actualPoll)).thenReturn(votes);

        pollService.calculateCompletedPollsResults(List.of(actualPoll), new ArrayList<>());

        ArgumentCaptor<List<ResultQuestion>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(resultQuestionRepository).saveAll(argumentCaptor.capture());
        assertEquals(resultQuestions.size(), argumentCaptor.getValue().size());
        for (int i = 0; i < argumentCaptor.getValue().size(); i++) {
            argumentCaptor.getValue().sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));
            resultQuestions.sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));

            assertThat(argumentCaptor.getValue().get(i)).usingRecursiveComparison()
                .isEqualTo(resultQuestions.get(i));
        }
    }

    @Test
    void calculateCompletedPollsResultsSavesSimplePollResultCalculatedByOwnershipArea() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<AnswerVariant> answers = createDoubleChoiceAnswerVariants();
        List<Vote> votes = createDoubleChoiceVotes(actualPoll, answers, 3, 1);
        List<ResultQuestion> resultQuestions = initializeResultQuestions(3, 1, actualPoll);
        actualPoll.setPolledHouses(List.of(createHouse(answers.size())));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.SIMPLE);
        question.setPoll(actualPoll);
        when(answerVariantRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(answers);
        when(voteRepository.findAllByPoll(actualPoll)).thenReturn(votes);

        pollService.calculateCompletedPollsResults(List.of(actualPoll), new ArrayList<>());

        ArgumentCaptor<List<ResultQuestion>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(resultQuestionRepository).saveAll(argumentCaptor.capture());
        assertEquals(resultQuestions.size(), argumentCaptor.getValue().size());
        for (int i = 0; i < argumentCaptor.getValue().size(); i++) {
            argumentCaptor.getValue().sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));
            resultQuestions.sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));

            assertThat(argumentCaptor.getValue().get(i)).usingRecursiveComparison()
                .isEqualTo(resultQuestions.get(i));
        }
    }

    @Test
    void calculateCompletedPollsResultsSavesSimplePollResultCalculatedByVotesQuantity() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<AnswerVariant> answers = createDoubleChoiceAnswerVariants();
        List<Vote> votes = createDoubleChoiceVotes(actualPoll, answers, 4, 2);
        List<ResultQuestion> resultQuestions = initializeResultQuestions(4, 2, actualPoll);
        actualPoll.setPolledHouses(List.of(createHouse(4)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.SIMPLE);
        question.setPoll(actualPoll);
        when(answerVariantRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(answers);
        when(voteRepository.findAllByPoll(actualPoll)).thenReturn(votes);

        pollService.calculateCompletedPollsResults(List.of(actualPoll), new ArrayList<>());

        ArgumentCaptor<List<ResultQuestion>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(resultQuestionRepository).saveAll(argumentCaptor.capture());
        assertEquals(resultQuestions.size(), argumentCaptor.getValue().size());
        for (int i = 0; i < argumentCaptor.getValue().size(); i++) {
            argumentCaptor.getValue().sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));
            resultQuestions.sort(Comparator.comparing(resultQuestion ->
                resultQuestion.getAnswerVariant().getAnswer()));

            assertThat(argumentCaptor.getValue().get(i)).usingRecursiveComparison()
                .isEqualTo(resultQuestions.get(i));
        }
    }

    @Test
    void calculateCompletedPollsResultsShouldNotSavesSimplePollResultsDueToNotEnoughVotes() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<AnswerVariant> answers = createDoubleChoiceAnswerVariants();
        List<Vote> votes = createDoubleChoiceVotes(actualPoll, answers, 5, 2);
        actualPoll.setPolledHouses(List.of(createHouse(10)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.SIMPLE);
        question.setPoll(actualPoll);
        when(answerVariantRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(answers);
        when(voteRepository.findAllByPoll(actualPoll)).thenReturn(votes);

        pollService.calculateCompletedPollsResults(List.of(actualPoll), new ArrayList<>());

        ArgumentCaptor<List<ResultQuestion>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(resultQuestionRepository, never()).saveAll(argumentCaptor.capture());
    }

    @Test
    void calculateCompletedPollsResultsShouldNotSavesMajorPollResultsDueToNotEnoughVotes() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<AnswerVariant> answers = createDoubleChoiceAnswerVariants();
        List<Vote> votes = createDoubleChoiceVotes(actualPoll, answers, 6, 2);
        actualPoll.setPolledHouses(List.of(createHouse(10)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.MAJOR);
        question.setPoll(actualPoll);
        when(answerVariantRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(answers);
        when(voteRepository.findAllByPoll(actualPoll)).thenReturn(votes);

        pollService.calculateCompletedPollsResults(List.of(actualPoll), new ArrayList<>());

        ArgumentCaptor<List<ResultQuestion>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(resultQuestionRepository, never()).saveAll(argumentCaptor.capture());
    }

    private static Poll createPoll() {
        Poll poll = new Poll();
        DoubleChoiceQuestion question = new DoubleChoiceQuestion();
        AnswerVariant positiveAnswer = new AnswerVariant();
        AnswerVariant negativeAnswer = new AnswerVariant();

        positiveAnswer.setAnswer("yes");
        negativeAnswer.setAnswer("no");

        question.setAnswerVariants(List.of(positiveAnswer, negativeAnswer));

        poll.setStatus(PollStatus.ACTIVE);
        poll.setCompletionDate(LocalDateTime.now().minusDays(1));
        poll.setEnabled(true);
        poll.setPollQuestions(List.of(question));

        return poll;
    }

    private static List<Vote> createDoubleChoiceVotes(Poll poll, List<AnswerVariant> answers, int voteCount,
                                                      int negativeVoteCount) {
        List<Vote> votes = new ArrayList<>();
        AnswerVariant positiveAnswer = answers.stream()
            .filter(answerVariant -> answerVariant.getAnswer().equals("yes")).findFirst().get();
        AnswerVariant negativeAnswer = answers.stream()
            .filter(answerVariant -> answerVariant.getAnswer().equals("no")).findFirst().get();

        for (int i = 0; i < voteCount - negativeVoteCount; i++) {
            createVote(poll, votes, positiveAnswer);
        }
        for (int i = 0; i < negativeVoteCount; i++) {
            createVote(poll, votes, negativeAnswer);
        }

        return votes;
    }

    private static void createVote(Poll poll, List<Vote> votes, AnswerVariant answer) {
        VoteAnswerVariant voteAnswerVariant = new VoteAnswerVariant();
        Vote vote = new Vote();
        User user = new User();

        voteAnswerVariant.setVote(vote);
        voteAnswerVariant.setAnswerVariant(answer);
        vote.setPoll(poll);
        vote.setUser(user);
        vote.setType(PollQuestionType.MULTIPLE_CHOICE);
        vote.setQuestion(poll.getPollQuestions().get(0));
        vote.setVoteAnswerVariants(List.of(voteAnswerVariant));

        votes.add(vote);
    }

    private static House createHouse(int apartmentsQuantity) {
        House house = new House();
        List<Apartment> apartments = new ArrayList<>();
        BigDecimal houseArea = new BigDecimal(0);

        for (int i = 0; i < apartmentsQuantity; i++) {
            Apartment apartment = createBaseApartment();
            houseArea = houseArea.add(apartment.getApartmentArea());
            apartments.add(apartment);
        }

        house.setHouseArea(houseArea.doubleValue());
        house.setApartments(apartments);

        return house;
    }

    private static Apartment createBaseApartment() {
        Apartment apartment = new Apartment();
        Ownership ownership = new Ownership();

        ownership.setOwnershipPart(new BigDecimal(1));
        ownership.setApartment(apartment);
        apartment.setOwnerships(List.of(ownership));
        apartment.setApartmentArea(new BigDecimal("100.0"));

        return apartment;
    }

    private static List<AnswerVariant> createDoubleChoiceAnswerVariants() {
        List<AnswerVariant> answers = new ArrayList<>();

        answers.add(createAnswerVariant("yes"));
        answers.add(createAnswerVariant("no"));

        return answers;
    }

    private static AnswerVariant createAnswerVariant(String answer) {
        AnswerVariant answerVariant = new AnswerVariant();

        answerVariant.setAnswer(answer);

        return answerVariant;
    }

    private static ArrayList<ResultQuestion> initializeResultQuestions(int answerCount, int negativeAnswers,
                                                                       Poll poll) {
        ArrayList<ResultQuestion> resultQuestions = new ArrayList<>();
        ResultQuestion positiveResult = new ResultQuestion();
        ResultQuestion negativeResult = new ResultQuestion();
        MultipleChoiceQuestion question = (MultipleChoiceQuestion) poll.getPollQuestions().get(0);

        positiveResult.setAnswerVariant(question.getAnswerVariants().get(0));
        positiveResult.setPoll(poll);
        positiveResult.setType(PollQuestionType.MULTIPLE_CHOICE);
        positiveResult.setVoteCount(answerCount - negativeAnswers);
        positiveResult.setPercentVotes(String.valueOf(
            new BigDecimal(answerCount - negativeAnswers)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(answerCount), 10, RoundingMode.CEILING)
        ));
        resultQuestions.add(positiveResult);

        negativeResult.setAnswerVariant(question.getAnswerVariants().get(1));
        negativeResult.setPoll(poll);
        negativeResult.setType(PollQuestionType.MULTIPLE_CHOICE);
        negativeResult.setVoteCount(negativeAnswers);
        negativeResult.setPercentVotes(String.valueOf(
            new BigDecimal(negativeAnswers)
                .multiply(new BigDecimal(100))
                .divide(new BigDecimal(answerCount), 10, RoundingMode.CEILING)
        ));
        resultQuestions.add(negativeResult);

        return resultQuestions;
    }

    private static void initializeOwnershipsUser(Poll poll, List<Vote> votes) {
        List<Ownership> ownerships = new ArrayList<>();
        int lastVoteIndex = votes.size() - 1;

        poll.getPolledHouses().get(0).getApartments().forEach(apartment ->
            ownerships.addAll(apartment.getOwnerships())
        );

        for (int i = 0; i < ownerships.size(); i++) {
            if (i >= lastVoteIndex) {
                ownerships.get(i).setUser(votes.get(lastVoteIndex).getUser());
            } else {
                ownerships.get(i).setUser(votes.get(i).getUser());
            }

            poll.getPolledHouses().get(0).getApartments().get(i).setOwnerships(List.of(ownerships.get(i)));
        }
    }
}
