package com.softserveinc.ita.homeproject.homedata.poll.votes;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.poll.Poll;
import com.softserveinc.ita.homeproject.homedata.poll.converters.PollQuestionTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollQuestionType;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import com.softserveinc.ita.homeproject.homedata.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "votes")
@SequenceGenerator(name = "sequence", sequenceName = "votes_sequence")
public class Vote extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "poll_id", nullable = false)
    private Poll poll;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Convert(converter = PollQuestionTypeAttributeConverter.class)
    @Column(name = "type", insertable = false, updatable = false)
    private PollQuestionType type;

    @Column(name = "advice_answer")
    private String adviceAnswer;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private PollQuestion question;

    @OneToOne(mappedBy = "vote")
    private VoteAnswerVariant voteAnswerVariant;

}
