package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.List;
import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.entity.polls.results.AdviceResultQuestion;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.AdviceResultQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdviceResultQuestionDtoServiceMappingConfig
    implements ServiceMappingConfig<AdviceResultQuestion, AdviceResultQuestionDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<AdviceResultQuestion, AdviceResultQuestionDto> typeMap) {
        typeMap
            // Если мапить без конвертера, то возникнет ошибка инстанциирования PollQuestionDto из PollQuestion
            // (на него ссылается ResultQuestion). С конвертером всё нормально
            // (для укорочения кода можно создать в dto билдер):
            .setConverter(c -> {
                var source = c.getSource();
                var dest = new AdviceResultQuestionDto();
                dest.setId(source.getId());
                dest.setType(PollQuestionTypeDto.ADVICE);
                dest.setQuestion(serviceMapper.convert(source.getQuestion(), AdviceChoiceQuestionDto.class));
                dest.setVoteCount(source.getVoteCount());
                List<Long> answerList = source.getAnswers().stream()
                    .map(BaseEntity::getId).collect(Collectors.toList());
                dest.setAnswers(answerList);
                return dest;
            });
    }
}
