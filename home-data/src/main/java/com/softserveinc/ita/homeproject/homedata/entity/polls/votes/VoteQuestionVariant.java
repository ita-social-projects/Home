package com.softserveinc.ita.homeproject.homedata.entity.polls.votes;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.entity.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.entity.polls.templates.AnswerVariant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "vote_question_variants")
@SequenceGenerator(name = "sequence", sequenceName = "vote_question_variants_sequence")
public class VoteQuestionVariant extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "question_vote_id")
    private MultipleChoiceQuestionVote questionVote;

    @OneToOne
    @JoinColumn(name = "answer_variant_id")
    private AnswerVariant answerVariant;
}