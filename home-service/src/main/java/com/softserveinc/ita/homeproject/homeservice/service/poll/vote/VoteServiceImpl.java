package com.softserveinc.ita.homeproject.homeservice.service.poll.vote;

import static com.softserveinc.ita.homeproject.homeservice.service.QueryableService.NOT_FOUND_MESSAGE;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homedata.entity.polls.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.entity.polls.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.entity.polls.results.AdviceResultQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.Poll;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.AdviceQuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.MultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.Vote;
import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.VoteQuestionVariant;
import com.softserveinc.ita.homeproject.homedata.repository.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollRepository;
import com.softserveinc.ita.homeproject.homedata.repository.QuestionVoteRepository;
import com.softserveinc.ita.homeproject.homedata.repository.ResultQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.UserRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteQuestionVariantRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.enums.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.votes.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.votes.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.votes.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.votes.VoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.votes.VoteQuestionVariantDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VoteServiceImpl implements VoteService {

    private static final String POLL_STATUS_VALIDATION_MESSAGE = "Can't create vote on poll with status: '%s'";

    private static final String USER_NOT_FOUND_MESSAGE = "User with 'login: %s' is not found";

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

    private final AnswerVariantRepository answerVariantRepository;

    private final PollRepository pollRepository;

    private final PollQuestionRepository questionRepository;

    private final VoteRepository voteRepository;

    private final UserRepository userRepository;

    private final VoteQuestionVariantRepository voteQuestionVariantRepository;

    private final ResultQuestionRepository resultQuestionRepository;

    private final QuestionVoteRepository questionVoteRepository;

    private final ServiceMapper mapper;

    @Transactional
    @Override
    public VoteDto createVote(VoteDto voteDto) {
        Poll votedPoll = validatePollEnabled(voteDto);
        validatePollStatus(votedPoll);
        User currentUser = getVoter();
        validateReVoting(votedPoll, currentUser);
        validateQuestionVotesCount(voteDto, votedPoll);

        var newVote = new Vote();
        newVote.setPollId(votedPoll.getId());
        newVote.setUser(currentUser);
        voteRepository.save(newVote);
        var newQuestionVotes = new ArrayList<QuestionVote>();
        for (QuestionVoteDto questionVoteDto : voteDto.getQuestionVotes()) {
            QuestionVote newQuestionVote;
            newQuestionVote = questionVoteDto.getType().equals(PollQuestionTypeDto.ADVICE)
                ? createAdviceQuestionVote((AdviceQuestionVoteDto) questionVoteDto)
                : createMultipleChoiceQuestionVote((MultipleChoiceQuestionVoteDto) questionVoteDto);
            newQuestionVote.setVote(newVote);
            newQuestionVotes.add(newQuestionVote);
        }

        validatePollQuestionsMatching(newQuestionVotes, votedPoll);
        validateAnswerCounts(newQuestionVotes);
        validateAnswersMatching(newQuestionVotes);

        newVote.setQuestionVotes(newQuestionVotes);
        voteRepository.save(newVote);

        return mapper.convert(newVote, VoteDto.class);
    }

    private Poll validatePollEnabled(VoteDto voteDto) {
        return pollRepository.findById(voteDto.getPollId()).filter(Poll::getEnabled).orElseThrow(
            () -> new NotFoundHomeException(String.format(NOT_FOUND_MESSAGE, "Poll", voteDto.getPollId())));
    }

    private void validatePollStatus(Poll votedPoll) {
        if (!votedPoll.getStatus().equals(PollStatus.ACTIVE)) {
            throw new BadRequestHomeException(
                String.format(POLL_STATUS_VALIDATION_MESSAGE, votedPoll.getStatus()));
        }
    }

    private User getVoter() {
        String voter = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(voter).orElseThrow(
            () -> new NotFoundHomeException(String.format(USER_NOT_FOUND_MESSAGE, voter)));
    }

    private void validateReVoting(Poll votedPoll, User currentUser) {
        if (voteRepository.findByPollIdAndUser(votedPoll.getId(), currentUser) != null) {
            throw new BadRequestHomeException(
                String.format(TRYING_TO_REVOTE_MESSAGE, votedPoll.getId()));
        }
    }

    private void validateQuestionVotesCount(VoteDto voteDto, Poll votedPoll) {
        int questionVotesCount = voteDto.getQuestionVotes().size();
        int questionsCount = votedPoll.getPollQuestions().size();
        if (questionVotesCount != questionsCount) {
            throw new BadRequestHomeException(
                String.format(WRONG_QUESTIONS_COUNT_FOR_POLL_MESSAGE, votedPoll.getId()));
        }
    }

    private void validatePollQuestionsMatching(List<QuestionVote> questionVotes, Poll votedPoll) {
        int votedQuestionsCount = questionVotes.size();
        int controlNumber = 0;
        for (QuestionVote questionVote : questionVotes) {
            Long questionPollId = questionVote.getQuestion().getPoll().getId();
            if (questionPollId.equals(votedPoll.getId())) {
                controlNumber++;
            }
        }
        if (votedQuestionsCount != controlNumber) {
            throw new BadRequestHomeException(
                String.format(WRONG_QUESTIONS_FOR_POLL_MESSAGE, votedPoll.getId()));
        }
    }

    private AdviceQuestionVote createAdviceQuestionVote(AdviceQuestionVoteDto questionVoteDto) {
        var newQuestionVote = new AdviceQuestionVote();
        newQuestionVote.setType(PollQuestionType.ADVICE);
        var question = getQuestionByIdWithCheckItsExistence(questionVoteDto.getQuestion().getId());
        newQuestionVote.setQuestion(question);
        newQuestionVote.setAnswer(questionVoteDto.getAnswer().trim());
        checkExistenceAndSetResultQuestion(newQuestionVote, question);
        return newQuestionVote;
    }

    private MultipleChoiceQuestionVote createMultipleChoiceQuestionVote(MultipleChoiceQuestionVoteDto questionVoteDto) {
        var newQuestionVote = new MultipleChoiceQuestionVote();
        newQuestionVote.setType(PollQuestionType.MULTIPLE_CHOICE);
        newQuestionVote.setQuestion(getQuestionByIdWithCheckItsExistence(questionVoteDto.getQuestion().getId()));
        var voteQuestionVariantList = new ArrayList<VoteQuestionVariant>();
        for (VoteQuestionVariantDto dto : questionVoteDto.getAnswers()) {
            var voteVariant = new VoteQuestionVariant();
            voteVariant.setQuestionVote(newQuestionVote);
            voteVariant.setAnswerVariant(getAnswerVariantByIdWithCheckItsExistence(dto.getAnswerVariant().getId()));
            voteQuestionVariantRepository.save(voteVariant);
            voteQuestionVariantList.add(voteVariant);
        }
        newQuestionVote.setAnswers(voteQuestionVariantList);
        return questionVoteRepository.save(newQuestionVote);
    }

    private void validateAnswerCounts(List<QuestionVote> questionVotes) {
        for (QuestionVote questionVote : questionVotes) {
            if (questionVote.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
                Integer maxAnswerCount = ((MultipleChoiceQuestion) questionVote.getQuestion()).getMaxAnswerCount();
                int realAnswerCount = ((MultipleChoiceQuestionVote) questionVote).getAnswers().size();
                if (realAnswerCount < 1 || realAnswerCount > maxAnswerCount) {
                    throw new BadRequestHomeException(
                        String.format(WRONG_ANSWER_COUNT_VALIDATION_MESSAGE, questionVote.getQuestion().getId(),
                            maxAnswerCount));
                }
            }
        }
    }

    private void validateAnswersMatching(List<QuestionVote> questionVotes) {
        for (QuestionVote questionVote : questionVotes) {
            if (questionVote.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
                PollQuestion question = questionVote.getQuestion();
                List<Long> questionAnswersIds = ((MultipleChoiceQuestion) question).getAnswerVariants().stream()
                    .map(AnswerVariant::getId).collect(Collectors.toList());
                List<Long> questionVoteAnswersIds =
                    ((MultipleChoiceQuestionVote) questionVote).getAnswers().stream()
                        .map(votedVariant -> votedVariant.getAnswerVariant().getId()).collect(Collectors.toList());
                questionVoteAnswersIds.forEach(answerId -> {
                    if (!questionAnswersIds.contains(answerId)) {
                        throw new BadRequestHomeException(
                            String.format(ANSWER_DOES_NOT_MATCH_QUESTION_VALIDATION_MESSAGE, answerId,
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

    private void checkExistenceAndSetResultQuestion(AdviceQuestionVote questionVote, PollQuestion question) {
        var checkedResultQuestion = resultQuestionRepository.findByQuestion(question);
        AdviceResultQuestion adviceResultQuestion;
        if (checkedResultQuestion == null) {
            adviceResultQuestion = new AdviceResultQuestion();
            adviceResultQuestion.setType(PollQuestionType.ADVICE);
            adviceResultQuestion.setQuestion(question);
            ArrayList<AdviceQuestionVote> answerList = new ArrayList<>();
            answerList.add(questionVote);
            adviceResultQuestion.setAnswers(answerList);
        } else {
            adviceResultQuestion = (AdviceResultQuestion) checkedResultQuestion;
            List<AdviceQuestionVote> answerList = adviceResultQuestion.getAnswers();
            answerList.add(questionVote);
            adviceResultQuestion.setAnswers(answerList);
        }
        adviceResultQuestion.setVoteCount(adviceResultQuestion.getAnswers().size());
        resultQuestionRepository.save(adviceResultQuestion);
        questionVote.setResultQuestion(adviceResultQuestion);
    }
}
