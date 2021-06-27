package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundAnswerVariantException;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultipleChoiceQuestionVoteDtoServiceMappingConfig
    implements ServiceMappingConfig<MultipleChoiceQuestionVoteDto, QuestionVote> {

    private final VoteRepository voteRepository;

    private final PollQuestionRepository questionRepository;

    private final AnswerVariantRepository answerVariantRepository;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionVoteDto, QuestionVote> typeMap) {
        typeMap.addMappings(mapper -> mapper.when(Conditions.isNotNull())
            .map(MultipleChoiceQuestionVoteDto::getId, QuestionVote::setId)).setPostConverter(fieldsConverter());
    }

    public Converter<MultipleChoiceQuestionVoteDto, QuestionVote> fieldsConverter() {
        return context -> {
            MultipleChoiceQuestionVoteDto source = context.getSource();
            QuestionVote destination = context.getDestination();
            Long voteId = source.getVoteId();
            if (voteId != null) {
                Vote vote = voteRepository.findById(voteId).orElse(null);
                destination.setVote(vote);
            }
            Long questionId = source.getQuestionId();
            if (questionId != null) {
                MultipleChoiceQuestion question =
                    (MultipleChoiceQuestion) questionRepository.findById(questionId).orElse(null);
                destination.setQuestion(question);
            }
            List<AnswerVariant> answers = source.getAnswerVariantIds().stream().map(
                id -> answerVariantRepository.findById(id).orElseThrow(() -> new NotFoundAnswerVariantException(
                    String.format("Answer variant with 'id: %d' is not found", id)))).collect(Collectors.toList());
            destination.setAnswerVariants(answers);
            return context.getDestination();
        };
    }
}
