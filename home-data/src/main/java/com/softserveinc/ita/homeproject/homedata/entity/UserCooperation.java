package com.softserveinc.ita.homeproject.homedata.entity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_cooperation")
@SequenceGenerator(name = "sequence", sequenceName = "user_cooperation_sequence")
public class UserCooperation extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "cooperation_id")
    private Cooperation cooperation;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}
