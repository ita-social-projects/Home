package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.model.ReadQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadMultipleChoiceQuestionVoteHomeMappingConfig
    implements HomeMappingConfig<MultipleChoiceQuestionVoteDto, ReadQuestionVote> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionVoteDto, ReadQuestionVote> typeMap) {
        typeMap.setProvider(request -> homeMapper.convert(request.getSource(), ReadMultipleChoiceQuestionVote.class))
            .setPostConverter(c -> {
                var dest = c.getDestination();
                dest.setQuestion(homeMapper.convert(c.getSource().getQuestion(), ReadMultipleChoiceQuestion.class));
                return dest;
            });
    }
}
