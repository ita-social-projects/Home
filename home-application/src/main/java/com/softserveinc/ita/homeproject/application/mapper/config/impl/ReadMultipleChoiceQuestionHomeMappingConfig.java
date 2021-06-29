package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadMultipleChoiceQuestionHomeMappingConfig implements
    HomeMappingConfig<PollQuestionDto, ReadMultipleChoiceQuestion> {

    @Override
    public void addMappings(TypeMap<PollQuestionDto, ReadMultipleChoiceQuestion> typeMap) {
        typeMap
            .addMappings(m -> m.map(PollQuestionDto::getId, ReadMultipleChoiceQuestion::setId))
            .addMappings(m -> m.map(PollQuestionDto::getQuestion, ReadMultipleChoiceQuestion::setQuestion))
            .addMappings(m -> m.map(PollQuestionDto::getMaxAnswerCount, ReadMultipleChoiceQuestion::setMaxAnswerCount))
            .addMappings(m -> m.map(PollQuestionDto::getAnswerVariants, ReadMultipleChoiceQuestion::setAnswerVariants));
    }
}
