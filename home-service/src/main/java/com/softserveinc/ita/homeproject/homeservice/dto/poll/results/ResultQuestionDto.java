package com.softserveinc.ita.homeproject.homeservice.dto.poll.results;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultQuestionDto extends BaseDto {

    private AnswerVariantDto answerVariantDto;

    private String percentVotes;

    private int voteCount;
}
