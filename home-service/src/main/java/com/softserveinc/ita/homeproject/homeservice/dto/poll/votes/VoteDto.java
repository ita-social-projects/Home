package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.question.PollQuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDto extends BaseDto {
    private Poll poll;

    private Long userId;

    private String type;

    private String adviceAnswer;

    private PollQuestionDto questionDto;
}
