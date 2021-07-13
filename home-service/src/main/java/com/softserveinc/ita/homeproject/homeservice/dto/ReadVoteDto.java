package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadVoteDto extends BaseDto {

    private List<ReadQuestionVoteDto> questionVoteDtos;
}
