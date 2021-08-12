package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviceQuestionVoteDto extends QuestionVoteDto {

    private String answer;

    private AdviceResultQuestionDto resultQuestion;
}
