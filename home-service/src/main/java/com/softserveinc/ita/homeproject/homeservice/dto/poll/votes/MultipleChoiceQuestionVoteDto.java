package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MultipleChoiceQuestionVoteDto extends QuestionVoteDto {

    private List<VoteQuestionVariantDto> answers;
}
