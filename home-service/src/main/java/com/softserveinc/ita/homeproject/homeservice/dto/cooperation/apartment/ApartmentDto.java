package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.apartment;

import java.math.BigDecimal;
import java.util.List;

import com.softserveinc.ita.homeproject.homedata.general.address.Address;
import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.apartment.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.user.ownership.OwnershipDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentDto extends BaseDto {

    private String apartmentNumber;

    private BigDecimal apartmentArea;

    private List<ApartmentInvitationDto> invitations;

    private List<OwnershipDto> ownerships;

    private Long houseId;
}
