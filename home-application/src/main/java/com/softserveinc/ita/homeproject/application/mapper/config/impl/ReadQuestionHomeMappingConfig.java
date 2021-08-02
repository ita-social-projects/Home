package com.softserveinc.ita.homeproject.application.mapper.config.impl;

import java.util.stream.Collectors;

import com.softserveinc.ita.homeproject.application.mapper.HomeMapper;
import com.softserveinc.ita.homeproject.application.mapper.config.HomeMappingConfig;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollQuestionTypeDto;
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
public class ReadQuestionHomeMappingConfig
    implements HomeMappingConfig<PollQuestionDto, ReadQuestion> {
    @Lazy
    private final HomeMapper homeMapper;

    @Override
    public void addMappings(TypeMap<PollQuestionDto, ReadQuestion> typeMap) {
        typeMap.setPostConverter(questionConverter());
    }

    private Converter<PollQuestionDto, ReadQuestion> questionConverter() {
        return context -> {
            PollQuestionDto source = context.getSource();
            if (source.getType().equals(PollQuestionTypeDto.ADVICE)) {
                return homeMapper.convert(source, ReadAdviceQuestion.class);
            } else {
                ReadMultipleChoiceQuestion readQuestion = homeMapper.convert(source, ReadMultipleChoiceQuestion.class);
                readQuestion.setMaxAnswerCount(source.getMaxAnswerCount());
                readQuestion.setAnswerVariants(source.getAnswerVariants().stream().map(dto -> homeMapper.convert(dto,
                    ReadAnswerVariant.class)).collect(Collectors.toList()));
                return readQuestion;
            }
        };
    }
}
