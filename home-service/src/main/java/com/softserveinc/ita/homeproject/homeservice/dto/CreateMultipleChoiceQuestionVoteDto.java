package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateMultipleChoiceQuestionVoteDto extends CreateQuestionVoteDto {
    private List<Long> answerVariantIds;
}
