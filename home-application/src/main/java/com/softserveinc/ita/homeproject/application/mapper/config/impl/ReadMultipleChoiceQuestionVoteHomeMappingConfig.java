package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.model.ReadAnswerVariant;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.model.ReadQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadMultipleChoiceQuestionVoteHomeMappingConfig
    implements HomeMappingConfig<MultipleChoiceQuestionVoteDto, ReadQuestionVote> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionVoteDto, ReadQuestionVote> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadMultipleChoiceQuestionVote.class))
            .setPostConverter(multiQuestionConverter());
    }

    private Converter<MultipleChoiceQuestionVoteDto, ReadQuestionVote> multiQuestionConverter() {
        return context -> {
            MultipleChoiceQuestionVoteDto source = context.getSource();
            ReadMultipleChoiceQuestionVote destination = (ReadMultipleChoiceQuestionVote) context.getDestination();
            ReadMultipleChoiceQuestion multipleQuestion = new ReadMultipleChoiceQuestion();
            multipleQuestion
                .id(destination.getQuestion().getId())
                .type(destination.getQuestion().getType())
                .setQuestion(destination.getQuestion().getQuestion());
            destination.setQuestion(multipleQuestion
                .maxAnswerCount(source.getQuestion().getMaxAnswerCount())
                .answerVariants(source.getQuestion().getAnswerVariants().stream()
                    .map(dto -> homeMapper.convert(dto, ReadAnswerVariant.class)).collect(Collectors.toList())));
            return destination;
        };
    }
}
