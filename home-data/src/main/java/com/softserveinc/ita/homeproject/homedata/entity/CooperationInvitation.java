package com.softserveinc.ita.homeproject.homedata.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Getter
@Setter
@Entity
@DiscriminatorValue("cooperation")
public class CooperationInvitation extends Invitation{

    @Column(name = "role")
    @Convert(converter = InvitationRoleConverter.class)
    private RoleEnum role;

    @Column(name = "cooperation_name")
    private String cooperationName;
}
