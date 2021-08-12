package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.polls.votes.MultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteQuestionVariantDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MultipleChoiceQuestionVoteDtoServiceMappingConfig implements
    ServiceMappingConfig<MultipleChoiceQuestionVote, QuestionVoteDto> {

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<MultipleChoiceQuestionVote, QuestionVoteDto> typeMap) {
        typeMap
            // 1) Если просто подставить провайдер,
            // то возникнет ошибка инстанциирования PollQuestionDto из PollQuestion:
            // .setProvider(request -> serviceMapper.convert(request.getSource(), MultipleChoiceQuestionVoteDto.class));
            // 2) С конвертером всё нормально (для укорочения кода можно создать в dto билдер):
            .setConverter(c -> {
                MultipleChoiceQuestionVote source = c.getSource();
                var dest = new MultipleChoiceQuestionVoteDto();
                dest.setId(source.getId());
                dest.setVoteId(source.getVote().getId());
                dest.setType(PollQuestionTypeDto.MULTIPLE_CHOICE);
                dest.setQuestion(serviceMapper.convert(source.getQuestion(), MultipleChoiceQuestionDto.class));
                dest.setAnswers(source.getAnswers().stream().map(answer -> serviceMapper.convert(answer,
                    VoteQuestionVariantDto.class)).collect(Collectors.toList()));
                return dest;
            });
    }
}
