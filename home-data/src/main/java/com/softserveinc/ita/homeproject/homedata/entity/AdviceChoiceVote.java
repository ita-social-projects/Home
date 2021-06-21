package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("advice")
public class AdviceChoiceVote extends Vote {

    @Convert(converter = AdviceChoiceAnswerAttributeConverter.class)
    @Column(name = "answer")
    private AdviceChoiceAnswerVariant answer;

    @Column(name = "free_answer")
    private String freeAnswer;
}
