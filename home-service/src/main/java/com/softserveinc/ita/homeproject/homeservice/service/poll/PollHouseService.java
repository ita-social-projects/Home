package com.softserveinc.ita.homeproject.homeservice.service.poll;

import com.softserveinc.ita.homeproject.homedata.entity.house.House;
import com.softserveinc.ita.homeproject.homeservice.dto.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface PollHouseService extends QueryableService<House, HouseDto> {
    PollDto add(Long houseId, Long pollId);

    void remove(Long houseId, Long pollId);
}
