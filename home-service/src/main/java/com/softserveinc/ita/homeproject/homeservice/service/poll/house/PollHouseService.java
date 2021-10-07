package com.softserveinc.ita.homeproject.homeservice.service.poll.house;

import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.templates.PollDto;
import com.softserveinc.ita.homeproject.homeservice.service.QueryableService;

public interface PollHouseService extends QueryableService<House, HouseDto> {
    PollDto add(Long houseId, Long pollId);

    void remove(Long houseId, Long pollId);
}
