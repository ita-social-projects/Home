package com.softserveinc.ita.homeproject.homedata.entity;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
public class QuestionVote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "vote_id")
    private Vote vote;

    @Column(name = "question_id")
    private Long questionId;

    @ManyToMany
    @JoinTable(name = "quest_vote_answer_var",
        joinColumns = @JoinColumn(name = "question_vote_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_variant_id")
    )
    private List<AnswerVariant> answerVariants;
}
