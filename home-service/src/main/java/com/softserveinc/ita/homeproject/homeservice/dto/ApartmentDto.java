package com.softserveinc.ita.homeproject.homeservice.dto;

import java.math.BigDecimal;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApartmentDto extends BaseDto {

    private String apartmentNumber;

    private BigDecimal apartmentArea;

    List<InvitationDto> invitations;

    List<OwnershipDto> ownerships;
}
