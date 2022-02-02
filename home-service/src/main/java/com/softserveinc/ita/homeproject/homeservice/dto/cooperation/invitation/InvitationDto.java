package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation;

import com.softserveinc.ita.homeproject.homedata.general.address.Address;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationStatusDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.enums.InvitationTypeDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class InvitationDto extends BaseDto {

    private InvitationStatusDto status;

    private InvitationTypeDto type;

    private String email;

    private String registrationToken;

    private Address address;

    private Long houseId;
}
