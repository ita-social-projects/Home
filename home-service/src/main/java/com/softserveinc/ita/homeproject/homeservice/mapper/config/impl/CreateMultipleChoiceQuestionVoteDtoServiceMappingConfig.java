package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.entity.Vote;
import com.softserveinc.ita.homeproject.homedata.repository.AnswerVariantRepository;
import com.softserveinc.ita.homeproject.homedata.repository.VoteRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateMultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundAnswerVariantException;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Conditions;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMultipleChoiceQuestionVoteDtoServiceMappingConfig
    implements ServiceMappingConfig<CreateMultipleChoiceQuestionVoteDto, QuestionVote> {

    private final VoteRepository voteRepository;

    private final AnswerVariantRepository answerVariantRepository;

    @Override
    public void addMappings(TypeMap<CreateMultipleChoiceQuestionVoteDto, QuestionVote> typeMap) {
        typeMap.addMappings(mapper -> mapper.when(Conditions.isNotNull())
                .map(CreateMultipleChoiceQuestionVoteDto::getId, QuestionVote::setId))
            .addMappings(mapper -> mapper
                .map(CreateMultipleChoiceQuestionVoteDto::getQuestionId, QuestionVote::setQuestionId))
            .setPostConverter(fieldsConverter());
    }

    public Converter<CreateMultipleChoiceQuestionVoteDto, QuestionVote> fieldsConverter() {
        return context -> {
            CreateMultipleChoiceQuestionVoteDto source = context.getSource();
            QuestionVote destination = context.getDestination();
            Long voteId = source.getVoteId();
            if (voteId != null) {
                Vote vote = voteRepository.findById(voteId).orElse(null);
                destination.setVote(vote);
            }
            List<AnswerVariant> answers = source.getAnswerVariantIds().stream().map(
                id -> answerVariantRepository.findById(id).orElseThrow(() -> new NotFoundAnswerVariantException(
                    String.format("Answer variant with 'id: %d' is not found", id)))).collect(Collectors.toList());
            destination.setAnswerVariants(answers);
            return context.getDestination();
        };
    }
}
