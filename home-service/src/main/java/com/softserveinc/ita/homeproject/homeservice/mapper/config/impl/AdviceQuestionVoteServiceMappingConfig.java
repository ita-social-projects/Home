package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.AdviceQuestionVote;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdviceQuestionVoteServiceMappingConfig
    implements ServiceMappingConfig<AdviceQuestionVote, QuestionVoteDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<AdviceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), AdviceQuestionVoteDto.class))
            .setPostConverter(c -> {
                var dest = c.getDestination();
                dest.setQuestion(serviceMapper.convert(c.getSource().getQuestion(), AdviceChoiceQuestionDto.class));
                return dest;
            });
    }
}

