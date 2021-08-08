package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.model.ReadAdviceQuestion;
import com.softserveinc.ita.homeproject.model.ReadQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadAdviceQuestionHomeMappingConfig
    implements HomeMappingConfig<AdviceChoiceQuestionDto, ReadQuestion> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<AdviceChoiceQuestionDto, ReadQuestion> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadAdviceQuestion.class));
    }
}
