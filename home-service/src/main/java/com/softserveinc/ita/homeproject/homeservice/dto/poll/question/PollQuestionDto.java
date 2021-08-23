package com.softserveinc.ita.homeproject.homeservice.dto.poll.question;

import java.util.List;

import com.softserveinc.ita.homeproject.homedata.entity.poll.question.AnswerVariant;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PollQuestionDto extends BaseDto {

    private PollQuestionTypeDto type;

    private String question;

    private Integer maxAnswerCount;

    private List<AnswerVariant> answerVariants;
}
