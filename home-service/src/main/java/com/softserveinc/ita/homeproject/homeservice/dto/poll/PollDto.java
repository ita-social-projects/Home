package com.softserveinc.ita.homeproject.homeservice.dto.poll;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.dto.poll.enums.PollTypeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PollDto extends BaseDto {

    private String header;

    private LocalDateTime creationDate;

    private String description;

    private LocalDateTime completionDate;

    private List<HouseDto> polledHouses;

    private List<Long> includedHouses = new ArrayList<>();

    private PollStatusDto status;

    private PollTypeDto type;
}
