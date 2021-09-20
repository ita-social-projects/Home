package com.softserveinc.ita.homeproject.homeservice.service.poll.question;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.PollRepository;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.question.AdviceChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollQuestionTypeDto;
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

    private final PollQuestionRepository pollQuestionRepository;

    private final PollRepository pollRepository;

    private final ServiceMapper mapper;

    @Transactional
    @Override
    public PollQuestionDto createPollQuestion(Long pollId, PollQuestionDto pollQuestionDto) {
        var poll = getPollById(pollId);

        PollQuestion question = fillMultipleQuestionType(pollQuestionDto);

        question.setPoll(poll);
        question.setEnabled(true);

        pollQuestionRepository.save(question);
        return mapper.convert(question, PollQuestionDto.class);
    }

    @Transactional
    @Override
    public PollQuestionDto updatePollQuestion(Long pollId, Long id, PollQuestionDto updatePollQuestionDto) {
        var poll = getPollById(pollId);

        PollQuestion toUpdate = poll.getPollQuestions().stream()
                .filter(question -> question.getId().equals(id)).findFirst()
                .filter(PollQuestion::getEnabled)
                .orElseThrow(() ->
                        new NotFoundHomeException(
                                String.format(NOT_FOUND_MESSAGE,"Question", id)));


        if (updatePollQuestionDto.getQuestion() != null) {
            toUpdate.setQuestion(updatePollQuestionDto.getQuestion());
        }

        PollQuestionTypeDto existingQuestionType = mapper.convert(toUpdate.getType(), PollQuestionTypeDto.class);

        if (existingQuestionType == updatePollQuestionDto.getType()) {
            return updateQuestion(toUpdate, updatePollQuestionDto);
        } else {
            throw new BadRequestHomeException("Type of the question doesn't match");
        }
    }

    private PollQuestionDto updateQuestion(PollQuestion pollQuestion, PollQuestionDto updatePollQuestionDto) {
        if (pollQuestion.getType().equals(PollQuestionType.MULTIPLE_CHOICE)) {
            return updateMultiChoiceQuestion((MultipleChoiceQuestion) pollQuestion, updatePollQuestionDto);
        } else if (pollQuestion.getType().equals(PollQuestionType.ADVICE)) {
            return updateAdviceChoiceQuestion((AdviceChoiceQuestion) pollQuestion);
        } else {
            throw new NotFoundHomeException("Type of the question is not found");
        }
    }

    private PollQuestionDto updateMultiChoiceQuestion(MultipleChoiceQuestion multipleChoiceQuestion,
                                                      PollQuestionDto pollQuestionDto) {
        if (pollQuestionDto.getAnswerVariants() != null) {
            for (var i = 0; i < multipleChoiceQuestion.getAnswerVariants().size(); i++) {
                multipleChoiceQuestion.getAnswerVariants().get(i).setAnswer(
                        pollQuestionDto.getAnswerVariants().get(i).getAnswer());
            }
        }

        if (pollQuestionDto.getMaxAnswerCount() != null) {
            multipleChoiceQuestion.setMaxAnswerCount(pollQuestionDto.getMaxAnswerCount());
        }

        pollQuestionRepository.save(multipleChoiceQuestion);

        return mapper.convert(multipleChoiceQuestion, PollQuestionDto.class);
    }

    private PollQuestionDto updateAdviceChoiceQuestion(AdviceChoiceQuestion adviceChoiceQuestion) {

        pollQuestionRepository.save(adviceChoiceQuestion);

        return mapper.convert(adviceChoiceQuestion, PollQuestionDto.class);
    }

    private PollQuestion fillMultipleQuestionType(PollQuestionDto pollQuestionDto) {
        if (pollQuestionDto.getType().equals(PollQuestionTypeDto.MULTIPLE_CHOICE)) {
            var multipleChoiceQuestion = mapper.convert(pollQuestionDto, MultipleChoiceQuestion.class);
            multipleChoiceQuestion.getAnswerVariants().forEach(element ->
                    element.setMultipleChoiceQuestion(multipleChoiceQuestion));
            return multipleChoiceQuestion;
        }
        return mapper.convert(pollQuestionDto, AdviceChoiceQuestion.class);
    }

    private Poll getPollById(Long id){
        return pollRepository.findById(id)
                .filter(Poll::getEnabled)
                .filter(poll1 -> poll1.getStatus().equals(PollStatus.DRAFT))
                .orElseThrow(() ->
                        new NotFoundHomeException(
                                String.format(NOT_FOUND_MESSAGE,"Poll", id)));
    }

    @Transactional
    @Override
    public void deactivatePollQuestion(Long pollId, Long pollQuestionId) {
        var poll = getPollById(pollId);

        PollQuestion toDelete = poll.getPollQuestions().stream()
                .filter(question -> question.getId().equals(pollQuestionId)).findFirst()
                .filter(PollQuestion::getEnabled)
                .orElseThrow(() ->
                        new NotFoundHomeException(
                                String.format(NOT_FOUND_MESSAGE,"Question", pollQuestionId)));

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
