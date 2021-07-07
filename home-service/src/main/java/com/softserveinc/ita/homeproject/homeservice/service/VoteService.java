package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.CreateVoteDto;
import com.softserveinc.ita.homeproject.homeservice.dto.ReadVoteDto;

public interface VoteService {
    ReadVoteDto createVote(Long pollId, User currentUser, CreateVoteDto voteDto);
}
