package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteQuestionVariantDto extends BaseDto {
    private Long questionVoteId;

    private AnswerVariantDto answerVariant;
}
