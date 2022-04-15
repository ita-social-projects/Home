package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AnswerVariantDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class VoteQuestionVariantDto extends BaseDto {

    private AnswerVariantDto answerVariant;
}
