package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.AdviceChoiceQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdviceChoiceQuestionDtoServiceMappingConfig
    implements ServiceMappingConfig<AdviceChoiceQuestion, PollQuestionDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<AdviceChoiceQuestion, PollQuestionDto> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), AdviceChoiceQuestionDto.class));
    }
}
