package com.softserveinc.ita.homeproject.homeservice.dto.poll.results;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class MultipleResultQuestionDto extends ResultQuestionDto {

    private List<ResultQuestionVariantDto> variants;
}
