package com.softserveinc.ita.homeproject.homeservice.service.impl;

import com.softserveinc.ita.homeproject.homedata.entity.Poll;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.HousePollService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

public class HousePollServiceImpl implements HousePollService {

    @Override
    public PollDto add(Long houseId, Long pollId) {
        return null;
    }

    @Override
    public void deactivate(Long houseId, Long pollId) {

    }

    @Override
    public Page<PollDto> findAll(Integer pageNumber, Integer pageSize, Specification<Poll> specification) {
        return null;
    }

}
