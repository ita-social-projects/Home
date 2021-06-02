package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

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
}
