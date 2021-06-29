package com.softserveinc.ita.homeproject.homedata.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "votes")
@SequenceGenerator(name = "sequence", sequenceName = "votes_sequence")
public class Vote extends BaseEntity {

    @Column(name = "poll_id")
    private Long pollId;

    @OneToMany(mappedBy = "vote", cascade = CascadeType.PERSIST)
    private List<QuestionVote> questionVotes;
}
