package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuestionVoteDto extends BaseDto {
    private PollQuestionTypeDto type;

    private Long voteId;

    private PollQuestionDto question;
}
