package com.softserveinc.ita.homeproject.homeservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdviceQuestionVoteDto extends CreateQuestionVoteDto {
    private String answer;
}
