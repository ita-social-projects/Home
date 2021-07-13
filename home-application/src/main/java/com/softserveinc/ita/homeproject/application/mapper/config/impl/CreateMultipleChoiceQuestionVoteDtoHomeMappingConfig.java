package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateMultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateQuestionVoteDto;
import com.softserveinc.ita.homeproject.model.AnswerVariantLookup;
import com.softserveinc.ita.homeproject.model.CreateMultipleChoiceQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMultipleChoiceQuestionVoteDtoHomeMappingConfig implements
    HomeMappingConfig<CreateMultipleChoiceQuestionVote, CreateQuestionVoteDto> {

    @Lazy
    private final HomeMapper homeMapper;

    public void addMappings(TypeMap<CreateMultipleChoiceQuestionVote, CreateQuestionVoteDto> typeMap) {
        typeMap.setProvider(
                request -> homeMapper.convert(request.getSource(), CreateMultipleChoiceQuestionVoteDto.class))
            .addMappings(m -> m.skip(CreateQuestionVoteDto::setVoteId))
            .addMappings(
                mapper -> mapper.map(cmcqv -> cmcqv.getQuestion().getId(), CreateQuestionVoteDto::setQuestionId))
            .setPostConverter(toDtoConverter());
    }

    public Converter<CreateMultipleChoiceQuestionVote, CreateQuestionVoteDto> toDtoConverter() {
        return context -> {
            CreateMultipleChoiceQuestionVote source = context.getSource();
            CreateMultipleChoiceQuestionVoteDto destination =
                (CreateMultipleChoiceQuestionVoteDto) context.getDestination();
            destination.setAnswerVariantIds(
                source.getAnswers().stream().map(AnswerVariantLookup::getId).collect(Collectors.toList()));
            return context.getDestination();
        };
    }
}
