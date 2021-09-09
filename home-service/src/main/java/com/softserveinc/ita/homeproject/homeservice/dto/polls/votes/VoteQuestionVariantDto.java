package com.softserveinc.ita.homeproject.homeservice.dto.polls.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.templates.AnswerVariantDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteQuestionVariantDto extends BaseDto {

    private AnswerVariantDto answerVariant;
}
