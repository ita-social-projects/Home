package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteQuestionVariantDto;
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
        typeMap
            // 1) Если просто подставить провайдер,
            // то возникнет ошибка инстанциирования PollQuestionDto из QuestionLookup:
            //    .setProvider(request -> homeMapper.convert(request.getSource(), MultipleChoiceQuestionVoteDto.class));
            // 2) С конвертером всё нормально (для укорочения кода можно создать в энтити билдер):
            .setConverter(c -> {
                CreateMultipleChoiceQuestionVote source = c.getSource();
                var dest = new MultipleChoiceQuestionVoteDto();
                dest.setType(PollQuestionTypeDto.MULTIPLE_CHOICE);
                dest.setQuestion(homeMapper.convert(source.getQuestion(), MultipleChoiceQuestionDto.class));
                dest.setAnswers(source.getAnswers().stream()
                    .map(answerLookup -> homeMapper.convert(answerLookup, VoteQuestionVariantDto.class))
                    .collect(Collectors.toList()));
                return dest;
            });
    }
}
