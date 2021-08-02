package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VoteDto extends BaseDto {
    private Long pollId;

    private Long userId;

    private List<QuestionVoteDto> questionVotes;
}
