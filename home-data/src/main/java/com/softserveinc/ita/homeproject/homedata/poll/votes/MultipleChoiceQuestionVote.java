package com.softserveinc.ita.homeproject.homedata.poll.votes;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("multiple_choice")
public class MultipleChoiceQuestionVote extends QuestionVote {

    @OneToMany(mappedBy = "questionVote", cascade = CascadeType.PERSIST)
    private List<VoteQuestionVariant> answers;
}
