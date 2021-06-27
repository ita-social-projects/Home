package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.model.AnswerVariantLookup;
import com.softserveinc.ita.homeproject.model.CreateMultipleChoiceQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultipleChoiceQuestionVoteDtoHomeMappingConfig implements
    HomeMappingConfig<CreateMultipleChoiceQuestionVote, QuestionVoteDto> {

    @Lazy
    private final HomeMapper homeMapper;

    public void addMappings(TypeMap<CreateMultipleChoiceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), MultipleChoiceQuestionVoteDto.class))
            .addMappings(m -> m.skip(QuestionVoteDto::setVoteId))
            .addMappings(mapper -> mapper.map(cmcqv -> cmcqv.getQuestion().getId(), QuestionVoteDto::setQuestionId))
            .setPostConverter(toDtoConverter());
    }

    public Converter<CreateMultipleChoiceQuestionVote, QuestionVoteDto> toDtoConverter() {
        return context -> {
            CreateMultipleChoiceQuestionVote source = context.getSource();
            MultipleChoiceQuestionVoteDto destination = (MultipleChoiceQuestionVoteDto) context.getDestination();
            destination.setAnswerVariantIds(
                source.getAnswers().stream().map(AnswerVariantLookup::getId).collect(Collectors.toList()));
            return context.getDestination();
        };
    }
}
