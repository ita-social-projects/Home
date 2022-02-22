package com.softserveinc.ita.homeproject.homeservice.dto.poll;


import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollTypeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollShortenDto extends BaseDto {

    private String header;

    private LocalDateTime creationDate;

    private String description;

    private LocalDateTime completionDate;

    private List<HouseDto> polledHouses;

    private PollTypeDto type;
}
