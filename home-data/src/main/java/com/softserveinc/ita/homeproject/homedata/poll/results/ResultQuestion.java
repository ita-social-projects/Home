package com.softserveinc.ita.homeproject.homedata.poll.results;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.converters.PollQuestionTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "result_questions")
@SequenceGenerator(name = "sequence", sequenceName = "result_questions_sequence")
public class ResultQuestion extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "answer_variant_id")
    private AnswerVariant answerVariant;

    //TODO: this field needs to be removed in task #417
    @Convert(converter = PollQuestionTypeAttributeConverter.class)
    @JoinColumn(name = "type")
    private PollQuestionType type;

    @ManyToOne
    @JoinColumn(name = "poll_id")
    private Poll poll;

    @Column(name = "vote_count")
    private int voteCount;

    @Column(name = "percent_votes")
    private String percentVotes;
}
