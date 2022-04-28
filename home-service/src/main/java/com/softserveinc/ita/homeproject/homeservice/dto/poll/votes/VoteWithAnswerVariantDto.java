package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.results.AnswerVariantDto;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class VoteWithAnswerVariantDto extends BaseDto {

    private Long voteId;

    private List<AnswerVariantDto> answerVariantDtos;
}
