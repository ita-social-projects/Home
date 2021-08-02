package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.User;
import com.softserveinc.ita.homeproject.homeservice.dto.VoteDto;

public interface VoteService {
    VoteDto createVote(User currentUser, VoteDto voteDto);
}
