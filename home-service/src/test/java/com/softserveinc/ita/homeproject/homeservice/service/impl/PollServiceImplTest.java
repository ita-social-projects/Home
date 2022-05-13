package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.CooperationRepository;
import com.softserveinc.ita.homeproject.homedata.cooperation.apatment.Apartment;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollType;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.question.DoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.votes.MultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.QuestionVoteRepository;
import com.softserveinc.ita.homeproject.homedata.poll.votes.Vote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteQuestionVariant;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteQuestionVariantRepository;
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
    private static VoteQuestionVariantRepository voteQuestionVariantRepository;

    @Mock
    private static QuestionVoteRepository questionVoteRepository;

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
        pollDto.setCreationDate(LocalDateTime.now());
        when(cooperationRepository.findById(anyLong())).thenReturn(Optional.of(cooperation));
        assertThrows(NotFoundHomeException.class, () -> pollService.create(1L, pollDto));
    }

    @Test
    void calculateCompletedPollsResultsSavesMajorPollResultCalculatedByOwnershipArea() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<MultipleChoiceQuestionVote> votes = createQuestionVotes(4, 1);
        List<VoteQuestionVariant> voteQuestionVariants = createVoteQuestionVariants(votes);
        actualPoll.setPolledHouses(List.of(createHouse(votes.size())));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.MAJOR);
        question.setPoll(actualPoll);
        when(questionVoteRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(
            List.copyOf(votes));
        when(questionVoteRepository.findAllByQuestion(question)).thenReturn(List.copyOf(votes));
        when(pollRepository.findAll()).thenReturn(List.of(actualPoll));
        for ( int i = 0; i < votes.size(); i++) {
            when(voteQuestionVariantRepository.findByQuestionVote(votes.get(i))).thenReturn(voteQuestionVariants.get(i));
        }

        pollService.calculateCompletedPollsResults();

        verify(pollRepository, times(1)).save(actualPoll);
        assertEquals("75.0000000000", actualPoll.getResult());
        assertEquals(PollStatus.COMPLETED, actualPoll.getStatus());
    }

    @Test
    void calculateCompletedPollsResultsSavesMajorPollResultCalculatedByVotesQuantity() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<MultipleChoiceQuestionVote> votes = createQuestionVotes(6, 1);
        List<VoteQuestionVariant> voteQuestionVariants = createVoteQuestionVariants(votes);
        actualPoll.setPolledHouses(List.of(createHouse(2)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.MAJOR);
        question.setPoll(actualPoll);
        when(questionVoteRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(
            List.copyOf(votes));
        when(questionVoteRepository.findAllByQuestion(question)).thenReturn(List.copyOf(votes));
        when(pollRepository.findAll()).thenReturn(List.of(actualPoll));
        for ( int i = 0; i < votes.size(); i++) {
            when(voteQuestionVariantRepository.findByQuestionVote(votes.get(i))).thenReturn(voteQuestionVariants.get(i));
        }

        pollService.calculateCompletedPollsResults();

        verify(pollRepository, times(1)).save(actualPoll);
        assertEquals("83.3333333334", actualPoll.getResult());
        assertEquals(PollStatus.COMPLETED, actualPoll.getStatus());
    }

    @Test
    void calculateCompletedPollsResultsSavesSimplePollResultCalculatedByOwnershipArea() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<MultipleChoiceQuestionVote> votes = createQuestionVotes(3, 1);
        List<VoteQuestionVariant> voteQuestionVariants = createVoteQuestionVariants(votes);
        actualPoll.setPolledHouses(List.of(createHouse(4)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.SIMPLE);
        question.setPoll(actualPoll);
        when(questionVoteRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(
            List.copyOf(votes));
        when(questionVoteRepository.findAllByQuestion(question)).thenReturn(List.copyOf(votes));
        when(pollRepository.findAll()).thenReturn(List.of(actualPoll));
        for ( int i = 0; i < votes.size(); i++) {
            when(voteQuestionVariantRepository.findByQuestionVote(votes.get(i))).thenReturn(voteQuestionVariants.get(i));
        }

        pollService.calculateCompletedPollsResults();

        verify(pollRepository, times(1)).save(actualPoll);
        assertEquals("66.6666666667", actualPoll.getResult());
        assertEquals(PollStatus.COMPLETED, actualPoll.getStatus());
    }

    @Test
    void calculateCompletedPollsResultsSavesSimplePollResultCalculatedByVotesQuantity() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<MultipleChoiceQuestionVote> votes = createQuestionVotes(4, 2);
        List<VoteQuestionVariant> voteQuestionVariants = createVoteQuestionVariants(votes);
        actualPoll.setPolledHouses(List.of(createHouse(2)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.SIMPLE);
        question.setPoll(actualPoll);
        when(questionVoteRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(
            List.copyOf(votes));
        when(questionVoteRepository.findAllByQuestion(question)).thenReturn(List.copyOf(votes));
        when(pollRepository.findAll()).thenReturn(List.of(actualPoll));
        for ( int i = 0; i < votes.size(); i++) {
            when(voteQuestionVariantRepository.findByQuestionVote(votes.get(i))).thenReturn(voteQuestionVariants.get(i));
        }

        pollService.calculateCompletedPollsResults();

        verify(pollRepository, times(1)).save(actualPoll);
        assertEquals("50.0000000000", actualPoll.getResult());
        assertEquals(PollStatus.COMPLETED, actualPoll.getStatus());
    }

    @Test
    void calculateCompletedPollsResultsNotSavesResultOfMajorPollWithoutRequiredAmountOfVoters() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<MultipleChoiceQuestionVote> votes = createQuestionVotes(2, 1);
        List<VoteQuestionVariant> voteQuestionVariants = createVoteQuestionVariants(votes);
        actualPoll.setPolledHouses(List.of(createHouse(5)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.MAJOR);
        question.setPoll(actualPoll);
        when(questionVoteRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(
            List.copyOf(votes));
        when(questionVoteRepository.findAllByQuestion(question)).thenReturn(List.copyOf(votes));
        for ( int i = 0; i < votes.size(); i++) {
            when(voteQuestionVariantRepository.findByQuestionVote(votes.get(i))).thenReturn(voteQuestionVariants.get(i));
        }
        when(pollRepository.findAll()).thenReturn(List.of(actualPoll));

        pollService.calculateCompletedPollsResults();

        verify(pollRepository, times(1)).save(actualPoll);
        assertEquals(PollStatus.COMPLETED, actualPoll.getStatus());
        assertNull(actualPoll.getResult());
    }

    @Test
    void calculateCompletedPollsResultsNotSavesResultOfSimplePollWithoutRequiredAmountOfVoters() {
        Poll actualPoll = createPoll();
        PollQuestion question = actualPoll.getPollQuestions().get(0);
        List<MultipleChoiceQuestionVote> votes = createQuestionVotes(2, 2);
        List<VoteQuestionVariant> voteQuestionVariants = createVoteQuestionVariants(votes);
        actualPoll.setPolledHouses(List.of(createHouse(5)));
        initializeOwnershipsUser(actualPoll, votes);
        actualPoll.setType(PollType.SIMPLE);
        question.setPoll(actualPoll);
        when(questionVoteRepository.findAllByQuestion(actualPoll.getPollQuestions().get(0))).thenReturn(
            List.copyOf(votes));
        when(questionVoteRepository.findAllByQuestion(question)).thenReturn(List.copyOf(votes));
        for ( int i = 0; i < votes.size(); i++) {
            when(voteQuestionVariantRepository.findByQuestionVote(votes.get(i))).thenReturn(voteQuestionVariants.get(i));
        }
        when(pollRepository.findAll()).thenReturn(List.of(actualPoll));

        pollService.calculateCompletedPollsResults();

        verify(pollRepository, times(1)).save(actualPoll);
        assertEquals(PollStatus.COMPLETED, actualPoll.getStatus());
        assertNull(actualPoll.getResult());
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

    private static List<MultipleChoiceQuestionVote> createQuestionVotes(int totalVotesQuantity, int negativeVotes) {

        List<MultipleChoiceQuestionVote> votes = new ArrayList<>();
        for (int i = 0; i < negativeVotes; i++) {
            votes.add(createQuestionVote("no"));
        }

        for (int i = 0; i < (totalVotesQuantity - negativeVotes); i++) {
            votes.add(createQuestionVote("yes"));
        }

        return votes;
    }

    private static MultipleChoiceQuestionVote createQuestionVote(String answer) {
        MultipleChoiceQuestionVote questionVote = new MultipleChoiceQuestionVote();
        Vote vote = new Vote();
        VoteQuestionVariant voteQuestionVariant = new VoteQuestionVariant();
        AnswerVariant answerVariant = new AnswerVariant();

        vote.setUser(new User());
        questionVote.setVote(vote);
        answerVariant.setAnswer(answer);
        voteQuestionVariant.setAnswerVariant(answerVariant);
        questionVote.setAnswers(List.of(voteQuestionVariant));

        return questionVote;
    }

    private static List<VoteQuestionVariant> createVoteQuestionVariants(List<MultipleChoiceQuestionVote> votes) {
        List<VoteQuestionVariant> voteQuestionVariants = new ArrayList<>();

        for (MultipleChoiceQuestionVote vote : votes) {
            VoteQuestionVariant voteQuestionVariant = new VoteQuestionVariant();
            voteQuestionVariant.setQuestionVote(vote);
            voteQuestionVariant.setAnswerVariant(vote.getAnswers().get(0).getAnswerVariant());

            voteQuestionVariants.add(voteQuestionVariant);
        }

        return voteQuestionVariants;
    }

    private static void initializeOwnershipsUser(Poll poll, List<MultipleChoiceQuestionVote> votes) {
        List<Ownership> ownerships = new ArrayList<>();
        int lastVoteIndex = votes.size() - 1;

        poll.getPolledHouses().get(0).getApartments().forEach(apartment ->
            ownerships.addAll(apartment.getOwnerships())
        );

        for (int i = 0; i < ownerships.size(); i++) {
            if (i >= lastVoteIndex) {
                ownerships.get(i).setUser(votes.get(lastVoteIndex).getVote().getUser());
            } else {
                ownerships.get(i).setUser(votes.get(i).getVote().getUser());
            }

            poll.getPolledHouses().get(0).getApartments().get(i).setOwnerships(List.of(ownerships.get(i)));
        }
    }
}
