package com.softserveinc.ita.homeproject.homedata.entity.invitation;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.softserveinc.ita.homeproject.homedata.entity.converters.InvitationRoleConverter;
import com.softserveinc.ita.homeproject.homedata.entity.role.RoleEnum;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@DiscriminatorValue("cooperation")
public class CooperationInvitation extends Invitation {

    @Column(name = "end_time")
    private LocalDateTime requestEndTime;

    @Column(name = "role")
    @Convert(converter = InvitationRoleConverter.class)
    private RoleEnum role;

    @Column(name = "cooperation_name")
    private String cooperationName;
}
