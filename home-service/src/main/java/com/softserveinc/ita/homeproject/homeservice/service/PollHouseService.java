package com.softserveinc.ita.homeproject.homeservice.service;

import com.softserveinc.ita.homeproject.homedata.entity.House;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.PollDto;

public interface PollHouseService extends QueryableService<House, HouseDto> {
    PollDto add(Long houseId, Long pollId);

    void remove(Long houseId, Long pollId);
}
