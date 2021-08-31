package com.softserveinc.ita.homeproject.homedata.poll.entity.question;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("multiple_choice")
public class MultipleChoiceQuestion extends PollQuestion {

    @Column(name = "max_answer_count")
    private Integer maxAnswerCount;

    @OneToMany(mappedBy = "multipleChoiceQuestion", cascade = CascadeType.PERSIST)
    private List<AnswerVariant> answerVariants;

}
