package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;

public interface PollHouseService extends QueryableService<Poll, PollDto> {
    PollDto add(Long houseId, Long pollId);

    void remove(Long houseId, Long pollId);
}
