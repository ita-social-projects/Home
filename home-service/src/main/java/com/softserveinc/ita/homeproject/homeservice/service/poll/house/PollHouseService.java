package com.softserveinc.ita.homeproject.homeservice.service.poll.house;

import com.softserveinc.ita.homeproject.homedata.entity.cooperation.house.House;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface PollHouseService extends QueryableService<House, HouseDto> {
    PollDto add(Long houseId, Long pollId);

    void remove(Long houseId, Long pollId);
}
