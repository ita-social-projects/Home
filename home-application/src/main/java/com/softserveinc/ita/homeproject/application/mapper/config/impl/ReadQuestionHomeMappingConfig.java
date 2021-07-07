package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.model.QuestionType;
import com.softserveinc.ita.homeproject.model.ReadAdviceQuestion;
import com.softserveinc.ita.homeproject.model.ReadAnswerVariant;
import com.softserveinc.ita.homeproject.model.ReadMultipleChoiceQuestion;
import com.softserveinc.ita.homeproject.model.ReadQuestion;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReadQuestionHomeMappingConfig implements
    HomeMappingConfig<PollQuestionDto, ReadQuestion> {

    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<PollQuestionDto, ReadQuestion> typeMap) {

        typeMap.setConverter(fieldsMapper());
    }

    private Converter<PollQuestionDto, ReadQuestion> fieldsMapper() {
        return context -> {
            PollQuestionDto source = context.getSource();
            String sourceTypeValue = source.getType().toString();
            if (sourceTypeValue.equals(PollQuestionTypeDto.ADVICE.toString())) {
                ReadAdviceQuestion adviceDestination = new ReadAdviceQuestion();
                adviceDestination.setId(source.getId());
                adviceDestination.setType(QuestionType.ADVICE);
                adviceDestination.setQuestion(source.getQuestion());
                return adviceDestination;
            } else {
                ReadMultipleChoiceQuestion multiDestination = new ReadMultipleChoiceQuestion();
                multiDestination.setId(source.getId());
                multiDestination.setType(QuestionType.MULTIPLE_CHOICE);
                multiDestination.setQuestion(source.getQuestion());
                multiDestination.setMaxAnswerCount(source.getMaxAnswerCount());
                multiDestination.setAnswerVariants(source.getAnswerVariants().stream()
                    .map(av -> homeMapper.convert(av,
                        ReadAnswerVariant.class)).collect(Collectors.toList()));
                return multiDestination;
            }
        };
    }
}
