package com.softserveinc.ita.homeproject.homeservice.service.impl;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.entity.PollStatus;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homedata.repository.QuestionVoteRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateMultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadMultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadVoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundPollException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundPollQuestionException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private static final String NOT_FOUND_MESSAGE = "%s with 'id: %d' is not found";

    private static final String POLL_STATUS_VALIDATION_MESSAGE = "Can't create vote on poll with status: '%s'";

    private static final String MAX_ANSWER_COUNT_VALIDATION_MESSAGE =
        "You exceeded the max count of answers to poll question with 'id: %d'";

    private final VoteRepository voteRepository;

    private final QuestionVoteRepository questionVoteRepository;

    private final PollQuestionRepository questionRepository;

    private final PollRepository pollRepository;

    private final ServiceMapper mapper;

    @Transactional
    @Override
    public ReadVoteDto createVote(Long pollId, CreateVoteDto voteDto) {
        validatePollStatus(pollId);
        validateAnswerCounts(voteDto);
        var newVote = new Vote();
        newVote.setPollId(pollId);
        var savedVote = voteRepository.save(newVote);
        List<QuestionVote> savedQuestionVotes = new ArrayList<>();
        for (CreateQuestionVoteDto dto : voteDto.getQuestionVoteDtos()) {
            QuestionVote newQuestionVote = mapper.convert(dto, QuestionVote.class);
            newQuestionVote.setVote(savedVote);
            var savedQuestionVote = questionVoteRepository.save(newQuestionVote);
            savedQuestionVotes.add(savedQuestionVote);
        }
        savedVote.setQuestionVotes(savedQuestionVotes);
        var reSavedVote = voteRepository.save(savedVote);
        List<ReadQuestionVoteDto> questionVoteDtos = new ArrayList<>();
        for (QuestionVote qv : reSavedVote.getQuestionVotes()) {
            Long questionId = qv.getQuestionId();
            PollQuestionType questionType =
                questionRepository.findById(questionId).orElseThrow(() -> new NotFoundPollQuestionException(
                    String.format(NOT_FOUND_MESSAGE, "Poll question", questionId))).getType();
            if (questionType.equals(PollQuestionType.ADVICE)) {
                questionVoteDtos.add(mapper.convert(qv, ReadAdviceQuestionVoteDto.class));
            } else {
                questionVoteDtos.add(mapper.convert(qv, ReadMultipleChoiceQuestionVoteDto.class));
            }
        }
        ReadVoteDto readVoteDto = new ReadVoteDto();
        readVoteDto.setId(reSavedVote.getId());
        readVoteDto.setQuestionVoteDtos(questionVoteDtos);
        return readVoteDto;
    }

    private void validatePollStatus(Long pollId) {
        PollStatus pollStatus = pollRepository.findById(pollId).orElseThrow(() -> new NotFoundPollException(
            String.format(NOT_FOUND_MESSAGE, "Poll", pollId))).getStatus();
        if (!pollStatus.equals(PollStatus.ACTIVE)) {
            throw new BadRequestHomeException(
                String.format(POLL_STATUS_VALIDATION_MESSAGE, pollStatus));
        }
    }

    private void validateAnswerCounts(CreateVoteDto voteDto) {
        List<CreateQuestionVoteDto> questionVotes = voteDto.getQuestionVoteDtos();
        for (CreateQuestionVoteDto questionVote : questionVotes) {
            Long questionId = questionVote.getQuestionId();
            PollQuestion question = questionRepository.findById(questionId).orElseThrow(
                () -> new NotFoundPollQuestionException(String.format(NOT_FOUND_MESSAGE, "Poll question", questionId)));
            if (question.getClass() == MultipleChoiceQuestion.class) {
                Integer maxAnswerCount = ((MultipleChoiceQuestion) question).getMaxAnswerCount();
                int realAnswerCount = ((CreateMultipleChoiceQuestionVoteDto) questionVote).getAnswerVariantIds().size();
                if (realAnswerCount > maxAnswerCount) {
                    throw new BadRequestHomeException(
                        String.format(MAX_ANSWER_COUNT_VALIDATION_MESSAGE, questionId));
                }
            }
        }
    }
}
