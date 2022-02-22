package com.softserveinc.ita.homeproject.homeservice.service.poll;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

import java.time.LocalDateTime;
import java.util.List;

public interface PollService extends QueryableService<Poll, PollDto> {
    PollDto create(Long cooperationId, PollDto pollDto);

    PollDto update(Long cooperationId, Long id, PollDto pollDto, String description,
                   List<Long> housesID, LocalDateTime creationDate);

    void deactivate(Long id);
}
