package com.softserveinc.ita.homeproject.homeservice.dto.polls.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.enums.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.templates.PollQuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuestionVoteDto extends BaseDto {

    private PollQuestionTypeDto type;

    private PollQuestionDto question;
}
