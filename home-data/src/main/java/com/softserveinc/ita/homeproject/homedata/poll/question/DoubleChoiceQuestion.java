package com.softserveinc.ita.homeproject.homedata.poll.question;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("double_choice")
public class DoubleChoiceQuestion extends MultipleChoiceQuestion{
}
