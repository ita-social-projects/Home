package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.model.CreateMultipleChoiceQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateMultipleChoiceQuestionVoteHomeMappingConfig implements
    HomeMappingConfig<CreateMultipleChoiceQuestionVote, QuestionVoteDto> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<CreateMultipleChoiceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), MultipleChoiceQuestionVoteDto.class));
    }
}
