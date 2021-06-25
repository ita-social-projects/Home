package com.softserveinc.ita.homeproject.homeservice.dto;

import com.softserveinc.ita.homeproject.homedata.entity.AnswerVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviceQuestionVoteDto extends QuestionVoteDto {
    private AnswerVariant answerVariant;
}
