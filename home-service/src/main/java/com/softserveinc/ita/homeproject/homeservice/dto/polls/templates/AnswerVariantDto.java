package com.softserveinc.ita.homeproject.homeservice.dto.polls.templates;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnswerVariantDto extends BaseDto {
    private String answer;
}
