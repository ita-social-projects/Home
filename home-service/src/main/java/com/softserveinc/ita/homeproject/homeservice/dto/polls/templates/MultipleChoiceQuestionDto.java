package com.softserveinc.ita.homeproject.homeservice.dto.polls.templates;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuestionDto extends PollQuestionDto {

    private Integer maxAnswerCount;

    private List<AnswerVariantDto> answerVariants;
}
