package com.softserveinc.ita.homeproject.homedata.entity;

import java.util.List;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("multiple_choice")
public class MultipleChoiceQuestionVote extends QuestionVote {
    @ManyToMany
    @JoinTable(name = "quest_vote_answer_var",
        joinColumns = @JoinColumn(name = "question_vote_id"),
        inverseJoinColumns = @JoinColumn(name = "answer_variant_id")
    )
    private List<AnswerVariant> answers;
}
