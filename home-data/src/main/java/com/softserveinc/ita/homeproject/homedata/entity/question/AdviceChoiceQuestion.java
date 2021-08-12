package com.softserveinc.ita.homeproject.homedata.entity.question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.softserveinc.ita.homeproject.homedata.entity.poll.PollQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("advice")
public class AdviceChoiceQuestion extends PollQuestion {
}
