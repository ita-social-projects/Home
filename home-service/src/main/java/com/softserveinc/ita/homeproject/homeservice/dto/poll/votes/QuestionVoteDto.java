package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollQuestionTypeDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class QuestionVoteDto extends BaseDto {

    private PollQuestionTypeDto type;

    private PollQuestionDto question;
}
