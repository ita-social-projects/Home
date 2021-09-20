package com.softserveinc.ita.homeproject.homedata.poll.question;

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

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.converters.PollQuestionTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "poll_questions")
@SequenceGenerator(name = "sequence", sequenceName = "poll_questions_sequence")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
public abstract class PollQuestion extends BaseEntity {

    @Convert(converter = PollQuestionTypeAttributeConverter.class)
    @Column(name = "type", insertable = false, updatable = false)
    private PollQuestionType type;

    @Column(name = "question")
    private String question;

    @Column(name = "enabled")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;
}
