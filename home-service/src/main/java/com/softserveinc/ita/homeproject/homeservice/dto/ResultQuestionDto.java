package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class ResultQuestionDto extends BaseDto {

    private PollQuestionTypeDto type;

    private PollQuestionDto question;

    private int voteCount;
}
