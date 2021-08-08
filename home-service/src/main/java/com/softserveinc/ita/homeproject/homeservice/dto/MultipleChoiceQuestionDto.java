package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuestionDto extends PollQuestionDto {

    private Integer maxAnswerCount;

    private List<AnswerVariantDto> answerVariants;
}
