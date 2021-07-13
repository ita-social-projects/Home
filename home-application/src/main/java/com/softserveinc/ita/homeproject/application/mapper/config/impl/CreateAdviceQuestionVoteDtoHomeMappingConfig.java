package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateAdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateQuestionVoteDto;
import com.softserveinc.ita.homeproject.model.CreateAdviceQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAdviceQuestionVoteDtoHomeMappingConfig implements
    HomeMappingConfig<CreateAdviceQuestionVote, CreateQuestionVoteDto> {

    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<CreateAdviceQuestionVote, CreateQuestionVoteDto> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), CreateAdviceQuestionVoteDto.class));
    }
}
