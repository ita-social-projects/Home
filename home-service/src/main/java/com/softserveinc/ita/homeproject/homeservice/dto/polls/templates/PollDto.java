package com.softserveinc.ita.homeproject.homeservice.dto.polls.templates;

import java.time.LocalDateTime;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.HouseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.enums.PollStatusDto;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.enums.PollTypeDto;
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
