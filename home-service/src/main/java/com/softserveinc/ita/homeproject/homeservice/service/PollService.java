package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.CooperationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;

public interface PollService extends QueryableService<Poll, PollDto> {
    PollDto create(CooperationDto cooperationDto, PollDto pollDto);

    PollDto update(PollDto oldPollDto, PollDto updatePollDto);

    void deactivate(PollDto pollDto);
}
