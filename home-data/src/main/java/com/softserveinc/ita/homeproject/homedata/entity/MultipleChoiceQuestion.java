package com.softserveinc.ita.homeproject.homedata.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.List;

@Getter
@Setter
@Entity
@DiscriminatorValue("multiple_choice")
public class MultipleChoiceQuestion extends PollQuestion {

    @Column(name = "max_answer_count")
    private Integer maxAnswerCount;

    @OneToMany(mappedBy = "poll_question", cascade = CascadeType.PERSIST)
    private List<AnswerVariant> answerVariants;
}
