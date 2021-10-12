package com.softserveinc.ita.homeproject.homeservice.dto.poll.question;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AnswerVariantDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleChoiceQuestionDto extends PollQuestionDto {

    private Integer maxAnswerCount;

    private List<AnswerVariantDto> answerVariants;
}
