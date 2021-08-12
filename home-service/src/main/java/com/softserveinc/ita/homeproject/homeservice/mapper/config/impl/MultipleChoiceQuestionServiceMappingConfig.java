package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.PollQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultipleChoiceQuestionServiceMappingConfig
    implements ServiceMappingConfig<MultipleChoiceQuestionDto, PollQuestion> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionDto, PollQuestion> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), MultipleChoiceQuestion.class));
    }
}
