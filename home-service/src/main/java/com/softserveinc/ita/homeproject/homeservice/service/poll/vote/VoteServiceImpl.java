package com.softserveinc.ita.homeproject.homeservice.service.poll.vote;

import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.ALERT_TRYING_TO_REVOTE_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .ALERT_WRONG_ANSWER_COUNT_VALIDATION_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .ALERT_WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .ALERT_WRONG_QUESTIONS_FOR_POLL_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .CANT_CREATE_VOTE_OUTDATED_POLL_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .CANT_CREATE_VOTE_POLL_STATUS_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages.NOT_FOUND_USER_LOGIN_MESSAGE;
import static com.softserveinc.ita.homeproject.homeservice.exception.ExceptionMessages
    .NOT_MATCH_ANSWER_VOTING_QUESTION_VALIDATION_MESSAGE;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.poll.question.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.poll.votes.Vote;
import com.softserveinc.ita.homeproject.homedata.poll.votes.VoteRepository;
import com.softserveinc.ita.homeproject.homedata.user.User;
import com.softserveinc.ita.homeproject.homedata.user.UserRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AnswerVariantDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteQuestionVariantDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.VoteMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final AnswerVariantRepository answerVariantRepository;

    private final PollRepository pollRepository;

    private final PollQuestionRepository questionRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final VoteMapper voteMapper;

    private final ServiceMapper serviceMapper;

    @Transactional
    @Override
    public VoteDto createVote(VoteDto voteDto) {
        Poll votedPoll = validatePollEnabled(voteDto);
        User currentUser = getVoter();
        List<QuestionVoteDto> newQuestionVotes = new ArrayList<>();

        for (QuestionVoteDto questionVoteDto : voteDto.getQuestionVotes()) {
            QuestionVoteDto newQuestionVoteDto;
            newQuestionVoteDto = questionVoteDto.getType().equals(PollQuestionTypeDto.ADVICE)
                ? createAdviceQuestionVote((AdviceQuestionVoteDto) questionVoteDto)
                : createMultipleChoiceQuestionVote((MultipleChoiceQuestionVoteDto) questionVoteDto);
            newQuestionVotes.add(newQuestionVoteDto);
        }

        voteDto.setQuestionVotes(newQuestionVotes);
        validateQuestionVotesCount(voteDto, votedPoll);

        List<Vote> votes = voteMapper.convertToList(voteDto, votedPoll, currentUser);

        validatePollStatus(votedPoll);
        validateCompletionDateTime(votedPoll);
        validateReVoting(votedPoll, currentUser);
        validatePollQuestionsMatching(votes, votedPoll);
        validateAnswerCounts(votes);
        validateAnswersMatching(votes);
        voteRepository.saveAll(votes);

        return voteMapper.convertToDto(votes);
    }

    private Poll validatePollEnabled(VoteDto voteDto) {
        return pollRepository.findById(voteDto.getPollId()).filter(Poll::getEnabled).orElseThrow(
            () -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", voteDto.getPollId())));
    }

    private void validatePollStatus(Poll votedPoll) {
        if (!votedPoll.getStatus().equals(PollStatus.ACTIVE)) {
            throw new BadRequestHomeException(
                String.format(CANT_CREATE_VOTE_POLL_STATUS_MESSAGE, votedPoll.getStatus()));
        }
    }

    private void validateCompletionDateTime(Poll votedPoll) {
        if (votedPoll.getCompletionDate().isBefore(LocalDateTime.now())) {
            throw new BadRequestHomeException(
                String.format(CANT_CREATE_VOTE_OUTDATED_POLL_MESSAGE, votedPoll.getCompletionDate())
            );
        }
    }

    private User getVoter() {
        String voter = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(voter).orElseThrow(
            () -> new NotFoundHomeException(String.format(NOT_FOUND_USER_LOGIN_MESSAGE, voter)));
    }

    private void validateReVoting(Poll votedPoll, User currentUser) {
        if (voteRepository.findByPollIdAndUser(votedPoll.getId(), currentUser) != null) {
            throw new BadRequestHomeException(
                String.format(ALERT_TRYING_TO_REVOTE_MESSAGE, votedPoll.getId()));
        }
    }

    private void validateQuestionVotesCount(VoteDto voteDto, Poll votedPoll) {
        int questionVotesCount = voteDto.getQuestionVotes().size();
        int questionsCount = votedPoll.getPollQuestions().size();
        if (questionVotesCount != questionsCount) {
            throw new BadRequestHomeException(
                String.format(ALERT_WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE, votedPoll.getId()));
        }
    }

    private void validatePollQuestionsMatching(List<Vote> votes, Poll votedPoll) {
        int votedQuestionsCount = votes.size();
        int controlNumber = 0;

        for (Vote vote : votes) {
            Long questionPollId = vote.getQuestion().getPoll().getId();
            if (questionPollId.equals(votedPoll.getId())) {
                controlNumber++;
            }
        }

        if (votedQuestionsCount != controlNumber) {
            throw new BadRequestHomeException(
                String.format(ALERT_WRONG_QUESTIONS_FOR_POLL_MESSAGE, votedPoll.getId()));
        }
    }

    private AdviceQuestionVoteDto createAdviceQuestionVote(AdviceQuestionVoteDto questionVoteDto) {
        AdviceQuestionVoteDto newQuestionVoteDto = new AdviceQuestionVoteDto();
        PollQuestionDto question =
            serviceMapper.convert(getQuestionByIdWithCheckItsExistence(questionVoteDto.getQuestion().getId()),
                PollQuestionDto.class);

        newQuestionVoteDto.setType(PollQuestionTypeDto.ADVICE);
        newQuestionVoteDto.setQuestion(question);
        newQuestionVoteDto.setAnswer(questionVoteDto.getAnswer().trim());

        return newQuestionVoteDto;
    }

    private MultipleChoiceQuestionVoteDto createMultipleChoiceQuestionVote(
        MultipleChoiceQuestionVoteDto questionVoteDto) {
        MultipleChoiceQuestionVoteDto newQuestionVoteDto = new MultipleChoiceQuestionVoteDto();
        List<VoteQuestionVariantDto> voteQuestionVariantDtos = new ArrayList<>();

        newQuestionVoteDto.setType(PollQuestionTypeDto.MULTIPLE_CHOICE);
        newQuestionVoteDto.setQuestion(
            serviceMapper.convert(getQuestionByIdWithCheckItsExistence(questionVoteDto.getQuestion().getId()),
                PollQuestionDto.class));

        for (VoteQuestionVariantDto dto : questionVoteDto.getAnswers()) {
            VoteQuestionVariantDto voteQuestionVariantDto = new VoteQuestionVariantDto();
            AnswerVariantDto answerVariantDto = new AnswerVariantDto();

            AnswerVariant answerVariant = getAnswerVariantByIdWithCheckItsExistence(dto.getAnswerVariant().getId());
            answerVariantDto.setId(dto.getId());
            answerVariantDto.setAnswer(answerVariant.getAnswer());
            voteQuestionVariantDto.setAnswerVariant(answerVariantDto);
            voteQuestionVariantDtos.add(voteQuestionVariantDto);
        }

        newQuestionVoteDto.setAnswers(voteQuestionVariantDtos);

        return newQuestionVoteDto;
    }

    private void validateAnswerCounts(List<Vote> votes) {
        for (Vote vote : votes) {
            if (vote.getQuestion().getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
                Integer maxAnswerCount = ((MultipleChoiceQuestion) vote.getQuestion()).getMaxAnswerCount();
                int realAnswerCount = vote.getVoteAnswerVariants().size();
                if (realAnswerCount < 1 || realAnswerCount > maxAnswerCount) {
                    throw new BadRequestHomeException(
                        String.format(ALERT_WRONG_ANSWER_COUNT_VALIDATION_MESSAGE, vote.getQuestion().getId(),
                            maxAnswerCount));
                }
            }
        }
    }

    private void validateAnswersMatching(List<Vote> votes) {
        for (Vote vote : votes) {
            if (vote.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
                PollQuestion question = vote.getQuestion();
                List<Long> questionAnswersIds = ((MultipleChoiceQuestion) question).getAnswerVariants().stream()
                    .map(AnswerVariant::getId).collect(Collectors.toList());
                List<Long> questionVoteAnswersIds =
                    vote.getVoteAnswerVariants().stream()
                        .map(votedVariant -> votedVariant.getAnswerVariant().getId()).collect(Collectors.toList());

                questionVoteAnswersIds.forEach(answerId -> {
                    if (!questionAnswersIds.contains(answerId)) {
                        throw new BadRequestHomeException(
                            String.format(NOT_MATCH_ANSWER_VOTING_QUESTION_VALIDATION_MESSAGE, answerId,
                                question.getId()));
                    }
                });
            }
        }
    }

    private PollQuestion getQuestionByIdWithCheckItsExistence(Long questionId) {
        return questionRepository.findById(questionId).filter(PollQuestion::getEnabled)
            .orElseThrow(
                () -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll question", questionId)));
    }

    private AnswerVariant getAnswerVariantByIdWithCheckItsExistence(Long answerId) {
        return answerVariantRepository.findById(answerId)
            .orElseThrow(() -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE,
                "Answer variant", answerId)));
    }
}
