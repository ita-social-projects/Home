package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class VoteDto extends BaseDto {
    private Long pollId;

    private Long userId;

    private List<QuestionVoteDto> questionVotes;
}
