package com.softserveinc.ita.homeproject.homeservice.dto.poll.question;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollQuestionTypeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class PollQuestionDto extends BaseDto {

    private PollQuestionTypeDto type;

    private String question;

    private Long pollId;
}
