package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homeservice.dto.CreateVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadVoteDto;

public interface VoteService {
    ReadVoteDto createVote(Long pollId, CreateVoteDto voteDto);
}
