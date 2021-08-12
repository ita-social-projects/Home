package com.softserveinc.ita.homeproject.homedata.entity.polls.results;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.entity.polls.converters.PollQuestionTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.entity.polls.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.PollQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "result_questions")
@SequenceGenerator(name = "sequence", sequenceName = "result_questions_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class ResultQuestion extends BaseEntity {

    @Convert(converter = PollQuestionTypeAttributeConverter.class)
    @Column(name = "type", insertable = false, updatable = false)
    private PollQuestionType type;

    @OneToOne
    @JoinColumn(name = "question_id")
    private PollQuestion question;

    @Column(name = "vote_count")
    private int voteCount;
}
