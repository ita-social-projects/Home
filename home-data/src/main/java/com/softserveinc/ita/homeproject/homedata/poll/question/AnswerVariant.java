package com.softserveinc.ita.homeproject.homedata.poll.question;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "answer_variants")
@SequenceGenerator(name = "sequence", sequenceName = "answer_variants_sequence")
public class AnswerVariant extends BaseEntity {

    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "poll_question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;
}
