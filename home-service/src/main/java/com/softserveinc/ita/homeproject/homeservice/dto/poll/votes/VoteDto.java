package com.softserveinc.ita.homeproject.homeservice.dto.poll.votes;

import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class VoteDto extends BaseDto {
    private Long pollId;

    private Long userId;

    private List<QuestionVoteDto> questionVotes;
}
