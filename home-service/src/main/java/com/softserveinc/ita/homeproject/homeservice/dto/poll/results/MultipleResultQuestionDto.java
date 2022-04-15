package com.softserveinc.ita.homeproject.homeservice.dto.poll.results;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MultipleResultQuestionDto extends ResultQuestionDto {

    private List<ResultQuestionVariantDto> variants;
}
