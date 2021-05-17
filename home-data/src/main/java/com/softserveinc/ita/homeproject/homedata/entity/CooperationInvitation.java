package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

import java.time.LocalDateTime;

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
}
