package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuestionVoteDto extends BaseDto {

    private Long voteId;

    private PollQuestionTypeDto type;

    private PollQuestionDto question;
}
