package com.softserveinc.ita.homeproject.homeservice.dto.poll.results;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResultQuestionVariantDto extends BaseEntity {

    private MultipleResultQuestionDto resultQuestion;

    private AnswerVariantDto answerVariant;

    int count;
}
