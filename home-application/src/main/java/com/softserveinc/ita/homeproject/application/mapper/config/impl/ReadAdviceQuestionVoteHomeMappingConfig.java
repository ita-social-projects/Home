package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAnswerVariantDto;
import com.softserveinc.ita.homeproject.model.ReadAdviceQuestionVote;
import com.softserveinc.ita.homeproject.model.ReadAnswerVariant;
import com.softserveinc.ita.homeproject.model.ReadQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadAdviceQuestionVoteHomeMappingConfig implements
    HomeMappingConfig<ReadAdviceQuestionVoteDto, ReadQuestionVote> {

    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<ReadAdviceQuestionVoteDto, ReadQuestionVote> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadAdviceQuestionVote.class))
            .addMappings(mapper -> mapper.map(ReadAdviceQuestionVoteDto::getId, ReadQuestionVote::setId))
            .addMappings(mapper -> mapper.map(ReadAdviceQuestionVoteDto::getQuestion, ReadQuestionVote::setQuestion))
            .setPostConverter(fieldsConverter());
    }

    private Converter<ReadAdviceQuestionVoteDto, ReadQuestionVote> fieldsConverter() {
        return context -> {
            ReadAdviceQuestionVoteDto source = context.getSource();
            ReadQuestionVote destination = context.getDestination();
            ReadAdviceQuestionVote extendedDestination = (ReadAdviceQuestionVote) destination;
            ReadAnswerVariantDto answer = source.getAnswer();
            extendedDestination.setAnswer(homeMapper.convert(answer, ReadAnswerVariant.class));
            return extendedDestination;
        };
    }
}
