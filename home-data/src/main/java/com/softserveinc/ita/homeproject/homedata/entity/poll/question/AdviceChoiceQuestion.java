package com.softserveinc.ita.homeproject.homedata.entity.poll.question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("advice")
public class AdviceChoiceQuestion extends PollQuestion {
}
