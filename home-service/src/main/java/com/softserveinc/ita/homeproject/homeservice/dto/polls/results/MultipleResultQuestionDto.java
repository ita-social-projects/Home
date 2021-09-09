package com.softserveinc.ita.homeproject.homeservice.dto.polls.results;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MultipleResultQuestionDto extends ResultQuestionDto {

    private List<ResultQuestionVariantDto> variants;
}
