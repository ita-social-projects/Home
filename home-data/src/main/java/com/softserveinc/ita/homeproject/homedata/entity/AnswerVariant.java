package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "answer_variants")
@SequenceGenerator(name = "sequence", sequenceName = "answer_variants_sequence")
public class AnswerVariant extends BaseEntity{

    @JoinColumn(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "poll_question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;
}
