package com.softserveinc.ita.homeproject.homedata.poll.results;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.softserveinc.ita.homeproject.homedata.poll.votes.AdviceQuestionVote;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("advice")
public class AdviceResultQuestion extends ResultQuestion {

    @OneToMany(mappedBy = "resultQuestion", cascade = CascadeType.PERSIST)
    private List<AdviceQuestionVote> answers;
}
