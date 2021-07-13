package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateQuestionVoteDto extends BaseDto {

    private Long voteId;

    private Long questionId;
}
