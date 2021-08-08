package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultipleChoiceQuestionVoteServiceMappingConfig implements
    ServiceMappingConfig<MultipleChoiceQuestionVote, QuestionVoteDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), MultipleChoiceQuestionVoteDto.class))
            .setPostConverter(c -> {
                var dest = c.getDestination();
                dest.setQuestion(serviceMapper.convert(c.getSource().getQuestion(), MultipleChoiceQuestionDto.class));
                return dest;
            });
    }
}
