package com.softserveinc.ita.homeproject.homeservice.service.poll.question;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.question.AdviceChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.poll.question.DoubleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.exception.BadRequestHomeException;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundHomeException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PollQuestionServiceImpl implements PollQuestionService {

    private static final String UPDATE_QUESTION_TYPE_MISMATCH_MESSAGE = "Type of the PollQuestion doesn't match";

    private static final String QUESTION_TYPE_NOT_FOUND_MESSAGE = "Type of the PollQuestion is not found";

    private final PollQuestionRepository pollQuestionRepository;

    private final PollRepository pollRepository;

    private final AnswerVariantRepository answerRepository;

    private final ServiceMapper mapper;

    @Transactional
    @Override
    public PollQuestionDto createPollQuestion(PollQuestionDto pollQuestionDto) {
        var poll = getPollById(pollQuestionDto.getPollId());
        var question = mapper.convert(pollQuestionDto, PollQuestion.class);
        question.setEnabled(true);
        question.setPoll(poll);
        if (question.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
            ((MultipleChoiceQuestion) question).getAnswerVariants().forEach(answer -> {
                answer.setQuestion(question);
            });
        }
        if (question.getType().equals(PollQuestionType.DOUBLE_CHOICE)) {
            AnswerVariant positiveAnswer = new AnswerVariant();
            AnswerVariant negativeAnswer = new AnswerVariant();

            positiveAnswer.setAnswer("yes");
            positiveAnswer.setQuestion(question);
            negativeAnswer.setAnswer("no");
            negativeAnswer.setQuestion(question);
            ((DoubleChoiceQuestion) question).setMaxAnswerCount(1);
            ((DoubleChoiceQuestion) question).setAnswerVariants(List.of(
                positiveAnswer, negativeAnswer
            ));
        }
        pollQuestionRepository.save(question);
        poll.getPollQuestions().add(question);
        pollRepository.save(poll);
        return mapper.convert(question, PollQuestionDto.class);
    }

    @Transactional
    @Override
    public PollQuestionDto updatePollQuestion(PollQuestionDto updatePollQuestionDto) {
        var poll = getPollById(updatePollQuestionDto.getPollId());

        PollQuestion toUpdate = poll.getPollQuestions().stream()
            .filter(question -> question.getId().equals(updatePollQuestionDto.getId())).findFirst()
            .filter(PollQuestion::getEnabled)
            .orElseThrow(() ->
                new NotFoundHomeException(
                    String.format(NOT_FOUND_MESSAGE, "PollQuestion", updatePollQuestionDto.getId())));

        if (updatePollQuestionDto.getQuestion() != null) {
            toUpdate.setQuestion(updatePollQuestionDto.getQuestion());
        }

        PollQuestionTypeDto existingQuestionType = mapper.convert(toUpdate.getType(), PollQuestionTypeDto.class);

        if (existingQuestionType == updatePollQuestionDto.getType()) {
            return updateQuestion(toUpdate, updatePollQuestionDto);
        } else {
            throw new BadRequestHomeException(UPDATE_QUESTION_TYPE_MISMATCH_MESSAGE);
        }
    }

    private PollQuestionDto updateQuestion(PollQuestion pollQuestion, PollQuestionDto updatePollQuestionDto) {
        if (pollQuestion.getType().equals(PollQuestionType.MULTIPLE_CHOICE)
            || pollQuestion.getType().equals(PollQuestionType.DOUBLE_CHOICE)) {
            return updateMultiChoiceQuestion((MultipleChoiceQuestion) pollQuestion,
                (MultipleChoiceQuestionDto) updatePollQuestionDto);
        } else if (pollQuestion.getType().equals(PollQuestionType.ADVICE)) {
            return updateAdviceChoiceQuestion((AdviceChoiceQuestion) pollQuestion);
        } else {
            throw new NotFoundHomeException(QUESTION_TYPE_NOT_FOUND_MESSAGE);
        }
    }

    private PollQuestionDto updateMultiChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion,
                                                      MultipleChoiceQuestionDto multipleChoiceQuestionDto) {
        if (multipleChoiceQuestionDto.getAnswerVariants() != null) {
            multipleChoiceQuestion.setAnswerVariants(
                multipleChoiceQuestionDto.getAnswerVariants().stream()
                    .map(answerDto -> mapper.convert(answerDto, AnswerVariant.class))
                    .peek(answer -> {
                        answer.setQuestion(multipleChoiceQuestion);
                        answerRepository.save(answer);
                    })
                    .collect(Collectors.toList()));
        }

        if (multipleChoiceQuestionDto.getMaxAnswerCount() != null) {
            multipleChoiceQuestion.setMaxAnswerCount(multipleChoiceQuestionDto.getMaxAnswerCount());
        }

        pollQuestionRepository.save(multipleChoiceQuestion);

        return mapper.convert(multipleChoiceQuestion, PollQuestionDto.class);
    }

    private PollQuestionDto updateAdviceChoiceQuestion(AdviceChoiceQuestion adviceChoiceQuestion) {

        pollQuestionRepository.save(adviceChoiceQuestion);

        return mapper.convert(adviceChoiceQuestion, PollQuestionDto.class);
    }

    private Poll getPollById(Long id) {
        return pollRepository.findById(id)
            .filter(Poll::getEnabled)
            .filter(poll1 -> poll1.getStatus().equals(PollStatus.DRAFT))
            .orElseThrow(() ->
                new NotFoundHomeException(
                    String.format(NOT_FOUND_MESSAGE, "Poll", id)));
    }

    @Transactional
    @Override
    public void deactivatePollQuestion(Long pollId, Long pollQuestionId) {
        var poll = getPollById(pollId);

        PollQuestion toDelete = poll.getPollQuestions().stream()
            .filter(question -> question.getId().equals(pollQuestionId)).findFirst()
            .filter(PollQuestion::getEnabled)
            .orElseThrow(
                () -> new NotFoundHomeException(String
                    .format(NOT_FOUND_MESSAGE, "PollQuestion", pollQuestionId)));

        toDelete.setEnabled(false);
        pollQuestionRepository.save(toDelete);
    }

    @Override
    public Page<PollQuestionDto> findAll(Integer pageNumber,
                                         Integer pageSize,
                                         Specification<PollQuestion> specification) {
        specification = specification
            .and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder
                .equal(root.get("poll").get("enabled"), true));
        return pollQuestionRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize))
            .map(pollQuestion -> mapper.convert(pollQuestion, PollQuestionDto.class));
    }
}
