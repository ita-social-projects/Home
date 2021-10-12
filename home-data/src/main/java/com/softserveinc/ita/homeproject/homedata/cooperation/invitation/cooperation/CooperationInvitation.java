package com.softserveinc.ita.homeproject.homedata.cooperation.invitation.cooperation;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.Invitation;
import com.softserveinc.ita.homeproject.homedata.cooperation.invitation.converters.InvitationRoleConverter;
import com.softserveinc.ita.homeproject.homedata.user.role.RoleEnum;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@DiscriminatorValue("cooperation")
public class CooperationInvitation extends Invitation {

    @Column(name = "role")
    @Convert(converter = InvitationRoleConverter.class)
    private RoleEnum role;

    @Column(name = "cooperation_id")
    private Long cooperationId;

}
