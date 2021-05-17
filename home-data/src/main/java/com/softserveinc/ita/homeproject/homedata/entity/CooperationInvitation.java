package com.softserveinc.ita.homeproject.homedata.entity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.JoinColumn;

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
}
