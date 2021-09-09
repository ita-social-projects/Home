package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.polls.votes.VoteDto;

public interface VoteService {
    VoteDto createVote(VoteDto voteDto);
}
