package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.VoteDto;

public interface VoteService {
    VoteDto createVote(Long pollId, VoteDto voteDto);
}
