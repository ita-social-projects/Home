package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.AdviceQuestionVote;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceResultQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdviceQuestionVoteDtoServiceMappingConfig
    implements ServiceMappingConfig<AdviceQuestionVote, QuestionVoteDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<AdviceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap
            // 1) Если просто подставить провайдер,
            // то возникнет ошибка инстанциирования PollQuestionDto из PollQuestion:
            //    .setProvider(request -> serviceMapper.convert(request.getSource(), AdviceQuestionVoteDto.class));
            // 2) С конвертером всё нормально (для укорочения кода можно создать в dto билдер):
            .setConverter(c -> {
                AdviceQuestionVote source = c.getSource();
                var dest = new AdviceQuestionVoteDto();
                dest.setId(source.getId());
                dest.setVoteId(source.getVote().getId());
                dest.setType(PollQuestionTypeDto.ADVICE);
                dest.setQuestion(serviceMapper.convert(source.getQuestion(), AdviceChoiceQuestionDto.class));
                dest.setAnswer(source.getAnswer());
                dest.setResultQuestion(
                    serviceMapper.convert(source.getResultQuestion(), AdviceResultQuestionDto.class));
                return dest;
            });
    }
}

