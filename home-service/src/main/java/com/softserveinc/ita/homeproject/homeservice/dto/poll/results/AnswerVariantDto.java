package com.softserveinc.ita.homeproject.homeservice.dto.poll.results;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AnswerVariantDto extends BaseDto {
    private String answer;
}
