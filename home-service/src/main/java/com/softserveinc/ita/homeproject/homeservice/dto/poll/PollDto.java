package com.softserveinc.ita.homeproject.homeservice.dto.poll;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.house.HouseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollDto extends BaseDto {

    private String header;

    private LocalDateTime creationDate;

    private LocalDateTime completionDate;

    private List<HouseDto> polledHouses;

    private PollStatusDto status;

    private PollTypeDto type;
}
