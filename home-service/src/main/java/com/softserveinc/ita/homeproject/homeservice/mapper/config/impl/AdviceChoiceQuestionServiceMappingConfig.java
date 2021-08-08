package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.AdviceChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.PollQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdviceChoiceQuestionServiceMappingConfig
    implements ServiceMappingConfig<AdviceChoiceQuestionDto, PollQuestion> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<AdviceChoiceQuestionDto, PollQuestion> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), AdviceChoiceQuestion.class));
    }
}
