package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.model.CreateAdviceQuestionVote;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CreateAdviceQuestionVoteHomeMappingConfig
    implements HomeMappingConfig<CreateAdviceQuestionVote, QuestionVoteDto> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<CreateAdviceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap
            // 1) Если просто подставить провайдер,
            // то возникнет ошибка инстанциирования PollQuestionDto из QuestionLookup:
            //    .setProvider(request -> homeMapper.convert(request.getSource(), AdviceQuestionVoteDto.class));
            // 2) С конвертером всё нормально (для укорочения кода можно создать в энтити билдер):
            .setConverter(c -> {
                CreateAdviceQuestionVote source = c.getSource();
                var dest = new AdviceQuestionVoteDto();
                dest.setType(PollQuestionTypeDto.ADVICE);
                dest.setQuestion(homeMapper.convert(source.getQuestion(), AdviceChoiceQuestionDto.class));
                dest.setAnswer(source.getAnswer().getAnswer());
                return dest;
            });
    }
}
