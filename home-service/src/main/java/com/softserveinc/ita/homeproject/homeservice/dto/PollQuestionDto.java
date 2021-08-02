package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollQuestionDto extends BaseDto {

    private PollQuestionTypeDto type;

    private String question;

    private Integer maxAnswerCount;

    private List<AnswerVariantDto> answerVariants;
}
