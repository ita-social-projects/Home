package com.softserveinc.ita.homeproject.homeservice.dto.polls.results;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homeservice.dto.polls.templates.AnswerVariantDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultQuestionVariantDto extends BaseEntity {

    private MultipleResultQuestionDto resultQuestion;

    private AnswerVariantDto answerVariant;

    int count;
}
