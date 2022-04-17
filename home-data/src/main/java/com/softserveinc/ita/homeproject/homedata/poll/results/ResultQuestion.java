package com.softserveinc.ita.homeproject.homedata.poll.results;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
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

    @Column(name = "vote_count")
    private int voteCount;

    @Column(name = "percent_votes")
    private String percentVotes;
}
