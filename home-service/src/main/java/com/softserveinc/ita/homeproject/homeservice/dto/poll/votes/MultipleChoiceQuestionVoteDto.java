package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MultipleChoiceQuestionVoteDto extends QuestionVoteDto {

    private List<VoteQuestionVariantDto> answers;
}
