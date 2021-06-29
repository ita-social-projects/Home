package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAnswerVariantDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadMultipleChoiceQuestionVoteDto;
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
public class ReadMultipleChoiceQuestionVoteHomeMappingConfig implements
    HomeMappingConfig<ReadMultipleChoiceQuestionVoteDto, ReadQuestionVote> {

    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<ReadMultipleChoiceQuestionVoteDto, ReadQuestionVote> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadMultipleChoiceQuestionVote.class))
            .addMappings(mapper -> mapper.map(ReadMultipleChoiceQuestionVoteDto::getId, ReadQuestionVote::setId))
            .setPostConverter(fieldsConverter());
    }

    private Converter<ReadMultipleChoiceQuestionVoteDto, ReadQuestionVote> fieldsConverter() {
        return context -> {
            ReadMultipleChoiceQuestionVoteDto source = context.getSource();
            ReadQuestionVote destination = context.getDestination();
            ReadMultipleChoiceQuestion readQuestion =
                homeMapper.convert(source.getQuestion(), ReadMultipleChoiceQuestion.class);
            ReadMultipleChoiceQuestionVote extendedDestination = (ReadMultipleChoiceQuestionVote) destination;
            extendedDestination.setQuestion(readQuestion);
            List<ReadAnswerVariantDto> answers = source.getAnswers();
            extendedDestination.setAnswers(answers.stream()
                .map(a -> homeMapper.convert(a, ReadAnswerVariant.class)).collect(Collectors.toList()));
            return extendedDestination;
        };
    }
}
