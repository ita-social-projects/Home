package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AnswerVariantDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteQuestionVariantDto extends BaseDto {

    private AnswerVariantDto answerVariant;
}
