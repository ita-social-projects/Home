package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.List;
import java.util.Objects;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PollQuestionDtoServiceMappingConfig implements
    ServiceMappingConfig<PollQuestion, PollQuestionDto> {

    @Override
    public void addMappings(TypeMap<PollQuestion, PollQuestionDto> typeMap) {
        typeMap
            .addMappings(mapper -> mapper.map(PollQuestion::getId, PollQuestionDto::setId))
            .addMappings(mapper -> mapper.skip(PollQuestionDto::setType))
            .addMappings(mapper -> mapper.map(PollQuestion::getQuestion, PollQuestionDto::setQuestion))
            .setPostConverter(fieldsConverter());
    }

    private Converter<PollQuestion, PollQuestionDto> fieldsConverter() {
        return context -> {
            PollQuestion source = context.getSource();
            PollQuestionDto destination = context.getDestination();
            String sourceTypeValue = source.getType().toString();
            destination.setType(PollQuestionTypeDto.valueOf(sourceTypeValue));
            if (Objects.equals(sourceTypeValue, "multiple_choice")) {
                destination.setMaxAnswerCount(((MultipleChoiceQuestion) source).getMaxAnswerCount());
                List<AnswerVariant> answerVariants = ((MultipleChoiceQuestion) source).getAnswerVariants();
                destination.setAnswerVariants(answerVariants);
            }
            return context.getDestination();
        };
    }
}
