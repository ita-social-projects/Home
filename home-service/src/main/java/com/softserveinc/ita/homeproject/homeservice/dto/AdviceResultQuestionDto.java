package com.softserveinc.ita.homeproject.homeservice.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdviceResultQuestionDto extends ResultQuestionDto {

    private List<Long> answers;
}
