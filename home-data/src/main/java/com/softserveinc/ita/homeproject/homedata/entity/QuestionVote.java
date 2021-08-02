package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "question_votes")
@SequenceGenerator(name = "sequence", sequenceName = "question_votes_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class QuestionVote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Convert(converter = PollQuestionTypeAttributeConverter.class)
    @Column(name = "type", insertable = false, updatable = false)
    private PollQuestionType type;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private PollQuestion question;
}
