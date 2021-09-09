package com.softserveinc.ita.homeproject.homeservice.dto.polls.votes;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuestionVoteDto extends QuestionVoteDto {

    private List<VoteQuestionVariantDto> answers;
}
