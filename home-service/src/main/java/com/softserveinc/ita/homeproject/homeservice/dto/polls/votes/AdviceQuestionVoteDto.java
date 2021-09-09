package com.softserveinc.ita.homeproject.homeservice.dto.polls.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.polls.results.AdviceResultQuestionDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviceQuestionVoteDto extends QuestionVoteDto {

    private String answer;

    private AdviceResultQuestionDto resultQuestion;
}
