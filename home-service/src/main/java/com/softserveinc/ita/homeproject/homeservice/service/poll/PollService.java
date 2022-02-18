package com.softserveinc.ita.homeproject.homeservice.service.poll;

import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollShortenDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public interface PollService extends QueryableService<Poll, PollDto> {
    PollDto create(Long cooperationId, PollDto pollDto);

    PollDto update(Long cooperationId, Long id, PollDto pollDto);

    Page<PollShortenDto> findAllShorten(Integer pageNumber, Integer pageSize, Specification<Poll> specification);

    void deactivate(Long id);
}
