package com.softserveinc.ita.homeproject.homedata.poll.votes;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.softserveinc.ita.homeproject.homedata.poll.results.AdviceResultQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("advice")
public class AdviceQuestionVote extends QuestionVote {
    @Column(name = "answer")
    private String answer;

    @ManyToOne
    @JoinColumn(name = "advice_result_question_id")
    private AdviceResultQuestion resultQuestion;
}
