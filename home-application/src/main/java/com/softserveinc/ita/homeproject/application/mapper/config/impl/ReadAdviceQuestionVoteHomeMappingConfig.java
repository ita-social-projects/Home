package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.model.ReadAdviceQuestion;
import com.softserveinc.ita.homeproject.model.ReadAdviceQuestionVote;
import com.softserveinc.ita.homeproject.model.ReadQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadAdviceQuestionVoteHomeMappingConfig
    implements HomeMappingConfig<AdviceQuestionVoteDto, ReadQuestionVote> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<AdviceQuestionVoteDto, ReadQuestionVote> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadAdviceQuestionVote.class))
            .setPostConverter(questionConverter());
    }

    private Converter<AdviceQuestionVoteDto, ReadQuestionVote> questionConverter() {
        return context -> {
            AdviceQuestionVoteDto source = context.getSource();
            ReadQuestionVote destination = context.getDestination();
            destination.setQuestion(homeMapper.convert(source.getQuestion(), ReadAdviceQuestion.class));
            return destination;
        };
    }
}
