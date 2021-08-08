package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.model.CreateMultipleChoiceQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMultipleChoiceQuestionHomeMappingConfig
    implements HomeMappingConfig<CreateMultipleChoiceQuestion, PollQuestionDto> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<CreateMultipleChoiceQuestion, PollQuestionDto> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), MultipleChoiceQuestionDto.class));
    }
}
