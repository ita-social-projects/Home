package com.softserveinc.ita.homeproject.homedata.poll.results;

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
public class MultipleResultQuestion extends ResultQuestion {

    @OneToMany(mappedBy = "resultQuestion", cascade = CascadeType.PERSIST)
    private List<ResultQuestionVariant> variants;
}
