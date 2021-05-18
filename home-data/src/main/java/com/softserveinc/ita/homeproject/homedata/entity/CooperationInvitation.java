package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@DiscriminatorValue("cooperation")
public class CooperationInvitation extends Invitation{

    @Column(name = "end_time")
    private LocalDateTime requestEndTime;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Column(name = "cooperation_name")
    private String cooperationName;
}
