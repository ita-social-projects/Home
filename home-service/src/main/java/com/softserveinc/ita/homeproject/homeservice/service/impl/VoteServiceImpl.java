package com.softserveinc.ita.homeproject.homeservice.service.impl;

import static com.softserveinc.ita.homeproject.homeservice.service.QueryableService.NOT_FOUND_MESSAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homedata.repository.QuestionVoteRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateAdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateMultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadMultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadVoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

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

    private final VoteRepository voteRepository;

    private final QuestionVoteRepository questionVoteRepository;

    private final PollQuestionRepository questionRepository;

    private final PollRepository pollRepository;

    private final ServiceMapper mapper;

    @Transactional
    @Override
    public ReadVoteDto createVote(User currentUser, CreateVoteDto voteDto) {
        Poll votedPoll = validatePollEnabled(voteDto.getPollId());
        validatePollStatus(votedPoll.getId());
        validateReVoting(votedPoll.getId(), currentUser);
        validateQuestionVotesCount(voteDto);
        validateDeletedPollQuestions(voteDto);
        validatePollQuestionsMatching(voteDto);
        transformAnswersForAdviceChoiceQuestions(voteDto);
        validateAnswerCounts(voteDto);
        validateAnswersMatching(voteDto);
        var newVote = mapper.convert(voteDto, Vote.class);
        newVote.setUser(currentUser);
        voteRepository.save(newVote);
        newVote.getQuestionVotes().forEach(qv -> qv.setVote(newVote));
        questionVoteRepository.saveAll(newVote.getQuestionVotes());
        List<ReadQuestionVoteDto> questionVoteDtos = new ArrayList<>();
        for (QuestionVote qv : newVote.getQuestionVotes()) {
            Long questionId = qv.getQuestionId();
            PollQuestionType questionType = getQuestionWithCheckItsExistence(questionId).getType();
            if (questionType.equals(PollQuestionType.ADVICE)) {
                questionVoteDtos.add(mapper.convert(qv, ReadAdviceQuestionVoteDto.class));
            } else {
                questionVoteDtos.add(mapper.convert(qv, ReadMultipleChoiceQuestionVoteDto.class));
            }
        }
        ReadVoteDto readVoteDto = new ReadVoteDto();
        readVoteDto.setId(newVote.getId());
        readVoteDto.setQuestionVoteDtos(questionVoteDtos);
        return readVoteDto;
    }

    private Poll validatePollEnabled(Long pollId) {
        return pollRepository.findById(pollId).filter(Poll::getEnabled)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", pollId)));
    }

    private void validatePollStatus(Long pollId) {
        PollStatus pollStatus = pollRepository.findById(pollId).orElseThrow(() -> new NotFoundHomeException(
            String.format(NOT_FOUND_MESSAGE, "Poll", pollId))).getStatus();
        if (!pollStatus.equals(PollStatus.ACTIVE)) {
            throw new BadRequestHomeException(
                String.format(POLL_STATUS_VALIDATION_MESSAGE, pollStatus));
        }
    }

    private void validateReVoting(Long pollId, User currentUser) {
        if (voteRepository.findByPollIdAndUser(pollId, currentUser) != null) {
            throw new BadRequestHomeException(
                String.format(TRYING_TO_REVOTE_MESSAGE, pollId));
        }
    }

    private void validateQuestionVotesCount(CreateVoteDto voteDto) {
        int questionVotesCount = voteDto.getQuestionVoteDtos().size();
        int questionsCount = pollRepository.findById(voteDto.getPollId()).orElseThrow(() -> new NotFoundHomeException(
            String.format(NOT_FOUND_MESSAGE, "Poll", voteDto.getPollId()))).getPollQuestions().size();
        if (questionVotesCount != questionsCount) {
            throw new BadRequestHomeException(
                String.format(WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE, voteDto.getPollId()));
        }
    }

    private void validateDeletedPollQuestions(CreateVoteDto voteDto) {
        voteDto.getQuestionVoteDtos().forEach(qv -> getQuestionWithCheckItsExistence(qv.getQuestionId()));
    }

    private void validatePollQuestionsMatching(CreateVoteDto voteDto) {
        List<CreateQuestionVoteDto> questionVotes = voteDto.getQuestionVoteDtos();
        int questionsCount = questionVotes.size();
        int controlNumber = 0;
        for (CreateQuestionVoteDto questionVote : questionVotes) {
            Long questionId = questionVote.getQuestionId();
            PollQuestion question = getQuestionWithCheckItsExistence(questionId);
            Long questionPollId = question.getPoll().getId();
            if (questionPollId.equals(voteDto.getPollId())) {
                controlNumber++;
            }
        }
        if (questionsCount != controlNumber) {
            throw new BadRequestHomeException(
                String.format(WRONG_QUESTIONS_FOR_POLL_MESSAGE, voteDto.getPollId()));
        }
    }

    private void transformAnswersForAdviceChoiceQuestions(CreateVoteDto voteDto) {
        for (CreateQuestionVoteDto questionVote : voteDto.getQuestionVoteDtos()) {
            if (questionVote.getClass() == CreateAdviceQuestionVoteDto.class) {
                ((CreateAdviceQuestionVoteDto) questionVote).setAnswer(
                    ((CreateAdviceQuestionVoteDto) questionVote).getAnswer().trim().toUpperCase());
            }
        }
    }

    private void validateAnswerCounts(CreateVoteDto voteDto) {
        for (CreateQuestionVoteDto questionVote : voteDto.getQuestionVoteDtos()) {
            Long questionId = questionVote.getQuestionId();
            PollQuestion question = getQuestionWithCheckItsExistence(questionId);
            if (question.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
                Integer maxAnswerCount = ((MultipleChoiceQuestion) question).getMaxAnswerCount();
                int realAnswerCount = ((CreateMultipleChoiceQuestionVoteDto) questionVote).getAnswerVariantIds().size();
                if (realAnswerCount < 1 || realAnswerCount > maxAnswerCount) {
                    throw new BadRequestHomeException(
                        String.format(WRONG_ANSWER_COUNT_VALIDATION_MESSAGE, questionId, maxAnswerCount));
                }
            }
        }
    }

    private void validateAnswersMatching(CreateVoteDto voteDto) {
        for (CreateQuestionVoteDto questionVote : voteDto.getQuestionVoteDtos()) {
            Long questionId = questionVote.getQuestionId();
            PollQuestion question = getQuestionWithCheckItsExistence(questionId);
            if (question.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
                List<Long> pollQuestionAnswerIds = ((MultipleChoiceQuestion) question).getAnswerVariants().stream()
                    .map(BaseEntity::getId).collect(Collectors.toList());
                List<Long> questionVoteAnswerIds =
                    ((CreateMultipleChoiceQuestionVoteDto) questionVote).getAnswerVariantIds();
                questionVoteAnswerIds.forEach(id -> {
                    if (!pollQuestionAnswerIds.contains(id)) {
                        throw new BadRequestHomeException(
                            String.format(ANSWER_DOES_NOT_MATCH_QUESTION_VALIDATION_MESSAGE, id, questionId));
                    }
                });
            }
        }
    }

    private PollQuestion getQuestionWithCheckItsExistence(Long questionId) {
        return questionRepository.findById(questionId).filter(PollQuestion::getEnabled)
            .orElseThrow(
                () -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll question", questionId)));
    }
}
