package com.softserveinc.ita.homeproject.homeservice.mapper.config.impl;

import com.softserveinc.ita.homeproject.homedata.entity.MultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.homedata.entity.QuestionVote;
import com.softserveinc.ita.homeproject.homedata.repository.PollQuestionRepository;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadMultipleChoiceQuestionVoteDto;
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
public class ReadMultipleChoiceQuestionVoteDtoServiceMappingConfig implements
    ServiceMappingConfig<QuestionVote, ReadMultipleChoiceQuestionVoteDto> {

    private final PollQuestionRepository questionRepository;

    @Lazy
    private final ServiceMapper serviceMapper;

    @Override
    public void addMappings(TypeMap<QuestionVote, ReadMultipleChoiceQuestionVoteDto> typeMap) {
        typeMap.addMappings(mapper -> mapper.map(QuestionVote::getId, ReadMultipleChoiceQuestionVoteDto::setId))
            .addMappings(
                mapper -> mapper.map(QuestionVote::getAnswerVariants, ReadMultipleChoiceQuestionVoteDto::setAnswers))
            .addMappings(mapper -> mapper.skip(ReadMultipleChoiceQuestionVoteDto::setQuestion))
            .setPostConverter(fieldsConverter());
    }

    private Converter<QuestionVote, ReadMultipleChoiceQuestionVoteDto> fieldsConverter() {
        return context -> {
            QuestionVote source = context.getSource();
            ReadMultipleChoiceQuestionVoteDto destination = context.getDestination();
            Long questionId = source.getQuestionId();
            MultipleChoiceQuestion question = (MultipleChoiceQuestion) questionRepository.findById(questionId)
                .orElseThrow(() -> new NotFoundPollQuestionException(
                    String.format("Poll question with 'id: %d' is not found", questionId)));
            destination.setQuestion(serviceMapper.convert(question, PollQuestionDto.class));
            return context.getDestination();
        };
    }
}
