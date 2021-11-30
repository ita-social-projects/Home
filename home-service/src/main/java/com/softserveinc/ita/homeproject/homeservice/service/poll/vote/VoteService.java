package com.softserveinc.ita.homeproject.homeservice.service.poll.vote;

import com.softserveinc.ita.homeproject.homeservice.dto.poll.votes.VoteDto;

public interface VoteService {
    VoteDto createVote(VoteDto voteDto);
}
