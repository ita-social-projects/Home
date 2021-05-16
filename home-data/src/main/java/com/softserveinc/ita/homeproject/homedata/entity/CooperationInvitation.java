package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@MappedSuperclass
public class CooperationInvitation extends Invitation{

    @Column(name = "end_time")
    private LocalDateTime requestEndTime;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
