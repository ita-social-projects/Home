package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVoteDto extends BaseDto {

    private Long pollId;

    private List<CreateQuestionVoteDto> questionVoteDtos;
}
