package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.AdviceChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAdviceQuestionVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadAnswerVariantDto;
import com.softserveinc.ita.homeproject.homeservice.exception.NotFoundPollQuestionException;
import com.softserveinc.ita.homeproject.homeservice.mapper.ServiceMapper;
import com.softserveinc.ita.homeproject.homeservice.mapper.config.ServiceMappingConfig;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadAdviceQuestionVoteDtoServiceMappingConfig implements
    ServiceMappingConfig<QuestionVote, ReadAdviceQuestionVoteDto> {

    private final PollQuestionRepository questionRepository;

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<QuestionVote, ReadAdviceQuestionVoteDto> typeMap) {
        typeMap.addMappings(mapper -> mapper.map(QuestionVote::getId, ReadAdviceQuestionVoteDto::setId))
            .setPostConverter(fieldsConverter());
    }

    private Converter<QuestionVote, ReadAdviceQuestionVoteDto> fieldsConverter() {
        return context -> {
            QuestionVote source = context.getSource();
            ReadAdviceQuestionVoteDto destination = context.getDestination();
            Long questionId = source.getQuestionId();
            AdviceChoiceQuestion question = (AdviceChoiceQuestion) questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundPollQuestionException(
                    String.format("Poll question with 'id: %d' is not found", questionId)));
            destination.setQuestion(serviceMapper.convert(question, PollQuestionDto.class));
            AnswerVariant answerVariant = source.getAnswerVariants().get(0);
            destination.setAnswer(serviceMapper.convert(answerVariant, ReadAnswerVariantDto.class));
            return context.getDestination();
        };
    }
}
