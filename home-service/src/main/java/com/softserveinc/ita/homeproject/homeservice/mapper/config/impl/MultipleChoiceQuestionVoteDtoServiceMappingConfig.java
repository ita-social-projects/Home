package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestionVote;
import com.softserveinc.ita.homeproject.homeservice.dto.AnswerVariantDto;
import com.softserveinc.ita.homeproject.homeservice.dto.MultipleChoiceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.QuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
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
        typeMap.setProvider(request -> serviceMapper.convert(request.getSource(), MultipleChoiceQuestionVoteDto.class))
            .setPostConverter(multiQuestionConverter());
    }

    private Converter<MultipleChoiceQuestionVote, QuestionVoteDto> multiQuestionConverter() {
        return context -> {
            MultipleChoiceQuestionVote source = context.getSource();
            MultipleChoiceQuestionVoteDto destination = (MultipleChoiceQuestionVoteDto) context.getDestination();
            PollQuestionDto questionDto = destination.getQuestion();
            questionDto.setMaxAnswerCount(((MultipleChoiceQuestion) source.getQuestion()).getMaxAnswerCount());
            questionDto.setAnswerVariants(((MultipleChoiceQuestion) source.getQuestion()).getAnswerVariants().stream()
                .map(e -> serviceMapper.convert(e, AnswerVariantDto.class)).collect(
                    Collectors.toList()));
            destination.setQuestion(questionDto);
            return destination;
        };
    }
}
