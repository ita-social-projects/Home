package com.softserveinc.ita.homeproject.homeservice.service.poll;

import com.softserveinc.ita.homeproject.homedata.entity.poll.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface PollService extends QueryableService<Poll, PollDto> {
    PollDto create(Long cooperationId, PollDto pollDto);

    PollDto update(Long cooperationId, Long id, PollDto pollDto);

    void deactivate(Long id);
}
