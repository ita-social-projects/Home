package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadMultipleChoiceQuestionVoteDto extends ReadQuestionVoteDto {
    private List<ReadAnswerVariantDto> answers;
}
