package com.softserveinc.ita.homeproject.homedata.poll;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.softserveinc.ita.homeproject.homedata.BaseEntity;
import com.softserveinc.ita.homeproject.homedata.cooperation.Cooperation;
import com.softserveinc.ita.homeproject.homedata.cooperation.house.House;
import com.softserveinc.ita.homeproject.homedata.poll.converters.PollStatusAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.poll.converters.PollTypeAttributeConverter;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollStatus;
import com.softserveinc.ita.homeproject.homedata.poll.enums.PollType;
import com.softserveinc.ita.homeproject.homedata.poll.question.PollQuestion;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "polls")
@SequenceGenerator(name = "sequence", sequenceName = "polls_sequence")
public class Poll extends BaseEntity {
    @Column(name = "header")
    private String header;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @Column(name = "completion_date")
    private LocalDateTime completionDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    @Column(name = "description")
    private String description;

    @ManyToMany
    @JoinTable(name = "poll_house",
        joinColumns = @JoinColumn(name = "poll_id"),
        inverseJoinColumns = @JoinColumn(name = "house_id")
    )
    private List<House> polledHouses;

    @Convert(converter = PollStatusAttributeConverter.class)
    @Column(name = "status")
    private PollStatus status;

    @Convert(converter = PollTypeAttributeConverter.class)
    @Column(name = "type")
    private PollType type;

    @ManyToOne
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

    @Column(name = "enabled")
    private Boolean enabled;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.PERSIST)
    private List<PollQuestion> pollQuestions;

    @Column(name = "result")
    private String result;
}
