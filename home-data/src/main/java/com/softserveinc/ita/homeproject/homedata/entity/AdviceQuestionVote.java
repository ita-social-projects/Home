package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("advice")
public class AdviceQuestionVote extends QuestionVote {
    @OneToOne
    @JoinColumn(name = "answer_variant_id")
    private AnswerVariant answer;
}
