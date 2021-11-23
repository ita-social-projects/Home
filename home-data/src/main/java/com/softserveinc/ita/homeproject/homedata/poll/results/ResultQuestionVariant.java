package com.softserveinc.ita.homeproject.homedata.poll.results;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.poll.question.AnswerVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "result_question_variants")
public class ResultQuestionVariant extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "result_question_id")
    private MultipleResultQuestion resultQuestion;

    @OneToOne
    @JoinColumn(name = "answer_variant_id")
    private AnswerVariant answerVariant;

    @Column(name = "vote_count")
    int count;
}
