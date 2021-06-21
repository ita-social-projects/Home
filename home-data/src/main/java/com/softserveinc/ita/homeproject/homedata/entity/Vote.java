package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "votes")
@SequenceGenerator(name = "sequence", sequenceName = "votes_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "question_type")
public abstract class Vote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "voter_id")
    private User voter;

    @OneToOne
    @JoinColumn(name = "question_id")
    private PollQuestion question;

    @Convert(converter = PollQuestionTypeAttributeConverter.class)
    @Column(name = "question_type", insertable = false, updatable = false)
    private PollQuestionType type;

    public void setType() {
        this.type = this.question.getType();
    }
}
