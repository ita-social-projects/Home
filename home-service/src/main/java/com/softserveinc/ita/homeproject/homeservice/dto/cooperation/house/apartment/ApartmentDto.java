package com.softserveinc.ita.homeproject.homeservice.dto.cooperation.house.apartment;

import java.math.BigDecimal;
import java.util.List;

import com.softserveinc.ita.homeproject.homeservice.dto.BaseDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.invitation.ApartmentInvitationDto;
import com.softserveinc.ita.homeproject.homeservice.dto.cooperation.ownership.OwnershipDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentDto extends BaseDto {

    private String apartmentNumber;

    private BigDecimal apartmentArea;

    private List<ApartmentInvitationDto> invitations;

    private List<OwnershipDto> ownerships;
}
