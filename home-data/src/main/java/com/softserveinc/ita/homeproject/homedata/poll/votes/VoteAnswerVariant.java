package com.softserveinc.ita.homeproject.homedata.poll.votes;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
@Table(name = "votes_answer_variants")
@SequenceGenerator(name = "sequence", sequenceName = "votes_answer_variants_sequence")
public class VoteAnswerVariant extends BaseEntity {

    @OneToOne
    @JoinColumn(name = "vote_id", nullable = false)
    private Vote vote;

    @OneToMany
    @JoinColumn(name = "answer_variant_id", nullable = false)
    private List<AnswerVariant> answerVariants;
}
