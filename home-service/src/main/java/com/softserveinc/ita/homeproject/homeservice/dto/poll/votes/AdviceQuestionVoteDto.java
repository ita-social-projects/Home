package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AdviceResultQuestionDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AdviceQuestionVoteDto extends QuestionVoteDto {

    private String answer;

    private AdviceResultQuestionDto resultQuestion;
}
